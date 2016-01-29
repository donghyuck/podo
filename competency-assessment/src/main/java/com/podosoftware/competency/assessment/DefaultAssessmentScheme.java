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

import architecture.common.model.json.JsonMapPropertyDeserializer;
import architecture.common.model.json.JsonMapPropertySerializer;
import architecture.common.model.support.PropertyAndDateAwareSupport;

public class DefaultAssessmentScheme extends PropertyAndDateAwareSupport implements AssessmentScheme {

	private long assessmentSchemeId;
	private int objectType;
	private long objectId;
	private RatingScheme ratingScheme;
	private String name;
	private String description;
	private boolean multipleApplyAllowed;
	private boolean feedbackEnabled;
	private List<JobSelection> jobSelections;
	private List<Subject> subjects;
	
	public DefaultAssessmentScheme() {
		this.assessmentSchemeId = -1L;
		this.ratingScheme = null;
		this.name = null;
		this.description = null;
		this.objectType = 0;
		this.objectId = -1L;
		this.multipleApplyAllowed = false;
		this.feedbackEnabled = false;
		this.jobSelections = Collections.EMPTY_LIST;
		this.subjects = Collections.EMPTY_LIST;
		Date now = new Date();
		setCreationDate(now);
		setModifiedDate(now);	
	}
	
	

	public boolean isFeedbackEnabled() {
		return feedbackEnabled;
	}



	public void setFeedbackEnabled(boolean feedbackEnabled) {
		this.feedbackEnabled = feedbackEnabled;
	}



	public boolean isMultipleApplyAllowed() {
		return multipleApplyAllowed;
	}

	public void setMultipleApplyAllowed(boolean multipleApplyAllowed) {
		this.multipleApplyAllowed = multipleApplyAllowed;
	}

	public long getAssessmentSchemeId() {
		return assessmentSchemeId;
	}

	public void setAssessmentSchemeId(long assessmentSchemeId) {
		this.assessmentSchemeId = assessmentSchemeId;
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
		return this.assessmentSchemeId;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return 71;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 71 ;
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
		return "AssessmentScheme [assessmentSchemeId=" + assessmentSchemeId + ", objectType=" + objectType
				+ ", objectId=" + objectId + ", ratingScheme=" + ratingScheme + ", name=" + name + ", description="
				+ description + ", multipleApplyAllowed=" + multipleApplyAllowed + ", feedbackEnabled="
				+ feedbackEnabled + ", jobSelections=" + jobSelections + ", subjects=" + subjects + "]";
	}

}
