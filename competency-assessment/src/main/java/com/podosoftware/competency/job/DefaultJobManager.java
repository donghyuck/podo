package com.podosoftware.competency.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.competency.CompetencyNotFoundException;
import com.podosoftware.competency.job.dao.JobDao;

import architecture.common.user.Company;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultJobManager implements JobManager {

    private Log log = LogFactory.getLog(getClass());
    private Cache jobIdsCache;

    private Cache jobCache;

    private JobDao jobDao;

    private CodeSetManager codeSetManager;

    private CompetencyManager competencyManager;

    public DefaultJobManager() {

    }

    public Cache getJobIdsCache() {
	return jobIdsCache;
    }

    public void setJobIdsCache(Cache jobIdsCache) {
	this.jobIdsCache = jobIdsCache;
    }

    public CompetencyManager getCompetencyManager() {
	return competencyManager;
    }

    public void setCompetencyManager(CompetencyManager competencyManager) {
	this.competencyManager = competencyManager;
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
	for (Long id : ids) {
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
	for (Long id : ids) {
	    try {
		list.add(getJob(id));
	    } catch (JobNotFoundException e) {

	    }
	}
	return list;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(Job job) throws JobNotFoundException {
	if (job.getClassification().getClassifyType() <= 0 || job.getClassification().getClassifiedMajorityId() <= 0
		|| job.getClassification().getClassifiedMajorityId() <= 0)
	    throw new IllegalArgumentException("Classification can not be null.");
	boolean isNew = true;
	Date now = new Date();
	if (job.getJobId() > 0) {
	    job.setModifiedDate(now);
	    isNew = false;
	} else {
	    job.setCreationDate(now);
	    job.setModifiedDate(now);
	}

	List<JobLevel> jobLevelUpdates = new ArrayList<JobLevel>();
	List<JobLevel> jobLevelDeletes = new ArrayList<JobLevel>();

	if (job.getJobLevels().size() > 0) {
	    for (JobLevel jobLevel : job.getJobLevels()) {
		if (jobLevel.getJobId() < 1 || jobLevel.getJobId() != job.getJobId()) {
		    jobLevel.setJobId(job.getJobId());
		}
		jobLevelUpdates.add(jobLevel);
	    }
	}

	if (!isNew) {
	    List<JobLevel> dbJobLevels = new ArrayList<JobLevel>();
	    try {
		Job dbJob = getJob(job.getJobId());
		for (JobLevel dbJobLevel : dbJob.getJobLevels()) {
		    if (!jobLevelUpdates.contains(dbJobLevel))
			jobLevelDeletes.add(dbJobLevel);
		}
	    } catch (JobNotFoundException e1) {
	    }
	}

	jobDao.saveOrUpdateJob(job);

	if (jobLevelUpdates.size() > 0) {
	    jobDao.saveOrUpdateJobLevels(jobLevelUpdates);
	}
	if (jobLevelDeletes.size() > 0)
	    jobDao.removeJobLevels(jobLevelDeletes);

	clearCache(job);
    }

    public Job getJob(long jobId) throws JobNotFoundException {
	Job job = getJobInCache(jobId);

	log.debug("job in cache : " + job);

	if (job == null) {
	    job = jobDao.getJobById(jobId);
	    if (job == null) {
		throw new JobNotFoundException();
	    }
	    job.setJobLevels(getJobLevels(job.getJobId()));
	    fetchClassification(job);
	    updateCache(job);
	}
	return job;
    }

    public List<JobLevel> getJobLevels(long jobId) {
	return loadJobLevels(jobDao.getJobLevelIds(jobId));
    }

    private List<JobLevel> loadJobLevels(List<Long> ids) {
	ArrayList<JobLevel> list = new ArrayList<JobLevel>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getJobLevel(id));
	    } catch (JobLevelNotFoundException e) {
	    }
	}
	return list;
    }

    public JobLevel getJobLevel(long jobLevelId) throws JobLevelNotFoundException {
	JobLevel jobLevel = null; // =
				  // getAssessmentJobSelectionInCache(jobSelectionId);
	if (jobLevel == null) {
	    jobLevel = jobDao.getJobLevelById(jobLevelId);
	    // updateCache(selection);
	}
	if (jobLevel == null) {
	    throw new JobLevelNotFoundException();
	}
	return jobLevel;
    }

    private void fetchClassification(Job job) {

	log.debug("fetch job classify " + job.getClassification());

	Classification classification = job.getClassification();
	try {
	    CodeSet codeset = getCodeSetManager().getCodeSet(classification.getClassifyType());
	    classification.setClassifyTypeName(codeset.getName());
	} catch (CodeSetNotFoundException e) {
	}
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

    public int getJobCount(Company company) {
	return jobDao.getJobCount(company);
    }

    private Job getJobInCache(Long jobId) {
	if (jobCache.get(jobId) == null)
	    return null;

	return (Job) jobCache.get(jobId).getValue();
    }

    private void updateCache(Job job) {
	if (jobCache.get(job.getJobId()) != null) {
	    jobCache.remove(job.getJobId());
	}
	jobCache.put(new Element(job.getJobId(), job));
    }

    private void clearCache(Job job) {
	if (jobCache.get(job.getJobId()) != null) {
	    jobCache.remove(job.getJobId());
	}
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void batchUpdate(List<Job> jobs) {
	List<Job> list = new ArrayList<Job>();
	for (Job job : jobs) {
	    if (job.getJobId() < 1)
		job.setJobId(jobDao.nextJobId());
	}
	jobDao.batchInsertJob(jobs);
    }

    @Override
    public int getJobCount(Company company, Classification classify) {
	return jobDao.getJobCount(company, classify);
    }

    @Override
    public List<Job> getJobs(Company company, Classification classify) {
	List<Long> ids = jobDao.getJobIds(company, classify);
	ArrayList<Job> list = new ArrayList<Job>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getJob(id));
	    } catch (JobNotFoundException e) {

	    }
	}
	return list;
    }

    @Override
    public List<Job> getJobs(Company company, Classification classify, int startIndex, int numResults) {
	List<Long> ids = jobDao.getJobIds(company, classify, startIndex, numResults);
	ArrayList<Job> list = new ArrayList<Job>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getJob(id));
	    } catch (JobNotFoundException e) {

	    }
	}
	return list;
    }

    @Override
    public int getJobCount(int objectType, long objectId, Classification classify) {
	return jobDao.getJobCount(objectType, objectId, classify);
    }

    @Override
    public List<Job> getJobs(int objectType, long objectId, Classification classify) {
	List<Long> ids = jobDao.getJobIds(objectType, objectId, classify);
	ArrayList<Job> list = new ArrayList<Job>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getJob(id));
	    } catch (JobNotFoundException e) {

	    }
	}
	return list;
    }

    public List<Job> getJobs(int objectType, long objectId, Classification classify, int startIndex, int numResults) {
	List<Long> ids = jobDao.getJobIds(objectType, objectId, classify, startIndex, numResults);
	ArrayList<Job> list = new ArrayList<Job>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getJob(id));
	    } catch (JobNotFoundException e) {

	    }
	}
	return list;
    }

    @Override
    public List<Competency> getJobCompetencies(Job job) {
	List<Long> ids = jobDao.getJobCompetencyIds(job);
	ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
	for (Long competencyId : ids) {
	    try {
		list.add(competencyManager.getCompetency(competencyId));
	    } catch (CompetencyNotFoundException e) {
	    }
	}
	return list;
    }

    @Override
    public Job getJob(Competency competency) throws JobNotFoundException {
	long jobId = -1L;
	if (this.jobIdsCache.get(competency.getCompetencyId()) != null) {
	    jobId = (Long) this.jobIdsCache.get(competency.getCompetencyId()).getObjectValue();
	} else {
	    jobId = jobDao.getJobIdByCompetency(competency);
	    jobIdsCache.put(new Element(competency.getCompetencyId(), jobId));
	}

	log.debug("get job by " + jobId);

	if (jobId < 1)
	    throw new JobNotFoundException();

	return getJob(jobId);
    }
}
