package com.podosoftware.competency.job;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;
import architecture.common.model.ModelObject;

public class JobLevel implements Cacheable, ModelObject {

    private Long jobLevelId;

    private Long jobId;

    private String name;

    private String description;

    private Integer level;

    private Integer minWorkExperienceYear;

    private Integer maxWorkExperienceYear;

    private boolean strong;

    public JobLevel() {
	this.jobLevelId = -1L;
	this.jobId = -1L;
	this.name = null;
	this.description = null;
	this.level = 0;
	this.minWorkExperienceYear = 0;
	this.maxWorkExperienceYear = 0;
	this.strong = false;
    }

    public JobLevel(Long jobLevelId, Long jobId, String name, String description, Integer level,
	    Integer minWorkExperienceYear, Integer maxWorkExperienceYear) {
	this.jobLevelId = jobLevelId;
	this.jobId = jobId;
	this.name = name;
	this.description = description;
	this.level = level;
	this.minWorkExperienceYear = minWorkExperienceYear;
	this.maxWorkExperienceYear = maxWorkExperienceYear;
	this.strong = false;
    }

    public JobLevel(Long jobLevelId, Long jobId, String name, String description, Integer level,
	    Integer minWorkExperienceYear, Integer maxWorkExperienceYear, boolean strong) {
	this.jobLevelId = jobLevelId;
	this.jobId = jobId;
	this.name = name;
	this.description = description;
	this.level = level;
	this.minWorkExperienceYear = minWorkExperienceYear;
	this.maxWorkExperienceYear = maxWorkExperienceYear;
	this.strong = strong;
    }

    /**
     * if is true assosication with competency is strong.
     * 
     * @return
     */
    public boolean isStrong() {
	return strong;
    }

    public void setStrong(boolean strong) {
	this.strong = strong;
    }

    public Long getJobLevelId() {
	return jobLevelId;
    }

    public void setJobLevelId(Long jobLevelId) {
	this.jobLevelId = jobLevelId;
    }

    public Long getJobId() {
	return jobId;
    }

    public void setJobId(Long jobId) {
	this.jobId = jobId;
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

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public Integer getMinWorkExperienceYear() {
	return minWorkExperienceYear;
    }

    public void setMinWorkExperienceYear(Integer minWorkExperienceYear) {
	this.minWorkExperienceYear = minWorkExperienceYear;
    }

    public Integer getMaxWorkExperienceYear() {
	return maxWorkExperienceYear;
    }

    public void setMaxWorkExperienceYear(Integer maxWorkExperienceYear) {
	this.maxWorkExperienceYear = maxWorkExperienceYear;
    }

    @JsonIgnore
    public int getCachedSize() {
	int totalSize = CacheSizes.sizeOfLong() + CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt()
		+ CacheSizes.sizeOfInt() + CacheSizes.sizeOfInt() + CacheSizes.sizeOfString(name)
		+ CacheSizes.sizeOfString(description);
	return totalSize;
    }

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return jobLevelId;
    }

    @JsonIgnore
    public void setModelObjectType(int modelObjectType) {

    }

    public int getModelObjectType() {
	return 61;
    }

    @Override
    public String toString() {
	return "JobLevel [jobLevelId=" + jobLevelId + ", jobId=" + jobId + ", name=" + name + ", description="
		+ description + ", level=" + level + ", minWorkExperienceYear=" + minWorkExperienceYear
		+ ", maxWorkExperienceYear=" + maxWorkExperienceYear + ", strong=" + strong + "]";
    }

    public int hashCode() {
	return new HashCodeBuilder().append(jobLevelId).toHashCode();
    }

    public boolean equals(Object obj) {
	if (obj instanceof JobLevel) {
	    if (this.jobLevelId == ((JobLevel) obj).getJobLevelId())
		return true;
	    return false;
	}
	return super.equals(obj);
    }
}
