package com.podosoftware.competency.assessment;

import java.util.List;

import architecture.common.user.User;

public interface Application {
		
	public long getAccessmentId();
	
	public void setAccessmentId(long accessmentId);
	
	public User getApplicant();
		
	public boolean isSelfAccessment();
		
	/**
	 * 
	 * @return
	 */
	public List<User> getValidators();
	
	/**
	 * 진단자 
	 * @return
	 */
	public List<User> getAccessors();
		
	public RatingScale getRatingScale();
	
	public void setRatingScale(RatingScale ratingScale);
		
	public List<Object> getQuestions();	

}
