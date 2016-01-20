package com.podosoftware.competency.assessment;

import com.podosoftware.competency.competency.CompetencyType;

public enum AssessmentMethod {
	NONE(0),
	COGNITIVE_ABILITY_TESTS(1), /** 인지능혁테스트 */
	JOB_KNOWLEDGE_TESTS(2),
	PERSONALITY_TESTS(3),
	BIOGRAPHICAL_DATA(4),
	INTEGRITY_TESTS(5),
	STRUCTURED_INTERVIEWS(6),
	PHYSICAL_FITNESS_TESTS(7),
	PHYSICAL_ABILITY_TESTS(8),
	SITUATIONAL_JUDGMENT_TESTS(9),
	WORK_SAMPLE_TESTS(10),
	ASSESSMENT_CENTERS(11);
	
	private int id ;

	private AssessmentMethod(int id) {
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public static AssessmentMethod getAssessmentMethodById(int id){
		for( AssessmentMethod type : AssessmentMethod.values()){
			if( type.getId() == id)
				return type;
		}
		return AssessmentMethod.NONE;
	}
}
