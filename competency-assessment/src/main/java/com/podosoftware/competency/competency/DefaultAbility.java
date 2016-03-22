package com.podosoftware.competency.competency;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;

public class DefaultAbility implements Ability {

    private Integer objectType;

    private Long objectId;

    private Long abilityId;

    private String name;

    private String description;

    private AbilityType abilityType;

    public DefaultAbility() {
	this.objectType = 1;
	this.objectId = -1L;
	this.abilityId = -1L;
	this.name = null;
	this.description = null;
	this.abilityType = AbilityType.NONE;
    }

    @JsonIgnore
    public int getCachedSize() {
	int size = CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt() + CacheSizes.sizeOfLong()
		+ CacheSizes.sizeOfString(this.name) + CacheSizes.sizeOfString(this.description)
		+ CacheSizes.sizeOfObject();
	return 0;
    }

    public Integer getObjectType() {
	return objectType;
    }

    public void setObjectType(Integer objectType) {
	this.objectType = objectType;
    }

    public Long getObjectId() {
	return objectId;
    }

    public void setObjectId(Long objectId) {
	this.objectId = objectId;
    }

    public Long getAbilityId() {
	return abilityId;
    }

    public void setAbilityId(Long abilityId) {
	this.abilityId = abilityId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public AbilityType getAbilityType() {
	return abilityType;
    }

    public void setAbilityType(AbilityType abilityType) {
	this.abilityType = abilityType;
    }

}
