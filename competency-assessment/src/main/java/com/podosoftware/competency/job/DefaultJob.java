package com.podosoftware.competency.job;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.cache.CacheSizes;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.support.PropertyAwareSupport;

public class DefaultJob extends PropertyAwareSupport implements Job {

	private Long jobId;

	private Integer objectType;
	
	private Long objectId;
	
	private String name;

	private String description;

	private Date creationDate;

	private Date modifiedDate;

	private Classfication Classfication;

	public DefaultJob() {
		this.jobId = -1L;
		this.objectType = 0 ;
		this.objectId = -1L;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
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

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Classfication getClassfication() {
		return Classfication;
	}

	public void setClassfication(Classfication classfication) {
		Classfication = classfication;
	}

	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return jobId;
	}

	@Override
	public int getModelObjectType() {
		return 60;
	}

	@JsonIgnore
	public int getCachedSize() {
		int totalSize = CacheSizes.sizeOfLong() 
		+ CacheSizes.sizeOfInt() 
		+ CacheSizes.sizeOfLong()
		+ CacheSizes.sizeOfString(name) 
		+ CacheSizes.sizeOfString(description)
		+ CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
		return totalSize;
	}

}
