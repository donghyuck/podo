package com.podosoftware.competency.assessment;

import java.util.Date;

public interface Accessment {

	public abstract long getAccessmentId();

	public abstract void setAccessmentId(long accessmentId);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract Date getStartDate();

	public abstract Date getEndDate();

	public abstract State getState();
	
	public abstract void setState(State state);
	
	public enum State {
		INCOMPLETE, 
		APPROVAL, 
		PUBLISHED, 
		REJECTED, 
		ARCHIVED, 
		DELETED, 
		NONE;
	}
}
