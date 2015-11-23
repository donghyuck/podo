package com.podosoftware.competency.competency;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.model.support.PropertyAwareSupport;

public class DefaultEssentialElement extends PropertyAwareSupport implements EssentialElement {
	
	private Long competencyId;
	
	private Long essentialElementId;
	
	private String name;
	
	private String capabilityStandard;
	
	private Integer level;
	
	public DefaultEssentialElement() {
		competencyId = -1L;
		essentialElementId = -1L;
		name = null;
		level = 0;
	}

	public Long getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(Long competencyId) {
		this.competencyId = competencyId;
	}

	public Long getEssentialElementId() {
		return essentialElementId;
	}

	public void setEssentialElementId(Long essentialElementId) {
		this.essentialElementId = essentialElementId;
	}	

	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapabilityStandard() {
		return capabilityStandard;
	}

	public void setCapabilityStandard(String capabilityStandard) {
		this.capabilityStandard = capabilityStandard;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}

	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return essentialElementId;
	}

	@Override
	public int getModelObjectType() {
		return 54;
	}

}
