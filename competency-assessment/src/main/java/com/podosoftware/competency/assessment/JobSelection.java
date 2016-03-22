package com.podosoftware.competency.assessment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.Cacheable;

public class JobSelection implements Cacheable {

    private Long selectionId;

    private Integer objectType;

    private Long objectId;

    private Long classifyType;

    private Long classifiedMajorityId;

    private Long classifiedMiddleId;

    private Long classifiedMinorityId;

    private String classifyTypeName;

    private String classifiedMajorityName;

    private String classifiedMiddleName;

    private String classifiedMinorityName;

    private Long jobId;

    private String jobName;

    public JobSelection(Long selectionId, Integer objectType, Long objectId, Long classifyType,
	    Long classifiedMajorityId, Long classifiedMiddleId, Long classifiedMinorityId, Long jobId) {
	this.selectionId = selectionId;
	this.objectType = objectType;
	this.objectId = objectId;
	this.classifyType = classifyType;
	this.classifiedMajorityId = classifiedMajorityId;
	this.classifiedMiddleId = classifiedMiddleId;
	this.classifiedMinorityId = classifiedMinorityId;
	this.jobId = jobId;
    }

    public JobSelection() {
	this.selectionId = -1L;
	this.objectType = 0;
	this.objectId = -1L;
	this.classifiedMajorityId = 0L;
	this.classifiedMiddleId = 0L;
	this.classifiedMinorityId = 0L;
	this.jobId = -1L;
    }

    public Long getSelectionId() {
	return selectionId;
    }

    public void setSelectionId(Long selectionId) {
	this.selectionId = selectionId;
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

    public Long getClassifyType() {
	return classifyType;
    }

    public void setClassifyType(Long classifyType) {
	this.classifyType = classifyType;
    }

    public Long getClassifiedMajorityId() {
	return classifiedMajorityId;
    }

    public void setClassifiedMajorityId(Long classifiedMajorityId) {
	this.classifiedMajorityId = classifiedMajorityId;
    }

    public Long getClassifiedMiddleId() {
	return classifiedMiddleId;
    }

    public void setClassifiedMiddleId(Long classifiedMiddleId) {
	this.classifiedMiddleId = classifiedMiddleId;
    }

    public Long getClassifiedMinorityId() {
	return classifiedMinorityId;
    }

    public void setClassifiedMinorityId(Long classifiedMinorityId) {
	this.classifiedMinorityId = classifiedMinorityId;
    }

    @JsonGetter
    public String getClassifyTypeName() {
	return classifyTypeName;
    }

    @JsonIgnore
    public void setClassifyTypeName(String classifyTypeName) {
	this.classifyTypeName = classifyTypeName;
    }

    @JsonGetter
    public String getClassifiedMajorityName() {
	return classifiedMajorityName;
    }

    @JsonIgnore
    public void setClassifiedMajorityName(String classifiedMajorityName) {
	this.classifiedMajorityName = classifiedMajorityName;
    }

    @JsonGetter
    public String getClassifiedMiddleName() {
	return classifiedMiddleName;
    }

    @JsonIgnore
    public void setClassifiedMiddleName(String classifiedMiddleName) {
	this.classifiedMiddleName = classifiedMiddleName;
    }

    @JsonGetter
    public String getClassifiedMinorityName() {
	return classifiedMinorityName;
    }

    @JsonIgnore
    public void setClassifiedMinorityName(String classifiedMinorityName) {
	this.classifiedMinorityName = classifiedMinorityName;
    }

    public Long getJobId() {
	return jobId;
    }

    public void setJobId(Long jobId) {
	this.jobId = jobId;
    }

    @JsonGetter
    public String getJobName() {
	return jobName;
    }

    @JsonIgnore
    public void setJobName(String jobName) {
	this.jobName = jobName;
    }

    public int getCachedSize() {
	return 0;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof JobSelection) {
	    JobSelection job2 = (JobSelection) obj;
	    if (job2.getSelectionId() > 0 && this.selectionId == job2.getSelectionId())
		return true;
	    else
		return false;
	} else {
	    return super.equals(obj);
	}
    }

    @Override
    public String toString() {
	return "JobSelection [selectionId=" + selectionId + ", objectType=" + objectType + ", objectId=" + objectId
		+ ", classifyType=" + classifyType + ", classifiedMajorityId=" + classifiedMajorityId
		+ ", classifiedMiddleId=" + classifiedMiddleId + ", classifiedMinorityId=" + classifiedMinorityId
		+ ", classifyTypeName=" + classifyTypeName + ", classifiedMajorityName=" + classifiedMajorityName
		+ ", classifiedMiddleName=" + classifiedMiddleName + ", classifiedMinorityName="
		+ classifiedMinorityName + ", jobId=" + jobId + ", jobName=" + jobName + "]";
    }

}
