package com.podosoftware.competency.codeset;

import java.util.Date;
import java.util.List;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

public interface CodeSet extends PropertyAware, Cacheable{
	
	public boolean isEnabled() ;

	public String getDescription() ;

	public void setDescription(String description) ;

	public Date getCreationDate() ;

	public void setCreationDate(Date creationDate) ;

	public Date getModifiedDate() ;

	public void setModifiedDate(Date modifiedDate) ;

	public String getName();

	public void setName(String name) ;

	public void setEnabled(boolean enabled) ;

	public long getCodeSetId() ;

	public void setCodeSetId(long codeSetId) ;

	public Long getParentCodeSetId() ;

	public void setParentCodeSetId(Long parentCodeSetId);

	public List<Code> getCodes() ;

	public void setCodes(List<Code> codes) ;
	
	public int getObjectType();

	public void setObjectType(int objectType) ;

	public long getObjectId() ;

	public void setObjectId(long objectId);

	public String getCode();

	public void setCode(String code);
	
}
