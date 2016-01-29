package com.podosoftware.competency.assessment;

import java.util.List;

import architecture.common.model.DateAware;
import architecture.common.user.User;

public interface AssessmentResult extends DateAware {

	public enum State {
		INCOMPLETE, 
		ASSESSED, 
		DELETED, 
		NONE;
	}
	
	public abstract Assessment getAssessment();
	
	public abstract void setAssessment(Assessment assessment);
	
	public abstract State getState();
	
	public abstract void setState(State state);
	
	public abstract User getCandidate(); 
	
	public abstract void setCandidate();
	
	public abstract List<User> getAssessors();
	
	public abstract void setAssessors(List<User> users);
	
	public int getTotalScore();
	
	public void setTotalScore(int totalScore);
	
}
