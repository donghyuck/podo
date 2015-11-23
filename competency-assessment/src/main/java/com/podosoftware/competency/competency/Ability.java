package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;

public interface Ability extends Cacheable {
	
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);
	
}
