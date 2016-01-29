package com.podosoftware.competency.assessment.dao;

import java.util.List;

import com.podosoftware.competency.assessment.Assessment;
import com.podosoftware.competency.assessment.AssessmentScheme;
import com.podosoftware.competency.assessment.JobSelection;
import com.podosoftware.competency.assessment.RatingLevel;
import com.podosoftware.competency.assessment.RatingScheme;
import com.podosoftware.competency.assessment.Subject;

import architecture.common.user.User;

public interface AssessmentDao {
	
	public abstract List<Long> getRatingSchemeIds( int objectType, long objectId);
	
	public abstract int getRatingSchemeCount(int objectType, long objectId);
	
	public abstract RatingScheme getRatingSchemeById(long ratingSchemeId);
	
	public abstract void saveOrUpdateRatingScheme(RatingScheme ratingScheme); 
	
	public abstract List<RatingLevel> getRatingLevels(long ratingSchemeId);
	
	public abstract void saveOrUpdateRatingLevels(List<RatingLevel> ratingLevels) ;
	
	
	
	public abstract List<Long> getAssessmentSchemeIds( int objectType, long objectId);
	
	public abstract int getAssessmentSchemeCount(int objectType, long objectId);
	
	public abstract AssessmentScheme getAssessmentSchemeById(long assessmentSchemeId);
	
	public abstract void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme); 
	
	
	/**
	 * 파라메터 objectType, objectId 에 해당하는 직무선택 객체 ID 를 리턴한다.
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public List<Long> getAssessmentJobSelectionIds(int objectType, long objectId);
	
	/**
	 * 파라메터 objectType, objectId 에 해당하는 직무선택 객체 수를 리턴한다.
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public int getAssessmentJobSelectionCount(int objectType, long objectId);
	
	
	public JobSelection getAssessmentJobSelectionById(long selectionId) ;
	
	public void removeAssessmentJobSelections(final List<JobSelection> jobSelections);
	
	public void saveOrUpdateAssessmentJobSelections(List<JobSelection> jobSelections);
	
	
	
	/**
	 * 파라메터 objectType, objectId 에 해당하는 대상자 객체 ID 를 리턴한다.
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public List<Long> getAssessmentSubjectIds(int objectType, long objectId);
	
	/**
	 * 파라메터 objectType, objectId 에 해당하는 대상자 객체 수를 리턴한다.
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public int getAssessmentSubjectCount(int objectType, long objectId);	
	
	public Subject getAssessmentSubjectById(long subjectId) ;
	
	public void removeAssessmentSubjects(final List<Subject> jobSelections);
	
	public void saveOrUpdateAssessmentSubjects(List<Subject> jobSelections);
	
	
	
	public abstract List<Long> getAssessmentIds( int objectType, long objectId);
	
	public abstract int getAssessmentCount(int objectType, long objectId);
	
	public abstract Assessment getAssessmentById(long assessmentId);
	
	public abstract void saveOrUpdateAssessment(Assessment assessment); 
	
	
	
	/**
	 * user 가 이용가능한 모든 진단 객체 ID를 리턴한다.
	 * @param user
	 * @return
	 */
	public abstract List<Long> getAssessmentIdsByUser(User user);
	
}
