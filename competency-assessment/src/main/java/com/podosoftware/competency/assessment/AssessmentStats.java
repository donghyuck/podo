package com.podosoftware.competency.assessment;

import java.util.List;

public class AssessmentStats {
	
	private AssessmentPlan assessmentPlan ; 
	
	private int userAssessedCount = 0;
	
	private int userIncompleteCount = 0 ;
		
	private List<Assessment> userAssessments;
	
	public AssessmentStats() {
		
	}

	public long getAssessmentPlanId(){
		return assessmentPlan.getAssessmentId();
	}
	
	public AssessmentPlan getAssessmentPlan() {
		return assessmentPlan;
	}

	public void setAssessmentPlan(AssessmentPlan assessmentPlan) {
		this.assessmentPlan = assessmentPlan;
	}

	public int getUserAssessedCount() {
		return userAssessedCount;
	}

	public void setUserAssessedCount(int userAssessedCount) {
		this.userAssessedCount = userAssessedCount;
	}

	public int getUserIncompleteCount() {
		return userIncompleteCount;
	}

	public void setUserIncompleteCount(int userIncompleteCount) {
		this.userIncompleteCount = userIncompleteCount;
	}

	public List<Assessment> getUserAssessments() {
		return userAssessments;
	}

	public void setUserAssessments(List<Assessment> userAssessments) {
		this.userAssessments = userAssessments;
	}

	
}
