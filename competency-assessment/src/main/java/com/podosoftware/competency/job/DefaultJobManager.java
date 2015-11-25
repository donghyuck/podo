package com.podosoftware.competency.job;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.job.dao.JobDao;

import architecture.common.user.Company;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultJobManager implements JobManager {
	
	private Cache jobCache;
	
	private JobDao jobDao;
	
	private CodeSetManager codeSetManager;
	

	public DefaultJobManager() {
		
	}
	
	

	public CodeSetManager getCodeSetManager() {
		return codeSetManager;
	}



	public void setCodeSetManager(CodeSetManager codeSetManager) {
		this.codeSetManager = codeSetManager;
	}



	public Cache getJobCache() {
		return jobCache;
	}



	public void setJobCache(Cache jobCache) {
		this.jobCache = jobCache;
	}



	public JobDao getJobDao() {
		return jobDao;
	}



	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

 
	public List<Job> getJobs(Company company) {
		List<Long> ids = jobDao.getJobIds(company);
		ArrayList<Job> list = new ArrayList<Job>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getJob(id));
			} catch (JobNotFoundException e) {
				
			}			
		}		
		return list;
	}
 
	public List<Job> getJobs(Company company, int startIndex, int numResults) {
		List<Long> ids = jobDao.getJobIds(company, startIndex, numResults);
		ArrayList<Job> list = new ArrayList<Job>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getJob(id));
			} catch (JobNotFoundException e) {
				
			}			
		}		
		return list;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(Job job) throws JobNotFoundException {
		jobDao.saveOrUpdateJob(job);
		clearCache( job );
	}
 
	public Job getJob(long jobId) throws JobNotFoundException {
		Job job = getJobInCache(jobId);
		if(job == null){
			job = jobDao.getJobById(jobId);
			if( job == null ){				
				throw new JobNotFoundException();
			}
			fetchClassification(job);
			updateCache(job);
		}
		return job;
	}	
	
	private void fetchClassification(Job job){
		Classification classification = job.getClassification();			
		try {
			CodeSet codeset = getCodeSetManager().getCodeSet(classification.getClassifiedMajorityId());
			classification.setClassifiedMajorityName(codeset.getName());
		} catch (CodeSetNotFoundException e) {
		}
		try {
			CodeSet codeset = getCodeSetManager().getCodeSet(classification.getClassifiedMiddleId());
			classification.setClassifiedMiddleName(codeset.getName());
		} catch (CodeSetNotFoundException e) {
		}
		try {
			CodeSet codeset = getCodeSetManager().getCodeSet(classification.getClassifiedMinorityId());
			classification.setClassifiedMinorityName(codeset.getName());
		} catch (CodeSetNotFoundException e) {
		}		
	}
	
	public int getJobCount(Company company){
		return jobDao.getJobCount(company);
	}

	private Job getJobInCache(Long jobId){
		if( jobCache.get(jobId) == null )
			return null;		
		return (Job)jobCache.get(jobId).getValue(); 
	}
	
	private void updateCache(Job job){
		if( jobCache.get(job.getJobId()) != null ){
			jobCache.remove(job.getJobId());
		}
		jobCache.put(new Element(job.getJobId(), job));
	}
	
	private void clearCache(Job job){
		if( jobCache.get(job.getJobId()) != null ){
			jobCache.remove(job.getJobId());
		}		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void batchUpdate(List<Job> jobs) {
		List<Job> list = new ArrayList<Job>();
		for(Job job : jobs){
			if( job.getJobId() < 1 )
				job.setJobId(jobDao.nextJobId());
		}
		jobDao.batchInsertJob(jobs);
	}
}
