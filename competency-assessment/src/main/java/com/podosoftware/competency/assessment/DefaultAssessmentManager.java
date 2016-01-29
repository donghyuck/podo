package com.podosoftware.competency.assessment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.assessment.dao.AssessmentDao;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobManager;
import com.podosoftware.competency.job.JobNotFoundException;

import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultAssessmentManager implements AssessmentManager {

	private Log log = LogFactory.getLog(getClass());
	private AssessmentDao assessmentDao;
	
	private JobManager jobManager ;
	
	private CodeSetManager codeSetManager;
	
	private CompanyManager companyManager;
	
	protected Cache ratingSchemeCache;
	
	protected Cache assessmentSchemeCache;
	
	protected Cache assessmentCache;
	
	protected Cache assessmentJobSelectionCache;
	
	protected Cache assessmentSubjectCache;
	
	
	public DefaultAssessmentManager() {
		
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

	public Cache getAssessmentCache() {
		return assessmentCache;
	}

	public void setAssessmentCache(Cache assessmentCache) {
		this.assessmentCache = assessmentCache;
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
		List<Long> ids = assessmentDao.getAssessmentIds(objectType, objectId);		
		return loadAssessments(ids);
	}

 
	public int getAssessmentPlanCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentCount(objectType, objectId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdateAssessmentPlan(AssessmentPlan assessment) {	
		
		boolean isNew = true;
		if( assessment.getAssessmentId() > 0){
			isNew = false;
		}
		
		assessmentDao.saveOrUpdateAssessment(assessment);
		
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
			if( assessmentCache.get(assessment.getAssessmentId()) != null ){
				assessmentCache.remove(assessment.getAssessmentId());
			}
		}
	}
 
	public AssessmentPlan getAssessmentPlan(long assessmentSchemeId) throws AssessmentPlanNotFoundException {
		AssessmentPlan scheme = getAssessmentInCache(assessmentSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getAssessmentById(assessmentSchemeId);		
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

	public List<AssessmentPlan> getUserAssessments(User user) {
		List<Long> ids = assessmentDao.getAssessmentIdsByUser(user);		
		return loadAssessments(ids);
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
	
	private List<AssessmentPlan> loadAssessments(List<Long> ids){
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
			return (Subject) assessmentSubjectCache.get(jobSelectionId).getValue();
		}
		return null;
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
			return (AssessmentScheme) assessmentSchemeCache.get(assessmentSchemeId).getValue();
		}
		return null;
	}
	
	
	private void updateCache( AssessmentPlan assessment){
		if( assessmentCache.get(assessment.getAssessmentId()) != null ){
			assessmentCache.remove(assessment.getAssessmentId());
		}
		assessmentCache.put(new Element(assessment.getAssessmentId(), assessment));
	}
	
	private AssessmentPlan getAssessmentInCache(long assessmentId){
		if(assessmentCache.get(assessmentId)!=null){
			return (AssessmentPlan) assessmentCache.get(assessmentId).getValue();
		}
		return null;
	}

	@Override
	public int getUserAssessmentResultCount(AssessmentPlan assessment, User candidate, String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<AssessmentResult> getUserAssessmentResults(AssessmentPlan assessment, User candidate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAssessmentCandidate(AssessmentPlan assessment, User candidate, Job job, int level) {
		// TODO Auto-generated method stub
		
	}

}
