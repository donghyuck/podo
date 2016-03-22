package com.podosoftware.competency.competency;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;
import architecture.common.model.support.PropertyAndDateAwareSupport;

public class DefaultPerformanceCriteria extends PropertyAndDateAwareSupport implements PerformanceCriteria {

    private int objectType;
    private long objectId;
    private int sortOrder;
    private long performanceCriteriaId;
    private String description;

    public DefaultPerformanceCriteria() {
	this.objectType = 0;
	this.objectId = -1L;
	this.sortOrder = 0;
	this.performanceCriteriaId = -1L;
	this.description = null;
	Date now = new Date();
	setCreationDate(now);
	setModifiedDate(now);
    }

    public int getSortOrder() {
	return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
	this.sortOrder = sortOrder;
    }

    public int getObjectType() {
	return objectType;
    }

    public void setObjectType(int objectType) {
	this.objectType = objectType;
    }

    public long getObjectId() {
	return objectId;
    }

    public void setObjectId(long objectId) {
	this.objectId = objectId;
    }

    public long getPerformanceCriteriaId() {
	return performanceCriteriaId;
    }

    public void setPerformanceCriteriaId(long performanceCriteriaId) {
	this.performanceCriteriaId = performanceCriteriaId;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return performanceCriteriaId;
    }

    @JsonIgnore
    public int getCachedSize() {
	int cacheSize = CacheSizes.sizeOfInt() + CacheSizes.sizeOfLong() + CacheSizes.sizeOfLong()
		+ CacheSizes.sizeOfString(description) + (CacheSizes.sizeOfDate() * 2)
		+ CacheSizes.sizeOfMap(getProperties());

	return cacheSize;
    }

    @JsonIgnore
    public int getModelObjectType() {
	return 55;
    }

}