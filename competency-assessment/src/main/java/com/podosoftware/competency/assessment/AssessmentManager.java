package com.podosoftware.competency.assessment;

import java.util.List;

import com.podosoftware.competency.job.Job;

import architecture.common.user.User;

public interface AssessmentManager {

	public abstract List<RatingScheme> getRatingSchemes(int objectType, long objectId);
	
	public abstract int getRatingSchemeCount(int objectType, long objectId);
	
	public abstract void saveOrUpdateRatingScheme(RatingScheme ragintScheme);
	
	public abstract RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException ;
	
			
	public abstract List<AssessmentScheme> getAssessmentSchemes(int objectType, long objectId);
	
	public abstract int getAssessmentSchemeCount(int objectType, long objectId);
	
	public abstract void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme);
	
	public abstract AssessmentScheme getAssessmentScheme(long assessmentSchemeId) throws AssessmentSchemeNotFoundException ;
	
	
	public abstract void saveOrUpdateJobSelections(List<JobSelection> jobSelectinos);
	
	public abstract List<JobSelection> getJobSelections(int objectType, long objectId);
	
	public abstract int getJobSelectionCount(int objectType, long objectId);
	
	
	public abstract void saveOrUpdateSubjects(List<Subject> subjects);
	
	public abstract List<Subject> getSubjects(int objectType, long objectId);
	
	public abstract int getSubjectCount(int objectType, long objectId);
	
		
	public abstract List<AssessmentPlan> getAssessmentPlans(int objectType, long objectId);
	
	public abstract int getAssessmentPlanCount(int objectType, long objectId);
	
	public abstract void saveOrUpdateAssessmentPlan(AssessmentPlan assessment);
	
	public abstract AssessmentPlan getAssessmentPlan(long assessment) throws AssessmentPlanNotFoundException ;
	
	
	/**
	 * 권한이 있는 진단 목록을 가져온다.
	 * @param user
	 * @return
	 */
	public abstract List<AssessmentPlan> getUserAssessments(User user);	
	
	public abstract int getUserAssessmentResultCount(AssessmentPlan assessment, User candidate, String state );	
	
	public abstract List<Assessment> getUserAssessmentResults(AssessmentPlan assessment, User candidate);
	
	public abstract void addAssessmentCandidate(AssessmentPlan assessment, User candidate, Job job, int level ); 
	
}
