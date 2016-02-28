package com.podosoftware.competency.job;

public class JobCompetencyRelationship {
	
	private int objectType ;
	private long objectId ;
	private long jobId;
	private long jobLevelId ;
	private long competencyId;
	
	public JobCompetencyRelationship() {
		this.objectType = 0;
		this.objectId = -1L;
		this.competencyId = -1L;
		this.jobId = -1L;
		this.jobLevelId  = -1L;
	}

	public JobCompetencyRelationship(int objectType, long objectId, long jobId, long jobLevelId, long competencyId) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.jobId = jobId;
		this.jobLevelId = jobLevelId;
		this.competencyId = competencyId;
	}

	public JobCompetencyRelationship(int objectType, long objectId, long jobId, long competencyId) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.jobId = jobId;
		this.competencyId = competencyId;
		this.jobLevelId  = -1L;
	}

	public long getJobLevelId() {
		return jobLevelId;
	}

	public void setJobLevelId(long jobLevelId) {
		this.jobLevelId = jobLevelId;
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

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(long competencyId) {
		this.competencyId = competencyId;
	}

	
}
