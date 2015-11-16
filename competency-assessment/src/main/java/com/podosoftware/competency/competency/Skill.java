package com.podosoftware.competency.competency;

/**
 * 기술은 직업의 직무 수행을 위하여 필요로하는 재능을 의미한다.
 * @author donghyuck
 *
 */
public interface Skill {
	public String getName();
	
	public void setName(String name);
	
	public String getDescription();
	
	public void setDescription(String description);
}
