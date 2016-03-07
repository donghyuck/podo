package com.podosoftware.sync;

import java.util.List;
import java.util.Map;

import architecture.common.adaptor.Adaptor;
import architecture.common.adaptor.processor.ProcessCallback;

public interface DataSyncClient extends Adaptor {

	public abstract List<Map<String, Object>> read (String processName);
	
	public abstract List<Map<String, Object>> read (String processName, Object[] args);
		
	public Object write(String processName, Map<String, Object> input);
	
	public Object write(String processName, List<Map<String, Object>> input);
		
	public abstract <T> T process ( String processName, Object[] args, ProcessCallback<T> action);
	
	public abstract Object process (String processName);
    
	public abstract Object process (String processName, Object[] args);
	
}
