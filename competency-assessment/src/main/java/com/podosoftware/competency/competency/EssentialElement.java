package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;

public interface EssentialElement extends Cacheable {

	public long getCompetencyId();
	
	public void setCompetencyId(long competencyId);
	
	public long getEssentialElementId();
	
	public void setEssestialElementId(long essestialElementId);
	
	public String getName();
	
	public void setName(String name);

	public String getCapabilityStandard();
	
	public void setCapabilitytandard(String capabilityStandard);
	
}
