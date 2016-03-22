package com.podosoftware.competency.assessment;

import java.util.List;

import com.podosoftware.competency.job.Job;

import architecture.common.user.User;

public interface AssessmentManager {

    public abstract List<RatingScheme> getRatingSchemes(int objectType, long objectId);

    public abstract int getRatingSchemeCount(int objectType, long objectId);

    public abstract void saveOrUpdateRatingScheme(RatingScheme ragintScheme);

    public abstract RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException;

    public abstract List<AssessmentScheme> getAssessmentSchemes(int objectType, long objectId);

    public abstract int getAssessmentSchemeCount(int objectType, long objectId);

    public abstract void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme);

    public abstract AssessmentScheme getAssessmentScheme(long assessmentSchemeId)
	    throws AssessmentSchemeNotFoundException;

    public abstract void saveOrUpdateJobSelections(List<JobSelection> jobSelectinos);

    public abstract List<JobSelection> getJobSelections(int objectType, long objectId);

    public abstract int getJobSelectionCount(int objectType, long objectId);

    public abstract void saveOrUpdateSubjects(List<Subject> subjects);

    public abstract List<Subject> getSubjects(int objectType, long objectId);

    public abstract int getSubjectCount(int objectType, long objectId);

    public abstract List<AssessmentPlan> getAssessmentPlans(int objectType, long objectId);

    public abstract int getAssessmentPlanCount(int objectType, long objectId);

    public abstract void saveOrUpdateAssessmentPlan(AssessmentPlan assessment);

    public abstract AssessmentPlan getAssessmentPlan(long assessment) throws AssessmentPlanNotFoundException;

    public abstract void saveOrUpdateUserAssessment(Assessment assessment);

    /**
     * 권한이 있는 진단계획 목록을 가져온다.
     * 
     * @param user
     * @return
     */
    public abstract List<AssessmentPlan> getUserAssessmentPlans(User user);

    /**
     * 
     * @param assessmentPlan
     * @param user
     * @return
     */
    public abstract AssessmentStats getAssessmentStats(AssessmentPlan assessmentPlan, User user);

    /**
     * 진단에 참여한 사용자 수를 리턴한다.
     * 
     * @param assessmentPlan
     * @return
     */
    public abstract int getUserAssessmentCount(User candidate, AssessmentPlan assessment, String state);

    public abstract Assessment getAssessment(long assessmentId)
	    throws AssessmentPlanNotFoundException, AssessmentNotFoundException;

    public abstract List<Assessment> getUserAssessments(User candidate, AssessmentPlan assessment, String state);

    public abstract boolean hasUserAssessed(AssessmentPlan assessment, User candidate);

    public abstract void addAssessmentCandidate(AssessmentPlan assessment, User candidate, Job job, int level);

    public abstract List<AssessmentQuestion> getUserAssessmentQuestions(Assessment assessment);

    public abstract void saveOrUpdateUserAssessmentScores(Assessment assessment, User assessor,
	    List<AssessmentQuestion> answers);

    public abstract List<AssessedEssentialElementScore> getUserAssessedSummaries(Assessment assessment);

}
