package com.podosoftware.competency.competency;

import architecture.common.cache.Cacheable;

public interface Competency extends Cacheable {
	
	public long getCompetencyId();
	
	public void setCompetencyId(long competencyId);
	
	public String getName();
	
	public void setName();
		
	public String getDescription();
	
	public void setDescription(String description);

	public int getObjectType();

	public void setObjectType(int objectType) ;

	public long getObjectId() ;

	public void setObjectId(long objectId);
	
}
