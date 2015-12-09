package com.podosoftware.competency.competency;

public enum AbilityType {
	NONE(0),	
	KNOWLEDGE(1),
	SKILL(2),
	ATTITUDE(3)
	;
	
	
	private int id;

	private AbilityType(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("AbilityType:");
		 switch(id)
	        {
	        case 0: // '\000'
	            builder.append("NONE");
	            break;
	        case 1: // '\001'
	            builder.append("KNOWLEDGE");
	            break;
	        case 2: // '\002'
	            builder.append("SKILL");
	            break;
	        case 3: // '\003'
	            builder.append("ATTITUDE");
	            break;        
	        default:
	            builder.append("id=").append(id);
	            break;
	        }
	        return builder.toString();
	}
	public static AbilityType getAbilityTypeById(int typeId){
		for( AbilityType type : AbilityType.values()){
			if( type.getId() == typeId)
				return type;
		}
		return AbilityType.NONE;
	}
}
