package com.podosoftware.competency.assessment.dao;

import java.util.List;

import com.podosoftware.competency.assessment.AssessmentScheme;
import com.podosoftware.competency.assessment.RatingLevel;
import com.podosoftware.competency.assessment.RatingScheme;

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
	
	
	
	
}
