package com.podosoftware.competency.accessment;

import java.util.Date;

import architecture.common.user.User;

public interface RatedPerformanceCriteriaLevel {
	
	public long getAccessmentId();
	
	public void setAccessmentId(long accessmentId);
	
	public User getApplicant();
	
	public void setApplicant(User user);
	
	public User getAccessor();
	
	public void setAccessor(User user);

	public long getEssentialElementId();
	
	public void setEssestialElementId(long essestialElementId);
		
	public int getScore();
	
	public void setScore(int score);
	
	public Date getCreationDate();

	public void setCreationDate(Date creationDate) ;

	public Date getModifiedDate() ;

	public void setModifiedDate(Date modifiedDate) ;
	
}
