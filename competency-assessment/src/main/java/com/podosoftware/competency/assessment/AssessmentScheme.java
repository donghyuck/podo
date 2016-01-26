package com.podosoftware.competency.assessment;

import java.util.List;

import architecture.common.model.DateAware;
import architecture.common.model.PropertyAware;

public interface AssessmentScheme extends PropertyAware, DateAware {
	
	public abstract long getAssessmentSchemeId();
	
	public abstract void setAssessmentSchemeId(long assessmentSchemeId);
	
	public int getObjectType();

	public void setObjectType(int objectType) ;

	public long getObjectId() ;

	public void setObjectId(long objectId);
	
	public RatingScheme getRatingScheme();

	public void setRatingScheme(RatingScheme ratingScheme) ;
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	public abstract String getDescription();
	
	public abstract void setDescription(String description);
	
	public boolean isMultipleApplyAllowed() ;

	public void setMultipleApplyAllowed(boolean multipleApplyAllowed) ;
	
	public boolean isFeedbackEnabled() ;

	public void setFeedbackEnabled(boolean feedbackEnabled) ;
	
	public void setJobSelections(List<JobSelection> jobSelections);
	
	public List<JobSelection> getJobSelections();
	
	public void setSubjects(List<Subject> subjects);
	
	public List<Subject> getSubjects();
	
}
