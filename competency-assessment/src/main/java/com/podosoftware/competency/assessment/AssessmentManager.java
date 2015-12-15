package com.podosoftware.competency.assessment;

import java.util.List;

public interface AssessmentManager {

	public abstract List<RatingScheme> getRatingSchemes(int objectType, long objectId);
	
	public abstract void saveOrUpdateRatingScheme(RatingScheme ragintScheme);
	
	public abstract RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException ;
	
	public abstract int getRatingSchemeCount(int objectType, long objectId);
}
