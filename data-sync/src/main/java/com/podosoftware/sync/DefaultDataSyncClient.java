package com.podosoftware.sync;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.podosoftware.sync.connector.EsaramReaderConnector;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.Pipeline;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;
import architecture.common.adaptor.processor.ProcessCallback;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.jdbc.TypeAliasRegistry;
import architecture.common.util.StringUtils;


public class DefaultDataSyncClient implements DataSyncClient {

	private Log log = LogFactory.getLog(getClass());
	
	public static final String DEFAULT_READ_CONNECTOR_PREFIX = "READ_";
	
	public static final String DEFAULT_WRITE_CONNECTOR_PREFIX = "WRITE_";

	public static final TypeAliasRegistry DEAFULT_TYPE_ALIAS_REGISTRY =  new TypeAliasRegistry(); 

	private Map<String, DefaultDataSyncMetaInfo> processMappings = new HashMap<String, DefaultDataSyncMetaInfo>();

	private Map<String, List<String>> pipelineMappings = new HashMap<String, List<String>>();
		
	private Map<String, Object> connectors = new HashMap<String, Object>();

	public void setConnectors(Map<String, Object> connectors) {
		this.connectors = connectors;
	}

	public Map<String, DefaultDataSyncMetaInfo> getProcessMappings() {
		return processMappings;
	}

	public Map<String, List<String>> getPipelineMappings() {
		return pipelineMappings;
	}

	public void setPipelineMappings(Map<String, List<String>> pipelineMappings) {
		this.pipelineMappings = pipelineMappings;
	}

	public void setProcessMappings(Map<String, DefaultDataSyncMetaInfo> processMappings) {		
		this.processMappings = processMappings;
	}
		
	public Object process(String processName) {
		return process( processName, new Object[0] );
	}

	public Object process(String processName, Object[] args) {		
		
		// STEP 1. 프로세스 이름에 해당하는 전체 프로세스 메타 정보를 가져온다. 
		List<DefaultDataSyncMetaInfo> processors = getProcessorMetaInfos(processName);
		
		List<Map<String, Object>> input = Collections.EMPTY_LIST;
		// STEP 2. 프로세스를 진행한다.
		for(DefaultDataSyncMetaInfo processor : processors){
			
			// STEP 2.1 프로세스 존재 유무를 검사한다.
			if( hasConnector( processor.connectorName )){
				
				// STEP 2.2 프로세스에 대한 컨택스트를 가져온다.
				Context context = getContext(processor);
				
				if( processor.type == DefaultDataSyncMetaInfo.Type.READ ){
					
					context.setObject(Context.DATA, args);
					
					ReadConnector connector = getConnector( processor.connectorName, ReadConnector.class);
					input = (List<Map<String, Object>>) connector.pull(context);
					
					if( connector instanceof EsaramReaderConnector ){
						// todo : log into jdbc ..						
					}
					
				}else  if( processor.type == DefaultDataSyncMetaInfo.Type.FILTER ){ 
					
					List<Map<String, Object>> inputToUse = new ArrayList<Map<String, Object>>();
					
					log.debug("filter applied!!");
					
					if ( input.size() > 0 ){
						List<ParameterMapping> mappings = processor.getParameterMappings();						
						for( Map<String, Object> row : input ){
							//log.debug(row);
							boolean filtered = false;
							for(ParameterMapping m : mappings){
								if(m.getJavaType() == String.class ){
									Object v = row.get(m.getProperty());
									String valueToUse = (String) v;
									if( !StringUtils.isEmpty(valueToUse) && (valueToUse.length() > m.getSize()) ){										
										filtered = true;		
									}
								}
							}
							if(!filtered){	
								inputToUse.add(row);
							}
						}				
						
						input = inputToUse;
					}
					
				} else if( processor.type == DefaultDataSyncMetaInfo.Type.MERGE ){

					// MATCH DATA
					context.setObject(Context.DATA, args);
					String primaryKey = processor.getPrimaryParameterMapping().getProperty();
					
					List<String> list = new ArrayList<String>();					
					for ( Map<String, Object> row : (List<Map<String, Object>>) getConnector( processor.connectorName, ReadConnector.class).pull(context) ){
						String value = (String)row.get(primaryKey);
						list.add(value);
					}
					
					log.debug("merge (" + primaryKey + "):" + list.size() + " with " + input.size());
					
					List<Map<String, Object>> insert = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> update = new ArrayList<Map<String, Object>>();
					
					// READ DATA
					for( Map<String, Object> row : input){						
						String value = (String)row.get(primaryKey);
						boolean merge = false;	
						
						for(String no : list )
						{
							if(value.equals(no)){
								merge = true;
				                break;
							}
						}	
						
						if(merge){
							update.add(row);
						}else{
							insert.add(row);
						}
					}
					
					log.debug("update:" + update.size() );
					log.debug("insert`:" + insert.size() );
					
					for(Pipeline p : processor.getPipelineMappings())
					{
						log.debug( "[Pipeline]"+ p.getName() + ", " + p.getIndex() + ", " + p.isMatch() );
						
						if(processMappings.containsKey(p.getName())){
							DefaultDataSyncMetaInfo subProcessor = processMappings.get(p.getName());
							Context subContext = getContext(subProcessor);
							if( subProcessor.type == DefaultDataSyncMetaInfo.Type.WRITE ){
								if( p.isMatch() ){
									subContext.setObject(Context.DATA, update);
									getConnector( subProcessor.connectorName, WriteConnector.class ).deliver(subContext);
								}else {
									subContext.setObject(Context.DATA, insert);
									getConnector( subProcessor.connectorName, WriteConnector.class ).deliver(subContext);
								}
							}
						}
					}
					
				} else if ( processor.type == DefaultDataSyncMetaInfo.Type.WRITE){
					context.setObject(Context.DATA, input);
					getConnector( processor.connectorName, WriteConnector.class ).deliver(context);
				}
			}
		}
		return input;
	}
		

