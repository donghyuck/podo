package com.podosoftware.competency.assessment;

public enum AssessmentCategory {
	NONE(0),	
	KNOWLEDGE(1),
	DECISIONMAKING(2),
	PRACTICE_PERFORMANCE_AND_PERSONAL_ATTRIBUTES(3),
	SKILLS_AND_TASKS(4) ;
	
	private int id;

	private AssessmentCategory(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
}
