package com.podosoftware.competency.job;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;

public class DefaultClassfication implements Classfication {

	private Long classifiedMajorityId;

	private Long classifiedMiddleId;

	private Long classifiedMinorityId;

	private String classifiedMajorityName;

	private String classifiedMiddleName;

	private String classifiedMinorityName;

	public DefaultClassfication() {
		
		this.classifiedMajorityId = -1L;
		this.classifiedMiddleId = -1L;
		this.classifiedMinorityId = -1L;
		
	}

	public DefaultClassfication(Long classifiedMajorityId, Long classifiedMiddleId, Long classifiedMinorityId) {
		this.classifiedMajorityId = classifiedMajorityId;
		this.classifiedMiddleId = classifiedMiddleId;
		this.classifiedMinorityId = classifiedMinorityId;
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

	public String getClassifiedMajorityName() {
		return classifiedMajorityName;
	}

	public void setClassifiedMajorityName(String classifiedMajorityName) {
		this.classifiedMajorityName = classifiedMajorityName;
	}

	public String getClassifiedMiddleName() {
		return classifiedMiddleName;
	}

	public void setClassifiedMiddleName(String classifiedMiddleName) {
		this.classifiedMiddleName = classifiedMiddleName;
	}

	public String getClassifiedMinorityName() {
		return classifiedMinorityName;
	}

	public void setClassifiedMinorityName(String classifiedMinorityName) {
		this.classifiedMinorityName = classifiedMinorityName;
	}

	@JsonIgnore
	public int getCachedSize() {
		int totalSize = CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfLong()
				+ CacheSizes.sizeOfString(classifiedMajorityName) 
				+ CacheSizes.sizeOfString(classifiedMiddleName)
				+ CacheSizes.sizeOfString(classifiedMinorityName);
		return totalSize;
	}

}
