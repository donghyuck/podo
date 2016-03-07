package com.podosoftware.sync;

import java.util.HashMap;
import java.util.Map;

import architecture.common.adaptor.Context;

public class DeaultContext implements Context {

	private Map<String, Object> context = new HashMap<String, Object>();
	
	/**
	 * @uml.property  name="connectorName"
	 */
	private String connectorName;
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(String name, Class<T> requiredType) {
		if( context.containsKey(name) )
			return (T)context.get(name);
		else {
			return null;
		}
	}
	
	public Object getObject(String name){
		return context.get(name);
	}

	public void setObject(String key, Object obj) {
		context.put(key, obj);
	}

	public int size() {
		return context.size();
	}

	/**
	 * @return
	 * @uml.property  name="connectorName"
	 */
	public String getConnectorName() {
		return connectorName;
	}

	/**
	 * @param connectorName
	 * @uml.property  name="connectorName"
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public Type getType() {
		return getObject(this.TYPE, Type.class);
	}

}