	public <T> T process(String processName, Object[] args, ProcessCallback<T> action) {
		
		List<DefaultDataSyncMetaInfo> processors = getProcessorMetaInfos(processName);
		List<Context> contexts = new ArrayList<Context>();		
		//List<Map<String, Object>> input = Collections.EMPTY_LIST;
		for(DefaultDataSyncMetaInfo processor : processors){
			if( hasConnector( processor.connectorName )){
				Context context = getContext(processor);
				if( processor.type == DefaultDataSyncMetaInfo.Type.READ ){
					context.setObject("data", args);
					List<Map<String, Object>> input = (List<Map<String, Object>>) getConnector( processor.connectorName, ReadConnector.class).pull(context);
					context.setObject("output", input);
					contexts.add(context);
				}
			}
		}		
		return action.doInProcess();
		
	}

	@SuppressWarnings("unchecked")
	public List<DefaultDataSyncMetaInfo> getProcessorMetaInfos(String processName){
		if( pipelineMappings.containsKey(processName) ){
			List<DefaultDataSyncMetaInfo> processors = new LinkedList<DefaultDataSyncMetaInfo>();
			for( String name :  pipelineMappings.get(processName)){
				if(processMappings.containsKey(name))
					processors.add(processMappings.get(name));
			}
			return processors;
		}
		else {
			return Collections.EMPTY_LIST;
		}		
	}
	
	
	protected Context getContext(DefaultDataSyncMetaInfo processor){
		
		DeaultContext context = new DeaultContext();
		
		if( processor != null ){
			context.setObject("parameterMappings", processor.parameterMappings);
			context.setObject("properties", processor.getProperties());
			
			context.setObject("queryString", processor.queryString);
			context.setObject("queryName", processor.queryName);
			context.setObject("batch", processor.isBatch);
			
			context.setConnectorName(processor.connectorName);
			
			if( processor.type == DefaultDataSyncMetaInfo.Type.READ || 
				processor.type == DefaultDataSyncMetaInfo.Type.MERGE )
				context.setObject(Context.TYPE, Context.Type.INPUT);
			else 
				context.setObject(Context.TYPE, Context.Type.OUTPUT);
		}
		
		return context;
	}
	
	protected Context getContext(String processName){
		DefaultDataSyncMetaInfo metaInfo = processMappings.get(processName);			
		return getContext(metaInfo);
	}
	
	
	/**
	 * 
	 * @param name
	 * @return name 에 해당하는 커넥터 존재 유무를 리턴한다.
	 */
	protected boolean hasConnector(String name){
		return connectors.containsKey(name);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param requiredType
	 * @return name 에 해당하는 커낵터를 requiredType 으로 형변환하여 리턴한다.
	 */
	protected <T> T getConnector(String name, Class<T> requiredType){
		if(connectors.containsKey(name))
			return (T) connectors.get(name);
		return null;
	}	

	public List<Map<String, Object>> read(String processName) {
		return read(processName, new Object[0]);
	}
	
	public List<Map<String, Object>> read(String processName, Object[] args) {
		Context context = getContext(DEFAULT_READ_CONNECTOR_PREFIX + processName);
		context.setObject("data", args);		
		return (List<Map<String, Object>>) getConnector(context.getConnectorName(), ReadConnector.class).pull(context);
	}	
	
	
	public Object write(String processName, List<Map<String, Object>> input) {		
		Context context = getContext(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
		context.setObject("data", input);		
		return getConnector( context.getConnectorName(), WriteConnector.class ).deliver(context);		
	}
	
	public Object write(String processName, Map<String, Object> input) {
		
		Context context = getContext(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
		context.setObject("data", input);		
		return getConnector( context.getConnectorName(), WriteConnector.class ).deliver(context);
	
	}
	

	protected boolean hasReadMapping(String processName){		
		return processMappings.containsKey(DEFAULT_READ_CONNECTOR_PREFIX + processName);
	}
	
	protected boolean hasWriterMapping(String processName){
		return processMappings.containsKey(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
	}
		

}
