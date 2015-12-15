package com.podosoftware.competency.assessment;

import architecture.common.cache.Cacheable;

public interface RatingLevel extends Cacheable {
			
	public Long getRatingSchemeId();

	public void setRatingSchemeId(Long ratingSchemeId) ;
	
	public Long getRatingLevelId();
	
	public void setRatingLevelId(Long ratingLevelId);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public int getScore();
	
	public void setScore(int score);
	
}
