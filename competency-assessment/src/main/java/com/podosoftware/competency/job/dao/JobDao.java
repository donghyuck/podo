package com.podosoftware.competency.job.dao;

import java.util.List;

import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobCompetencyRelationship;

import architecture.common.user.Company;

public interface JobDao {

	public Long nextJobId();
	
	public void batchInsertJob(List<Job> jobs);
	
	public void batchInsertJobCompetencyRelationship(List<JobCompetencyRelationship> relationships);
	
	public abstract void saveOrUpdateJob(Job job);
	
	public abstract Job getJobById(long jobId);
		
	public abstract void deleteJob(Job job);
	
	public abstract int getJobCount(Company company);
	
	public abstract List<Long> getJobIds(Company company);
	
	public abstract List<Long> getJobIds(Company company, int startIndex, int numResults);
	
	
	public abstract int getJobCount(Company company, Classification classify);
	
	public abstract List<Long> getJobIds(Company company, Classification classify);
	
	public abstract List<Long> getJobIds(Company company, Classification classify, int startIndex, int numResults);
	
	
	public abstract int getJobCount(int objectType, long objectId, Classification classify );
	
	public abstract List<Long> getJobIds(int objectType, long objectId, Classification classify);
	
	public abstract List<Long> getJobIds(int objectType, long objectId, Classification classify, int startIndex, int numResults);
	
	
	public abstract List<Long> getJobCompetencyIds(Job job);
	
	public abstract Long getJobIdByCompetency(Competency competency) ;
}
