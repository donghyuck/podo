package com.podosoftware.competency.assessment;

import architecture.common.cache.Cacheable;

public class CompetencySelection implements Cacheable {

    private Long selectionId;

    private Integer objectType;

    private Long objectId;

    private String competencyGroupCode;

    private Long competencyId;

    public CompetencySelection() {
	this.selectionId = -1L;
	this.objectType = 0;
	this.objectId = -1L;
	this.competencyGroupCode = null;
	this.competencyId = -1L;
    }

    public int getCachedSize() {
	return 0;
    }

    public Long getSelectionId() {
	return selectionId;
    }

    public void setSelectionId(Long selectionId) {
	this.selectionId = selectionId;
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

    public String getCompetencyGroupCode() {
	return competencyGroupCode;
    }

    public void setCompetencyGroupCode(String competencyGroupCode) {
	this.competencyGroupCode = competencyGroupCode;
    }

    public Long getCompetencyId() {
	return competencyId;
    }

    public void setCompetencyId(Long competencyId) {
	this.competencyId = competencyId;
    }

}
