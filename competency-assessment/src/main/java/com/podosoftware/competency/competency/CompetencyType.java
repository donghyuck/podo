package com.podosoftware.competency.competency;

public enum CompetencyType {

    NONE(0), ORGANIZATIONAL(1), FUNCTIONAL(2), JOB(3), LEADERSHIP(4), CORE(5);

    private int id;

    /**
     * 
     * 
     * 
     * 
     * @param id
     */

    private CompetencyType(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public String toString() {
	StringBuilder builder = new StringBuilder("CompetencyType:");
	switch (id) {
	case 0: // '\000'
	    builder.append("NONE");
	    break;

	case 1: // '\001'
	    builder.append("ORGANIZATIONAL");
	    break;
	case 2: // '\002'
	    builder.append("FUNCTIONAL");
	    break;
	case 3: // '\003'
	    builder.append("JOB");
	    break;
	case 4: // '\004'
	    builder.append("LEADERSHIP");
	    break;
	case 5: // '\005'
	    builder.append("CORE");
	    break;
	default:
	    builder.append("id=").append(id);
	    break;
	}
	return builder.toString();
    }

    public static CompetencyType getCompetencyTypeById(int typeId) {
	for (CompetencyType type : CompetencyType.values()) {
	    if (type.getId() == typeId)
		return type;
	}
	return CompetencyType.NONE;
    }
}
