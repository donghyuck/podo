package com.podosoftware.competency.job;

import java.util.List;

import com.podosoftware.competency.competency.Competency;

import architecture.common.user.Company;

public interface JobManager {

	public abstract int getJobCount(Company company);
	
	public abstract List<Job> getJobs(Company company) ;
	
	public abstract List<Job> getJobs(Company company, int startIndex, int numResults) ;
	
	
	public abstract int getJobCount(Company company, Classification classify);
	
	public abstract List<Job> getJobs(Company company, Classification classify) ;
	
	public abstract List<Job> getJobs(Company company, Classification classify, int startIndex, int numResults) ;	
	
	
	/**
	 * 인자로 주어진 Job 에 해당하는 역량 또는 능력단위를 리턴한다.
	 * @param job
	 * @return
	 */
	public abstract List<Competency> getJobCompetencies(Job job);
		
	public abstract void saveOrUpdate(Job job) throws JobNotFoundException ;
	
	public abstract Job getJob(long jobId) throws JobNotFoundException;
	
	public void batchUpdate(List<Job> jobs) ;
	
	
	
}
