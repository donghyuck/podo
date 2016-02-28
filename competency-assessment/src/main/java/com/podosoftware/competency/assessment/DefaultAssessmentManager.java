package com.podosoftware.competency.assessment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.assessment.dao.AssessmentDao;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobLevel;
import com.podosoftware.competency.job.JobManager;
import com.podosoftware.competency.job.JobNotFoundException;

import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultAssessmentManager implements AssessmentManager {

	private Log log = LogFactory.getLog(getClass());
	
	private AssessmentDao assessmentDao;
	
	private JobManager jobManager ;
	
	private CodeSetManager codeSetManager;
	
	private CompanyManager companyManager;
	
	private UserManager userManager;
	
	protected Cache ratingSchemeCache;
	
	protected Cache assessmentCache;
	
	protected Cache assessmentSchemeCache;
	
	protected Cache assessmentPlanCache;
	
	protected Cache assessmentJobSelectionCache;
	
	protected Cache assessmentSubjectCache;
	
	
	public DefaultAssessmentManager() {
		
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public JobManager getJobManager() {
		return jobManager;
	}

	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	public CodeSetManager getCodeSetManager() {
		return codeSetManager;
	}

	public void setCodeSetManager(CodeSetManager codeSetManager) {
		this.codeSetManager = codeSetManager;
	}

	public AssessmentDao getAssessmentDao() {
		return assessmentDao;
	}

	public void setAssessmentDao(AssessmentDao accessmentDao) {
		this.assessmentDao = accessmentDao;
	}
	
	public Cache getAssessmentSchemeCache() {
		return assessmentSchemeCache;
	}

	public void setAssessmentSchemeCache(Cache assessmentSchemeCache) {
		this.assessmentSchemeCache = assessmentSchemeCache;
	}

	public Cache getAssessmentPlanCache() {
		return assessmentPlanCache;
	}

	public void setAssessmentPlanCache(Cache assessmentCache) {
		this.assessmentPlanCache = assessmentCache;
	}

	public Cache getRatingSchemeCache() {
		return ratingSchemeCache;
	}

	public void setRatingSchemeCache(Cache ratingSchemeCache) {
		this.ratingSchemeCache = ratingSchemeCache;
	}
 
	public Cache getAssessmentJobSelectionCache() {
		return assessmentJobSelectionCache;
	}

	public Cache getAssessmentCache() {
		return assessmentCache;
	}

	public void setAssessmentCache(Cache assessmentCache) {
		this.assessmentCache = assessmentCache;
	}

	public void setAssessmentJobSelectionCache(Cache assessmentJobSelectionCache) {
		this.assessmentJobSelectionCache = assessmentJobSelectionCache;
	}

	public List<RatingScheme> getRatingSchemes(int objectType, long objectId) {
		List<Long> ids = assessmentDao.getRatingSchemeIds(objectType, objectId);		
		return loadRatingSchemes(ids);
	}
 
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateRatingScheme(RatingScheme ratingScheme) {
		if(ratingScheme.getRatingSchemeId() > 0 ){
			if( ratingSchemeCache.get(ratingScheme.getRatingSchemeId()) != null ){
				ratingSchemeCache.remove(ratingScheme.getRatingSchemeId());
			}
		}
		assessmentDao.saveOrUpdateRatingScheme(ratingScheme);
	}

	/**
	 * 
	 */
	public RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException {
		RatingScheme scheme = getRatingSchemeInCache(ratingSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getRatingSchemeById(ratingSchemeId);			
			if( scheme == null ){				
				throw new RatingSchemeNotFoundException();
			}
			updateCache(scheme);
		}
		return scheme;	
	}
	 
	public int getRatingSchemeCount(int objectType, long objectId) {
		return assessmentDao.getRatingSchemeCount(objectType, objectId);
	}
	
	private void updateCache( RatingScheme ratingScheme){
		if( ratingSchemeCache.get(ratingScheme.getRatingSchemeId()) != null ){
			ratingSchemeCache.remove(ratingScheme.getRatingSchemeId());
		}
		ratingSchemeCache.put(new Element(ratingScheme.getRatingSchemeId(), ratingScheme));
	}
	
	private RatingScheme getRatingSchemeInCache(long ratingSchemeId){
		if(ratingSchemeCache.get(ratingSchemeId)!=null){
			return (RatingScheme) ratingSchemeCache.get(ratingSchemeId).getValue();
		}
		return null;
	}
	
	private List<RatingScheme> loadRatingSchemes(List<Long> ids){
		ArrayList<RatingScheme> list = new ArrayList<RatingScheme>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getRatingScheme(id));
			} catch (RatingSchemeNotFoundException e) {}			
		}		
		return list;		
	}

	@Override
	public List<AssessmentScheme> getAssessmentSchemes(int objectType, long objectId) {
		List<Long> ids = assessmentDao.getAssessmentSchemeIds(objectType, objectId);		
		return loadAssessmentSchemes(ids);
	}

	@Override
	public int getAssessmentSchemeCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentSchemeCount(objectType, objectId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme) {
		
		AssessmentScheme dbAssessmentScheme = null;
		
		
		try {
			dbAssessmentScheme = getAssessmentScheme(assessmentScheme.getAssessmentSchemeId());
		} catch (AssessmentSchemeNotFoundException e1) {
		}
		List<JobSelection> jobSelections = assessmentScheme.getJobSelections();
		List<JobSelection> jobDeletes = new ArrayList<JobSelection>();
		List<JobSelection> dbJobSelections  = dbAssessmentScheme.getJobSelections();
		for( JobSelection jobSelection : dbJobSelections){
			if( !jobSelections.contains(jobSelection))
				jobDeletes.add(jobSelection);
		}
		List<Subject> subjects = assessmentScheme.getSubjects();
		List<Subject> subjectDeletes = new ArrayList<Subject>();
		List<Subject> dbSubjects  = dbAssessmentScheme.getSubjects();
		for( Subject sbj : dbSubjects){
			if( !subjects.contains(sbj))
				subjectDeletes.add(sbj);
		}
		assessmentDao.saveOrUpdateAssessmentScheme(assessmentScheme);
		if( jobSelections.size() > 0)
			assessmentDao.saveOrUpdateAssessmentJobSelections(jobSelections);
		if( jobDeletes.size() > 0)
			assessmentDao.removeAssessmentJobSelections(jobDeletes);
		if( subjects.size() > 0)
			assessmentDao.saveOrUpdateAssessmentSubjects(subjects);
		if( subjectDeletes.size() > 0)
			assessmentDao.removeAssessmentSubjects(subjectDeletes);	
		
		if(assessmentScheme.getAssessmentSchemeId() > 0 ){
			if( assessmentSchemeCache.get(assessmentScheme.getAssessmentSchemeId()) != null ){
				assessmentSchemeCache.remove(assessmentScheme.getAssessmentSchemeId());
			}
		}
	}

	
	/**
	 * 
	 */
	public AssessmentScheme getAssessmentScheme(long assessmentSchemeId) throws AssessmentSchemeNotFoundException {
		AssessmentScheme scheme = getAssessmentSchemeInCache(assessmentSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getAssessmentSchemeById(assessmentSchemeId);		
			RatingScheme ratingScheme;
			try {
				ratingScheme = getRatingScheme(scheme.getRatingScheme().getRatingSchemeId());
				scheme.setRatingScheme(ratingScheme);
			} catch (RatingSchemeNotFoundException e) {
			}
			
			List<JobSelection> selections = getJobSelections(scheme.getModelObjectType(), scheme.getAssessmentSchemeId());
			scheme.setJobSelections(selections);			
			if( scheme == null ){				
				throw new AssessmentSchemeNotFoundException();
			}
			List<Subject> subjects = getSubjects(scheme.getModelObjectType(), scheme.getAssessmentSchemeId());
			scheme.setSubjects(subjects);			
			updateCache(scheme);
		}
		return scheme;	
	}
	
	public JobSelection getJobSelection(long jobSelectionId) throws JobSelectionNotFoundException {
		JobSelection selection = null; // = getAssessmentJobSelectionInCache(jobSelectionId);
		if(selection == null){
			selection = assessmentDao.getAssessmentJobSelectionById(jobSelectionId);
			//updateCache(selection);
		}
		if( selection == null ){				
			throw new JobSelectionNotFoundException();
		}
		// cache problem ... 
		log.debug(  "name is not set" + StringUtils.isEmpty(selection.getClassifiedMajorityName())  );
		if(selection.getClassifyType() > 0 && StringUtils.isEmpty(selection.getClassifyTypeName()) ){
			try {
				selection.setClassifyTypeName(codeSetManager.getCodeSet(selection.getClassifyType()).getName());
			} catch (CodeSetNotFoundException e) {
			}
		}		
		
		if(selection.getClassifiedMajorityId() > 0 && StringUtils.isEmpty(selection.getClassifiedMajorityName()) ){
			try {
				selection.setClassifiedMajorityName(codeSetManager.getCodeSet(selection.getClassifiedMajorityId()).getName());
			} catch (CodeSetNotFoundException e) {
			}
		}
		if(selection.getClassifiedMiddleId() > 0 && StringUtils.isEmpty(selection.getClassifiedMiddleName()) ){
			try {
				selection.setClassifiedMiddleName(codeSetManager.getCodeSet(selection.getClassifiedMiddleId()).getName());
			} catch (CodeSetNotFoundException e) {
			}				
		}
		if(selection.getClassifiedMinorityId() > 0 && StringUtils.isEmpty(selection.getClassifiedMinorityName()) ){
			try {
				selection.setClassifiedMinorityName(codeSetManager.getCodeSet(selection.getClassifiedMinorityId()).getName());
			} catch (CodeSetNotFoundException e) {
			}
		}
		if(selection.getJobId() > 0 && StringUtils.isEmpty(selection.getJobName()) ){
			try {
				selection.setJobName(jobManager.getJob(selection.getJobId()).getName());
			} catch (JobNotFoundException e) {
			}
		}
			
		log.debug(selection);	
		return selection;	
	}

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateJobSelections(List<JobSelection> jobSelections) {
		assessmentDao.saveOrUpdateAssessmentJobSelections(jobSelections);
		for(JobSelection selection : jobSelections){
			if(assessmentJobSelectionCache.get(selection.getSelectionId())!=null)
				assessmentJobSelectionCache.remove(selection.getSelectionId());
		}
	}
 
	public List<JobSelection> getJobSelections(int objectType, long objectId) {
		return loadJobSelections(assessmentDao.getAssessmentJobSelectionIds(objectType, objectId));
	}
 
	public int getJobSelectionCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentJobSelectionCount(objectType, objectId);
	}


	private void updateCache( JobSelection jobSelection){
		if( assessmentJobSelectionCache.get(jobSelection.getSelectionId()) != null ){
			assessmentJobSelectionCache.remove(jobSelection.getSelectionId());
		}
		assessmentJobSelectionCache.put(new Element(jobSelection.getSelectionId(), jobSelection));
	}
	
	private JobSelection getAssessmentJobSelectionInCache(long jobSelectionId){
		if(assessmentJobSelectionCache.get(jobSelectionId)!=null){
			return (JobSelection) assessmentJobSelectionCache.get(jobSelectionId).getValue();
		}
		return null;
	}
	
	

	public Subject getSubject(long subjectId) throws SubjectNotFoundException {
		Subject sbj = null; // = getAssessmentJobSelectionInCache(jobSelectionId);
		if(sbj == null){
			sbj = assessmentDao.getAssessmentSubjectById(subjectId);
			//updateCache(selection);
		}
		if( sbj == null ){				
			throw new SubjectNotFoundException();
		}
		// cache problem ... 
		
		if(sbj.getSubjectObjectType() == 1 && sbj.getSubjectObjectId() > 0  && StringUtils.isEmpty(sbj.getSubjectObjectName()) ){
			try {
				sbj.setSubjectObjectName(companyManager.getCompany(sbj.getSubjectObjectId()).getDisplayName() ) ;
			} catch (CompanyNotFoundException e) {
			}
		}		
		
		log.debug(sbj);	
		return sbj;	
	}
	
	public void saveOrUpdateSubjects(List<Subject> subjects) {
		assessmentDao.saveOrUpdateAssessmentSubjects(subjects);
		for(Subject sbj : subjects){
			if(assessmentSubjectCache.get(sbj.getSubjectId())!=null)
				assessmentSubjectCache.remove(sbj.getSubjectId());
		}
	}

	@Override
	public List<Subject> getSubjects(int objectType, long objectId) {
		return loadSubjects(assessmentDao.getAssessmentSubjectIds(objectType, objectId));
	}
 
	public int getSubjectCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentSubjectCount(objectType, objectId);
	}
	
	
	
	
 
	public List<AssessmentPlan> getAssessmentPlans(int objectType, long objectId) {
		List<Long> ids = assessmentDao.getAssessmentPlanIds(objectType, objectId);		
		return loadAssessmentPlans(ids);
	}

 
	public int getAssessmentPlanCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentPlanCount(objectType, objectId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateAssessmentPlan(AssessmentPlan assessment) {	
		
		boolean isNew = true;
		if( assessment.getAssessmentId() > 0){
			isNew = false;
		}
		
		assessmentDao.saveOrUpdateAssessmentPlan(assessment);
		
		AssessmentPlan dbAssessment = new DefaultAssessmentPlan();	
		if( !isNew ){
			try {
				dbAssessment = getAssessmentPlan(assessment.getAssessmentId());
			} catch (AssessmentPlanNotFoundException e1) {}
		}
		
		List<JobSelection> jobSelections = assessment.getJobSelections();
		List<Subject> subjects = assessment.getSubjects();
		
		if(isNew){
			jobSelections = new ArrayList<JobSelection>();
			subjects = new ArrayList<Subject>();
			for(JobSelection js : assessment.getJobSelections() ){
				jobSelections.add(
					new JobSelection(
						-1L, 
						assessment.getModelObjectType(), 
						assessment.getAssessmentId(), 
						js.getClassifyType(), 
						js.getClassifiedMajorityId(), 
						js.getClassifiedMiddleId(), 
						js.getClassifiedMinorityId(), 
						js.getJobId()));
			}
			for(Subject sbj : assessment.getSubjects() ){
				subjects.add(new Subject(
						-1L, 
						assessment.getModelObjectType(),
						assessment.getAssessmentId(),
						sbj.getSubjectObjectType(),
						sbj.getSubjectObjectId()
				));
			}
		}else{
			jobSelections = assessment.getJobSelections();
			subjects = assessment.getSubjects();			
		}
		
		if(!isNew){
			List<JobSelection> jobDeletes = new ArrayList<JobSelection>();
			List<Subject> subjectDeletes = new ArrayList<Subject>();
			List<JobSelection> dbJobSelections  = dbAssessment.getJobSelections();
			List<Subject> dbSubjects  = dbAssessment.getSubjects();	
			for( JobSelection jobSelection : dbJobSelections){
				if( !jobSelections.contains(jobSelection))
					jobDeletes.add(jobSelection);
			}			
			for( Subject sbj : dbSubjects){
				if( !subjects.contains(sbj))
					subjectDeletes.add(sbj);
			}	
			assessmentDao.removeAssessmentJobSelections(jobDeletes);
			assessmentDao.removeAssessmentSubjects(subjectDeletes);
		}
		
		if( jobSelections.size() > 0){
			assessmentDao.saveOrUpdateAssessmentJobSelections(jobSelections);
		}
		if( subjects.size() > 0){
			assessmentDao.saveOrUpdateAssessmentSubjects(subjects);
		}
		if( !isNew && assessment.getAssessmentId() > 0 ){
			if( assessmentPlanCache.get(assessment.getAssessmentId()) != null ){
				assessmentPlanCache.remove(assessment.getAssessmentId());
			}
		}
	}
 
	public AssessmentPlan getAssessmentPlan(long assessmentSchemeId) throws AssessmentPlanNotFoundException {
		AssessmentPlan scheme = getAssessmentPlanInCache(assessmentSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getAssessmentPlanById(assessmentSchemeId);		
			RatingScheme ratingScheme;
			try {
				ratingScheme = getRatingScheme(scheme.getRatingScheme().getRatingSchemeId());
				scheme.setRatingScheme(ratingScheme);
			} catch (RatingSchemeNotFoundException e) {
			}
			
			List<JobSelection> selections = getJobSelections(scheme.getModelObjectType(), scheme.getAssessmentId());
			scheme.setJobSelections(selections);			
			if( scheme == null ){				
				throw new AssessmentPlanNotFoundException();
			}
			List<Subject> subjects = getSubjects(scheme.getModelObjectType(), scheme.getAssessmentId());
			scheme.setSubjects(subjects);
			updateCache(scheme);
		}
		return scheme;	
	}

	public List<AssessmentPlan> getUserAssessmentPlans(User user) {
		List<Long> ids = assessmentDao.getAssessmentPlanIdsByUser(user);		
		return loadAssessmentPlans(ids);
	}	
	
	public AssessmentStats getAssessmentStats(AssessmentPlan assessmentPlan, User user) {
		AssessmentStats stats = new AssessmentStats();
		stats.setAssessmentPlan(assessmentPlan);			
		int userAssessedCount = 0;				
		int userIncompleteCount = 0 ;
		List<Assessment> assessments = loadAssessments(assessmentDao.getUserAssessmentIds(user, assessmentPlan.getAssessmentId(), null));
		for( Assessment assessment : assessments ){
			if(assessment.getState() == Assessment.State.ASSESSED ){
				userAssessedCount ++;
			}else{
				userIncompleteCount ++;
			}
			if(assessment.getJob() !=null && assessment.getJob().getJobId() > 0){
				try {
					Job job = jobManager.getJob(assessment.getJob().getJobId());
					
					for( JobLevel jobLevel : job.getJobLevels() ) {
						if( jobLevel.getLevel() == assessment.getJobLevel() )
						{
							assessment.setJobLevelName(jobLevel.getName());
							break;
						}
					}
					assessment.setJob(job);
				} catch (JobNotFoundException e) {
				}
			}
		}
		stats.setUserAssessedCount(userAssessedCount);
		stats.setUserIncompleteCount(userIncompleteCount);
		stats.setUserAssessments(assessments);
		return stats;
	}
	
	public Assessment getAssessment(long assessmentId) throws AssessmentNotFoundException, AssessmentPlanNotFoundException {
		
		Assessment assessment = getAssessmentById(assessmentId);
		assessment.setAssessmentPlan( getAssessmentPlan(assessment.getAssessmentPlan().getAssessmentId()));
		
		if(assessment.getAssessors() != null && assessment.getAssessors().size() > 0){
			List<User> assessors = new ArrayList<User>(assessment.getAssessors().size());
			for(User assessor : assessors){
				try {
					assessors.add( userManager.getUser(assessor.getUserId()));
				} catch (UserNotFoundException e) {
					assessors.add(assessor);
				}
			}
			assessment.setAssessors(assessors);
		}
		
		if(assessment.getCandidate() != null && assessment.getCandidate().getUserId() > 0 )
		{
			User candidate;
			try {
				candidate = userManager.getUser(assessment.getCandidate().getUserId());
				assessment.setCandidate(candidate);
			} catch (UserNotFoundException e) {
				log.warn(e);
			}
			
		}
		if(assessment.getJob() !=null && assessment.getJob().getJobId() > 0){
			try {
				Job job = jobManager.getJob(assessment.getJob().getJobId());
				assessment.setJob(job);
				for( JobLevel jobLevel : job.getJobLevels() ) {
					if( jobLevel.getLevel() == assessment.getJobLevel() )
					{
						assessment.setJobLevelId(jobLevel.getJobLevelId());
						assessment.setJobLevelName(jobLevel.getName());
						break;
					}
				}				
			} catch (JobNotFoundException e) {
				log.warn(e);
			}
		}
		return assessment; 
	}
	
	public Assessment getAssessmentById(long assessmentId) throws AssessmentNotFoundException {
		Assessment assessment = getAccessmentInCache(assessmentId);
		if(assessment == null){
			assessment = assessmentDao.getAssessmentById(assessmentId);		
			/**
			AssessmentPlan plan;
			if( assessment.getAssessmentPlan().getAssessmentId() > 0){
				try {
					plan = getAssessmentPlan(assessment.getAssessmentPlan().getAssessmentId());
					assessment.setAssessmentPlan(plan);
				} catch (AssessmentPlanNotFoundException e) {
				}
			}			
			if( assessment.getJob().getJobId() > 0 ){				
				try {
					Job job = jobManager.getJob(assessment.getJob().getJobId());
					assessment.setJob(job);
				} catch (JobNotFoundException e) {
					e.printStackTrace();
				}
			};
			*/
//			if( assessment.getJob().getJobId() > 0 && assessment.getJobLevel() > 0 ){				
//				try {
//					Job job = jobManager.getJob(assessment.getJob().getJobId());
//					assessment.getJob().setName(job.getName());
//					for( JobLevel jobLevel : job.getJobLevels() ) {
//						if( jobLevel.getLevel() == assessment.getJobLevel() )
//						{
//							assessment.setJobLevelName(jobLevel.getName());
//							break;
//						}
//					}
//				} catch (JobNotFoundException e) {
//					e.printStackTrace();
//				}
//			};
			
			updateCache(assessment);
		}
		return assessment;	
	}
	

	/**
	 * 진단계획에 따른 평가 수를 리턴한다. 
	 * 
	 */
	public int getUserAssessmentCount(User candidate, AssessmentPlan assessment, String state) {
		return assessmentDao.getUserAssessmentCount( candidate, assessment.getAssessmentId(), state);
	}
	
	public List<Assessment> getUserAssessments(User candidate, AssessmentPlan assessment, String state) {
		return loadAssessments(assessmentDao.getUserAssessmentIds(candidate, assessment.getAssessmentId(), state ));
	}


	@Override
	public boolean hasUserAssessed(AssessmentPlan assessmentPlan, User candidate) {
		if( getUserAssessmentCount(candidate, assessmentPlan, null )>0)
			return true;
		
		return false;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addAssessmentCandidate(AssessmentPlan assessment, User candidate, Job job, int level) {		
		DefaultAssessment newAssessment = new DefaultAssessment();	
		newAssessment.setAssessmentPlan(assessment);
		newAssessment.setCandidate(candidate);
		newAssessment.setJob(job);
		newAssessment.setJobLevel(level);
		saveOrUpdateUserAssessment(newAssessment);
	}
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateUserAssessment(Assessment assessment) {	
		assessmentDao.saveOrUpdateAssessment(assessment);
	}
	

	
	private Assessment getAccessmentInCache(long assessmentId){
		if(assessmentCache.get(assessmentId)!=null){
			return (Assessment) assessmentCache.get(assessmentId).getObjectValue();
		}
		return null;
	}

	private List<Assessment> loadAssessments(List<Long> ids){
		ArrayList<Assessment> list = new ArrayList<Assessment>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getAssessmentById(id));
			} catch (AssessmentNotFoundException e) {}			
		}		
		return list;		
	}
	
	private List<JobSelection> loadJobSelections(List<Long> ids){
		ArrayList<JobSelection> list = new ArrayList<JobSelection>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getJobSelection(id));
			} catch (JobSelectionNotFoundException e) {}			
		}		
		return list;		
	}
	
	private List<AssessmentPlan> loadAssessmentPlans(List<Long> ids){
		ArrayList<AssessmentPlan> list = new ArrayList<AssessmentPlan>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getAssessmentPlan(id));
			} catch (AssessmentPlanNotFoundException e) {}			
		}		
		return list;		
	}	
	
	private List<Subject> loadSubjects(List<Long> ids){
		ArrayList<Subject> list = new ArrayList<Subject>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getSubject(id));
			} catch (SubjectNotFoundException e) {}			
		}		
		return list;		
	}
	
	private void updateCache( Subject jobSelection){
		if( assessmentSubjectCache.get(jobSelection.getSubjectId()) != null ){
			assessmentSubjectCache.remove(jobSelection.getSubjectId());
		}
		assessmentSubjectCache.put(new Element(jobSelection.getSubjectId(), jobSelection));
	}
	
	private Subject getAccessmentSubjectInCache(long jobSelectionId){
		if(assessmentSubjectCache.get(jobSelectionId)!=null){
			return (Subject) assessmentSubjectCache.get(jobSelectionId).getObjectValue();
		}
		return null;
	}

	private void updateCache( Assessment assessment){
		if( assessmentCache.get(assessment.getAssessmentId()) != null ){
			assessmentCache.remove(assessment.getAssessmentId());
		}
		assessmentCache.put(new Element(assessment.getAssessmentId(), assessment));
	}
	
	private List<AssessmentScheme> loadAssessmentSchemes(List<Long> ids){
		ArrayList<AssessmentScheme> list = new ArrayList<AssessmentScheme>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getAssessmentScheme(id));
			} catch (AssessmentSchemeNotFoundException e) {}			
		}		
		return list;		
	}
	
	private void updateCache( AssessmentScheme assessmentScheme){
		if( assessmentSchemeCache.get(assessmentScheme.getAssessmentSchemeId()) != null ){
			assessmentSchemeCache.remove(assessmentScheme.getAssessmentSchemeId());
		}
		assessmentSchemeCache.put(new Element(assessmentScheme.getAssessmentSchemeId(), assessmentScheme));
	}
	
	private AssessmentScheme getAssessmentSchemeInCache(long assessmentSchemeId){
		if(assessmentSchemeCache.get(assessmentSchemeId)!=null){
			return (AssessmentScheme) assessmentSchemeCache.get(assessmentSchemeId).getObjectValue();
		}
		return null;
	}
	
	
	private void updateCache( AssessmentPlan assessment){
		if( assessmentPlanCache.get(assessment.getAssessmentId()) != null ){
			assessmentPlanCache.remove(assessment.getAssessmentId());
		}
		assessmentPlanCache.put(new Element(assessment.getAssessmentId(), assessment));
	}
	
	private AssessmentPlan getAssessmentPlanInCache(long assessmentId){
		if(assessmentPlanCache.get(assessmentId)!=null){
			return (AssessmentPlan) assessmentPlanCache.get(assessmentId).getObjectValue();
		}
		return null;
	}

	@Override
	public List<AssessmentQuestion> getUserAssessmentQuestions(Assessment assessment) {	
		
		boolean isStrong = false;
		
		for(JobLevel jobLevel : assessment.getJob().getJobLevels() )
		{
			if( jobLevel.getJobLevelId() == assessment.getJobLevelId()){
				isStrong = jobLevel.isStrong();
				break;
			}
		}
		
		List<AssessmentQuestion> list ;
		if( isStrong )
			list = assessmentDao.getAssessmentQuestionByJobAndJobLevel(assessment.getJob().getJobId(), assessment.getJobLevel());	
		else
			list = assessmentDao.getAssessmentQuestionByJob(assessment.getJob().getJobId(), assessment.getJobLevel());
		int no = 0 ;
		
		for(AssessmentQuestion q : list){
			no ++ ;
			q.setSeq(no);
			q.setAssessmentId(assessment.getAssessmentId());
			q.setCandidateId(assessment.getCandidate().getUserId());
		}
		return list;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateUserAssessmentScores(Assessment assessment, User assessor, List<AssessmentQuestion> answers) {		
		if( answers.size() > 0){
			assessmentDao.removeAssessmentScores(assessment, assessor);
			assessmentDao.saveOrUpdateAssessmentScores(answers);
			assessmentDao.updateAssessmentScoreAndState(assessment);
			if( assessmentCache.get(assessment.getAssessmentId()) != null ){
				assessmentCache.remove(assessment.getAssessmentId());
			}
		}
	}

	@Override
	public List<AssessedEssentialElementScore> getUserAssessedSummaries(Assessment assessment) {
		
		Map<Long, AssessedEssentialElementScoreItem> averages = listToMap(
			assessmentDao.getAssessedEssentialElementScoreAverageByPlanAndJob(
			assessment.getAssessmentPlan().getAssessmentId(), 
			assessment.getJob().getJobId(), 
			assessment.getJobLevel())
		);
		
		List<AssessedEssentialElementScore> list = assessmentDao.getAssessedEssentialElementSummaries(assessment.getAssessmentId());
		
		for(AssessedEssentialElementScore summary : list){
			summary.setFinalScore( computeAssessedFinalScore(summary.getTotalScore(), summary.getTotalCount()) );			
			AssessedEssentialElementScoreItem average = averages.get(summary.getEssentialElementId());
			if(average!=null)
				summary.setOthersAverageScore(computeAssessedFinalScore(average.getTotalScore(), average.getTotalCount()));
		}
		
		return list;
	}
	
	private double computeAssessedFinalScore( int totalScore, int totalCount ){
		BigDecimal preNum = new BigDecimal(totalScore);
		BigDecimal postNum = new BigDecimal(totalCount);		
		BigDecimal divideResult = preNum.divide(postNum, 2, BigDecimal.ROUND_UP);
		return divideResult.doubleValue() ;
	}
	
	private Map<Long, AssessedEssentialElementScoreItem> listToMap(List<AssessedEssentialElementScoreItem> list){
		Map<Long, AssessedEssentialElementScoreItem> map = new HashMap<Long, AssessedEssentialElementScoreItem>();
		for(AssessedEssentialElementScoreItem score : list){
			map.put(score.getEssentialElementId(), score);
		}
		return map;
	}
	
	
}
