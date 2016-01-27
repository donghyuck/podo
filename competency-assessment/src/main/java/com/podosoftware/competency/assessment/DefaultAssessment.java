package com.podosoftware.competency.assessment;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.podosoftware.competency.assessment.json.JobSelectionsJsonDeserializer;
import com.podosoftware.competency.assessment.json.RatingSchemeJsonDeserializer;

import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.json.JsonMapPropertyDeserializer;
import architecture.common.model.json.JsonMapPropertySerializer;
import architecture.common.model.support.PropertyAndDateAwareSupport;

public class DefaultAssessment extends PropertyAndDateAwareSupport implements Assessment {

	private long assessmentId;
	private int objectType;
	private long objectId;
	private RatingScheme ratingScheme;
	private String name;
	private String description;
	private boolean multipleApplyAllowed;
	private boolean feedbackEnabled;
	private List<JobSelection> jobSelections;
	private List<Subject> subjects;
	private Date startDate;
	private Date endDate;
	private State state;
	
	public DefaultAssessment() {
		this.assessmentId = -1L;
		this.ratingScheme = null;
		this.name = null;
		this.description = null;
		this.objectType = 0;
		this.objectId = -1L;
		this.multipleApplyAllowed = false;
		this.feedbackEnabled = false;
		this.jobSelections = Collections.EMPTY_LIST;
		this.subjects = Collections.EMPTY_LIST;
		this.state = State.NONE;
		Date now = new Date();
		setCreationDate(now);
		setModifiedDate(now);	
		
	}

	
	public long getAssessmentId() {
		return assessmentId;
	}


	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
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


	public RatingScheme getRatingScheme() {
		return ratingScheme;
	}

	@JsonDeserialize(using=RatingSchemeJsonDeserializer.class)
	public void setRatingScheme(RatingScheme ratingScheme) {
		this.ratingScheme = ratingScheme;
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


	public boolean isMultipleApplyAllowed() {
		return multipleApplyAllowed;
	}


	public void setMultipleApplyAllowed(boolean multipleApplyAllowed) {
		this.multipleApplyAllowed = multipleApplyAllowed;
	}


	public boolean isFeedbackEnabled() {
		return feedbackEnabled;
	}


	public void setFeedbackEnabled(boolean feedbackEnabled) {
		this.feedbackEnabled = feedbackEnabled;
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public State getState() {
		return state;
	}


	public void setState(State state) {
		this.state = state;
	}


	@JsonDeserialize(using = JsonMapPropertyDeserializer.class)	
	public void setProperties(Map<String, String> properties) {
		super.setProperties(properties);
	}

	@JsonSerialize(using = JsonMapPropertySerializer.class)	
	public Map<String, String> getProperties() {
		return super.getProperties();
	}
	
	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return this.assessmentId;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return 70;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 70 ;
	}

	public List<JobSelection> getJobSelections() {
		return jobSelections;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	@JsonDeserialize(using=JobSelectionsJsonDeserializer.class)
	public void setJobSelections(List<JobSelection> jobSelections) {
		this.jobSelections = jobSelections;
	}


	@Override
	public String toString() {
		return "DefaultAssessment [assessmentId=" + assessmentId + ", objectType=" + objectType + ", objectId="
				+ objectId + ", ratingScheme=" + ratingScheme + ", name=" + name + ", description=" + description
				+ ", multipleApplyAllowed=" + multipleApplyAllowed + ", feedbackEnabled=" + feedbackEnabled
				+ ", jobSelections=" + jobSelections + ", subjects=" + subjects + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", state=" + state + ", modifiedDate=" + getModifiedDate() + ", creationDate=" + getCreationDate()  + "]";
	}
	
	
}
