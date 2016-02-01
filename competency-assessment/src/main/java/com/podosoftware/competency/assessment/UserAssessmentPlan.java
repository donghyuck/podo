package com.podosoftware.competency.assessment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.json.CustomJsonDateSerializer;

public class UserAssessmentPlan implements AssessmentPlan {

	private AssessmentPlan assessmentPlan;

	public UserAssessmentPlan(AssessmentPlan assessmentPlan) {
		this.assessmentPlan = assessmentPlan;
	}
 
	public Map<String, String> getProperties() {
		return assessmentPlan.getProperties();
	}

	public void setProperties(Map<String, String> properties) {
	}

	public boolean getBooleanProperty(String name, boolean defaultValue) {
		return assessmentPlan.getBooleanProperty(name, defaultValue);
	}

	public long getLongProperty(String name, long defaultValue) {
		return assessmentPlan.getLongProperty(name, defaultValue);
	}
 
	public int getIntProperty(String name, int defaultValue) {
		return assessmentPlan.getIntProperty(name, defaultValue);
	}
 
	public String getProperty(String name, String defaultValue) {
		return assessmentPlan.getProperty(name, defaultValue);
	}
 
	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return assessmentPlan.getAssessmentId();
	}
 
	@JsonIgnore
	public int getModelObjectType() {
		return assessmentPlan.getModelObjectType();
	}
 
	@JsonIgnore
	public int getCachedSize() {
		return assessmentPlan.getCachedSize();
	}
 
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate() {
		return assessmentPlan.getCreationDate();
	}
 
	public void setCreationDate(Date creationDate){		
	}
 
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate() {
		// TODO Auto-generated method stub
		return assessmentPlan.getModifiedDate();
	}
 
	public void setModifiedDate(Date modifiedDate) {		
	}
 
	public long getAssessmentId() {
		return assessmentPlan.getAssessmentId();
	}
 
	public void setAssessmentId(long accessmentId) {		
	}
 
	public int getObjectType() {
		return assessmentPlan.getObjectType();
	}
 
	public void setObjectType(int objectType) {		
	}
 
	public long getObjectId() {
		return assessmentPlan.getObjectId();
	}
 
	public void setObjectId(long objectId) {
	}
 
	public String getName() {
		return assessmentPlan.getName();
	}
 
	public void setName(String name) {
		
	}
 
	public String getDescription() {
		return assessmentPlan.getDescription();
	}
 
	public void setDescription(String description) {
		
	}
 
	public RatingScheme getRatingScheme() {
		return assessmentPlan.getRatingScheme();
	}
 
	public void setRatingScheme(RatingScheme ratingScheme) {
	}

	@Override
	public boolean isMultipleApplyAllowed() {
		return assessmentPlan.isMultipleApplyAllowed();
	}
 
	public void setMultipleApplyAllowed(boolean multipleApplyAllowed) {		
	} 
	
	public boolean isFeedbackEnabled() {
		return assessmentPlan.isFeedbackEnabled();
	}
 
	public void setFeedbackEnabled(boolean feedbackEnabled) {
	}
 
	public void setJobSelections(List<JobSelection> jobSelections) {		
	}
 
	public List<JobSelection> getJobSelections() {
		return assessmentPlan.getJobSelections();
	}
 
	public void setSubjects(List<Subject> subjects) {		
	}
 
	public List<Subject> getSubjects() {
		return assessmentPlan.getSubjects();
	}
 
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getStartDate() {
		return assessmentPlan.getStartDate();
	}
 
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getEndDate() {
		return assessmentPlan.getEndDate();
	}
 
	public State getState() {
		return assessmentPlan.getState();
	}

	public void setState(State state) {		
	}
	
	public int hashCode() {
		return assessmentPlan.hashCode();
	}
	public boolean equals(Object obj) {
		return assessmentPlan.equals(obj);
	}
	
}
