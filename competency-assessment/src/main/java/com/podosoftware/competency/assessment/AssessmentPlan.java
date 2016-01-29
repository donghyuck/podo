package com.podosoftware.competency.assessment;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import architecture.common.model.json.CustomJsonDateDeserializer;

public class AssessmentPlan {
	
	private int objectType;
	private long objectId;
	private String name;
	private String description;
	private long assessmentSchemeId;
	private Date startDate;
	private Date endDate;
	
	public AssessmentPlan() {
		this.objectType = 0 ;
		this.objectId = -1L;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
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

	public long getAssessmentSchemeId() {
		return assessmentSchemeId;
	}

	public void setAssessmentSchemeId(long assessmentSchemeId) {
		this.assessmentSchemeId = assessmentSchemeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "AssessmentPlan [objectType=" + objectType + ", objectId=" + objectId + ", name=" + name
				+ ", description=" + description + ", assessmentSchemeId=" + assessmentSchemeId + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}

}
