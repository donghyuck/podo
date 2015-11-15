package com.podosoftware.competency.accessment;

import java.util.List;

public interface RatingScale {

	public long getRatingScaleId();
	
	public void setRatingScaleId(long ratingScaleId);

	public String getName();
	
	public void setName(String name);
	
	public List<RatingLevel> getRatingLevels();
	
	public void setRatingLevels(List<RatingLevel> ratingLevels);
	
}
