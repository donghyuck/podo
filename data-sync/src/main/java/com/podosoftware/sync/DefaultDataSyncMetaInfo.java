package com.podosoftware.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import architecture.common.adaptor.Pipeline;
import architecture.common.jdbc.ParameterMapping;

import org.springframework.util.StringUtils;

public class DefaultDataSyncMetaInfo {

	
	public enum Type {
		READ, 
		WRITE,
		FILTER,
		MERGE;
	};	
	
	protected Type type = Type.READ ;
	
	public Type getType() {
		return type;
	}

	protected String name;
	
	protected String queryString = null ;
	
	protected String queryName = null;
	
	protected String connectorName = null;

	protected boolean isBatch = true;
	
	protected Properties properties;
	
	private List<Pipeline> pipelineMappings = new ArrayList<Pipeline>();

	protected List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		
	
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		if(this.properties == null)
			this.properties = new Properties(); 
		this.properties = properties;
	}

	public List<Pipeline> getPipelineMappings() {
		return pipelineMappings;
	}

	public void setPipelineMappings(List<String> pipelineMappings) {
		for( String pipelineMapping : pipelineMappings ){
			String[] arr = StringUtils.commaDelimitedListToStringArray(pipelineMapping);
			Properties props = StringUtils.splitArrayElementsIntoProperties(arr, "=");			
			String name = props.getProperty("name", null);
			String match = props.getProperty("match", "false");
			if( StringUtils.hasText(name)){
				DefaultPipeline pipeline = new DefaultPipeline();
				pipeline.name = name;
				pipeline.match = Boolean.parseBoolean(match);
				this.pipelineMappings.add(pipeline);
			}
		}
	}

	public boolean isBatch() {
		return isBatch;
	}

	public void setTypeString(String typeString) {
		this.type = Type.valueOf(typeString.toUpperCase());	
	}

	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<String> parameterMappings) {
		for( String mappingString : parameterMappings ){
			
			String[] arr = StringUtils.commaDelimitedListToStringArray(mappingString);
			
			Properties props = StringUtils.splitArrayElementsIntoProperties(arr, "=");
			
			String name = props.getProperty("name");
			
			String index = props.getProperty("index", null);
			
			String javaTypeName = props.getProperty("javaType", null);
			
			String jdbcTypeName = props.getProperty("jdbcType", null);
			
			String pattern = props.getProperty("pattern", null);
			
			String encoding = props.getProperty("encoding", null);
			
			String primary = props.getProperty("primary", "false");
			
			String cipher = props.getProperty("cipher", null);

			String cipherKey = props.getProperty("cipherKey", null);
			
			String cipherKeyAlg = props.getProperty("cipherKeyAlg", null);
			
			String sizeString = props.getProperty("size", "0");
			
			ParameterMapping.Builder builder = new ParameterMapping.Builder(name);
						
			if( index != null )
			    builder.index(Integer.parseInt(index));
			if( encoding != null)
				builder.encoding(encoding);
			if( jdbcTypeName != null)
				builder.jdbcTypeName(jdbcTypeName);
			if( javaTypeName != null )
				builder.javaType(DefaultDataSyncClient.DEAFULT_TYPE_ALIAS_REGISTRY.resolveAlias(javaTypeName));
			if( pattern != null )
				builder.pattern(pattern);	
			if( primary != null )
				builder.primary(Boolean.parseBoolean(primary));	
			
			if( cipher != null )
				builder.cipher(cipher);
			
			if( cipherKey != null )
				builder.cipherKey(cipherKey);
			
			if( cipherKeyAlg != null )
				builder.cipherKeyAlg(cipherKeyAlg);
			
			if( sizeString != null )
				builder.size(sizeString);
			
			this.parameterMappings.add(builder.build());			
		}
	}
	
	public boolean hasPrimary(){
		for(ParameterMapping m : parameterMappings)
			if( m.isPrimary() ) 
				return true;
		
		return false;
	}
	
	public ParameterMapping getPrimaryParameterMapping(){
		for(ParameterMapping m : parameterMappings)
			if( m.isPrimary() ) 
				return m;		
		return null;
	} 
	
}

