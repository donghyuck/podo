package com.podosoftware.competency.job;

import java.util.Date;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

public interface Job extends PropertyAware, Cacheable  {
	
	public Long getJobId();
	
	public void setJobId(Long jobId);
	
	public Classfication getClassfication();
	
	public void setClassfication(Classfication classfication);
	
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public Date getCreationDate() ;

	public void setCreationDate(Date creationDate) ;

	public Date getModifiedDate() ;

	public void setModifiedDate(Date modifiedDate) ;
	
}
