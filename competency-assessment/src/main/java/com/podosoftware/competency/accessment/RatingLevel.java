package com.podosoftware.competency.accessment;

import architecture.common.cache.Cacheable;

public interface RatingLevel extends Cacheable {
	
	public int getOrderNumber();
	
	public void setOrderNumber();
		
	public String getDescription();
	
	public void setDescription();
	
	public int getScore();
	
	public void setScore(int score);
	
}
