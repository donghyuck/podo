package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;

public interface Ability extends Cacheable {
	
	public Integer getObjectType();

	public void setObjectType(Integer objectType) ;

	public Long getObjectId() ;

	public void setObjectId(Long objectId);
	
	public Long getAbilityId();
	
	public void setAbilityId(Long abilityId);
	
	public AbilityType getAbilityType();
	
	public void setAbilityType(AbilityType abilityType);
	
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);

}
