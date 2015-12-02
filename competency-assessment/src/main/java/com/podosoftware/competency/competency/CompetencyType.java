package com.podosoftware.competency.competency;

public enum CompetencyType {
	
	NONE(0),	
	ORGANIZATIONAL(1),
	FUNCTIONAL(2),
	JOB(3),
	LEADERSHIP(4),
	;
	
	
	private int id;

	private CompetencyType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("CompetencyType:");
		 switch(id)
	        {
	        case 0: // '\001'
	            builder.append("NONE");
	            break;

	        case 1: // '\002'
	            builder.append("ORGANIZATIONAL");
	            break;
	        case 2: // '\002'
	            builder.append("FUNCTIONAL");
	            break;
	        case 3: // '\002'
	            builder.append("JOB");
	            break;
	        case 4: // '\002'
	            builder.append("LEADERSHIP");
	            break;	      	            
	        default:
	            builder.append("id=").append(id);
	            break;
	        }
	        return builder.toString();
	}
	public static CompetencyType getCompetencyTypeById(int typeId){
		for( CompetencyType type : CompetencyType.values()){
			if( type.getId() == typeId)
				return type;
		}
		return CompetencyType.NONE;
	}
}
