package com.podosoftware.competency.codeset;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DefaultCodeSetValue implements CodeSetValue {
		
	private int order ;
	private long codeSetId;
	private String name;
	private String description;
	private String stringValue;
	private Long longValue;
	private boolean booleanValue;
	
	private String locale;
		
	public DefaultCodeSetValue() {
		this.order = 0;
		this.codeSetId = -1L;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getCodeSetId() {
		return codeSetId;
	}

	public void setCodeSetId(long codeSetId) {
		this.codeSetId = codeSetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}

}
