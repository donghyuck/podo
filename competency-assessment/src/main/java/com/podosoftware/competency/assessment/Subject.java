package com.podosoftware.competency.assessment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.Cacheable;

public class Subject implements Cacheable {

    private long subjectId;

    private int objectType;

    private long objectId;

    private int subjectObjectType;

    private long subjectObjectId;

    private String subjectObjectName;

    public Subject() {
	subjectId = -1L;
	objectType = 0;
	objectId = -1L;
	subjectObjectType = 0;
	subjectObjectId = -1L;
    }

    public Subject(long subjectId, int objectType, long objectId, int subjectObjectType, long subjectObjectId) {
	this.subjectId = subjectId;
	this.objectType = objectType;
	this.objectId = objectId;
	this.subjectObjectType = subjectObjectType;
	this.subjectObjectId = subjectObjectId;
    }

    public long getSubjectId() {
	return subjectId;
    }

    public void setSubjectId(long subjectId) {
	this.subjectId = subjectId;
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

    public int getSubjectObjectType() {
	return subjectObjectType;
    }

    public void setSubjectObjectType(int subjectObjectType) {
	this.subjectObjectType = subjectObjectType;
    }

    public long getSubjectObjectId() {
	return subjectObjectId;
    }

    public void setSubjectObjectId(long subjectObjectId) {
	this.subjectObjectId = subjectObjectId;
    }

    @JsonGetter
    public String getSubjectObjectName() {
	return subjectObjectName;
    }

    @JsonIgnore
    public void setSubjectObjectName(String subjectObjectName) {
	this.subjectObjectName = subjectObjectName;
    }

    @JsonIgnore
    public int getCachedSize() {
	return 0;
    }

    public boolean equals(Object obj) {
	if (obj instanceof Subject) {
	    Subject sbj = (Subject) obj;
	    if (sbj.getSubjectId() > 0 && this.subjectId == sbj.getSubjectId())
		return true;
	    else
		return false;
	} else {
	    return super.equals(obj);
	}
    }

    @Override
    public String toString() {
	return "Subject [subjectId=" + subjectId + ", objectType=" + objectType + ", objectId=" + objectId
		+ ", subjectObjectType=" + subjectObjectType + ", subjectObjectId=" + subjectObjectId
		+ ", subjectObjectName=" + subjectObjectName + "]";
    }

}
