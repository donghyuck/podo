package com.podosoftware.competency.assessment;

import java.util.Date;
import java.util.List;

import architecture.common.model.DateAware;
import architecture.common.model.PropertyAware;

public interface Assessment extends PropertyAware, DateAware {

	public abstract long getAssessmentId();

	public abstract void setAssessmentId(long accessmentId);

	public int getObjectType();

	public void setObjectType(int objectType) ;

	public long getObjectId() ;

	public void setObjectId(long objectId);
	
	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getDescription();

	public abstract void setDescription(String description);
	

	public RatingScheme getRatingScheme();

	public void setRatingScheme(RatingScheme ratingScheme) ;
	
	public boolean isMultipleApplyAllowed() ;

	public void setMultipleApplyAllowed(boolean multipleApplyAllowed) ;
	
	public boolean isFeedbackEnabled() ;

	public void setFeedbackEnabled(boolean feedbackEnabled) ;
	
	public void setJobSelections(List<JobSelection> jobSelections);
	
	public List<JobSelection> getJobSelections();
	
	public void setSubjects(List<Subject> subjects);
	
	public List<Subject> getSubjects();	
	
	

	public abstract Date getStartDate();

	public abstract Date getEndDate();

	public abstract State getState();
	
	public abstract void setState(State state);
	
	public enum State {
		INCOMPLETE, 
		APPROVAL, 
		PUBLISHED, 
		REJECTED, 
		ARCHIVED, 
		DELETED, 
		NONE;
	}
}
