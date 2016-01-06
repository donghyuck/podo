package com.podosoftware.competency.competency;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.podosoftware.competency.job.Job;

import architecture.common.model.support.PropertyAwareSupport;


public class DefaultCompetency extends PropertyAwareSupport implements Competency  {

	private Integer objectType;
	
	private Long objectId;
	
	private Long competencyId;
	
	private String name;
	
	private String description;
	
	private Integer level;
	
	private String competencyUnitCode;
	
	private String competencyGroupCode;
	
	private CompetencyType competencyType;
	
	private Job job;
	
	public DefaultCompetency() {
		this.objectType = 1;
		this.objectId = -1L;
		this.competencyId = -1L;
		this.name = null;
		this.description = null;
		this.level = 0 ;
		this.competencyUnitCode = null;
		this.competencyGroupCode = null;
		this.competencyType = CompetencyType.NONE;
		this.job = null;
	}
	
	
	
	public String getCompetencyGroupCode() {
		return competencyGroupCode;
	}



	public void setCompetencyGroupCode(String competencyGroupCode) {
		this.competencyGroupCode = competencyGroupCode;
	}



	public String getCompetencyUnitCode() {
		return competencyUnitCode;
	}

	public void setCompetencyUnitCode(String competencyUnitCode) {
		this.competencyUnitCode = competencyUnitCode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	
	public CompetencyType getCompetencyType() {
		return competencyType;
	}

	public void setCompetencyType(CompetencyType competencyType) {
		this.competencyType = competencyType;
	}

	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return this.getCompetencyId();
	}

	public int getModelObjectType() {
		return 53;
	}

	@JsonProperty
	public Job getJob() {
		return job;
	}

	@JsonIgnore
	public void setJob(Job job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return "DefaultCompetency [competencyId=" + competencyId + ", objectType=" + objectType + ", objectId="
				+ objectId + ", competencyType=" + competencyType + ", name=" + name + ", description=" + description
				+ ", level=" + level + ", competencyUnitCode=" + competencyUnitCode + ", job=" + job + "]";
	}
	
	

}
