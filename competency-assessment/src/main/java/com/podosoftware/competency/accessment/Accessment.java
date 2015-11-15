package com.podosoftware.competency.accessment;

import java.util.Date;

public interface Accessment {

	public long getAccessmentId();
	
	public void setAccessmentId(long accessmentId);
	
	public String getName();
	
	public void setName(String name);	
	
	public Date getStartDate();
	
	public Date getEndDate();
			
}
