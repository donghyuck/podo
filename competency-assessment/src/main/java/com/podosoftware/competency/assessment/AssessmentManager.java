package com.podosoftware.competency.assessment;

import java.util.List;

public interface AssessmentManager {

	public abstract List<RatingScheme> getRatingSchemes(int objectType, long objectId);
	
	public abstract int getRatingSchemeCount(int objectType, long objectId);
	
	public abstract void saveOrUpdateRatingScheme(RatingScheme ragintScheme);
	
	public abstract RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException ;
		
	public abstract List<AssessmentScheme> getAssessmentSchemes(int objectType, long objectId);
	
	public abstract int getAssessmentSchemeCount(int objectType, long objectId);
	
	public abstract void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme);
	
	public abstract AssessmentScheme getAssessmentScheme(long assessmentSchemeId) throws AssessmentSchemeNotFoundException ;
		
	
}
