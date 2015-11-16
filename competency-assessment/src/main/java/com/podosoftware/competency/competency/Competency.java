package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

public interface Competency extends PropertyAware, Cacheable {

	public Integer getObjectType();

	public void setObjectType(Integer objectType) ;

	public Long getObjectId() ;

	public void setObjectId(Long objectId);
	
	public Long getCompetencyId();
	
	public void setCompetencyId(Long competencyId);
	
	public String getName();
	
	public void setName(String name);
		
	public String getDescription();
	
	public void setDescription(String description);
	
}
