package com.podosoftware.competency.competency;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.model.support.PropertyAwareSupport;


public class DefaultCompetency extends PropertyAwareSupport implements Competency  {

	private int objectType;
	
	private long objectId;
	
	private long competencyId;
	
	private String name;
	
	private String description;
	
	
	public DefaultCompetency() {
		this.objectType = 1;
		this.objectId = -1L;
		this.competencyId = -1L;
		this.name = null;
		this.description = null;
	}


	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}


	public Integer getObjectType() {
		return objectType;
	}


	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}


	public Long getObjectId() {
		return objectId;
	}


	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}


	public Long getCompetencyId() {
		return competencyId;
	}


	public void setCompetencyId(Long competencyId) {
		this.competencyId = competencyId;
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


	public Serializable getPrimaryKeyObject() {
		return this.getCompetencyId();
	}

	public int getModelObjectType() {
		return 53;
	}


}
