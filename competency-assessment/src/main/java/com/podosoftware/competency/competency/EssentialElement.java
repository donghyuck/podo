package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

public interface EssentialElement extends PropertyAware, Cacheable {

	public Long getCompetencyId();
	
	public void setCompetencyId(Long competencyId);
	
	public Long getEssentialElementId();
	
	public void setEssentialElementId(Long essestialElementId);
	
	public String getName();
	
	public void setName(String name);

	public String getDescription();
	
	public void setDescription(String description);
	
	public String getCapabilityStandard();
	
	public void setCapabilityStandard(String capabilityStandard);
	
	public Integer getLevel() ;

	public void setLevel(Integer level) ;
	
}
