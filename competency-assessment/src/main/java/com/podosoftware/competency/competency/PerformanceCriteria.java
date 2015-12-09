package com.podosoftware.competency.competency;

import architecture.common.model.DateAware;
import architecture.common.model.PropertyAware;

/**
 * 
 * Performance Criteria are evaluative statements, which specify what is to be
 * assessed and the required level of performance. 
 * 
 * They detail the activities,
 * skills, knowledge and understanding that provide evidence of competent
 * performance of each element.
 * 
 * @author donghyuck
 *
 */
public interface PerformanceCriteria extends PropertyAware, DateAware {

	public int getSortOrder();
	
	public void setSortOrder(int sortOrder);
	
	public int getObjectType();

	public void setObjectType(int objectType);

	public long getObjectId();

	public void setObjectId(long objectId);

	public long getPerformanceCriteriaId();

	public void setPerformanceCriteriaId(long performanceCriteriaId);

	public String getDescription();

	public void setDescription(String description);

}
