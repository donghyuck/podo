package com.podosoftware.competency.assessment;

import java.util.Date;

public class AssessmentPlan {
	
	private String name;
	private String description;
	private long assessmentSchemeId;
	private Date startDate;
	private Date endDate;
	
	public AssessmentPlan() {
		
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

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
