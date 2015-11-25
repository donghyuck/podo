package com.podosoftware.competency.job.dao;

import java.util.List;

import com.podosoftware.competency.job.Job;

import architecture.common.user.Company;

public interface JobDao {

	public Long nextJobId();
	
	public void batchInsertJob(List<Job> jobs);
	
	public abstract void saveOrUpdateJob(Job job);
	
	public abstract Job getJobById(long jobId);
		
	public abstract void deleteJob(Job job);
	
	public abstract int getJobCount(Company company);
	
	public abstract List<Long> getJobIds(Company company);
	
	public abstract List<Long> getJobIds(Company company, int startIndex, int numResults);
}
