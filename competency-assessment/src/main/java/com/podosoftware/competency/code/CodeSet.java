package com.podosoftware.competency.code;

import java.util.Date;
import java.util.List;

public interface CodeSet {
	
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

	public CodeSet getParentCodeSet() ;

	public void setParentCodeSet(CodeSet parentCodeSet);

	public List<Code> getCodes() ;

	public void setCodes(List<Code> codes) ;
	
	public int getObjectType();

	public void setObjectType(int objectType) ;

	public long getObjectId() ;

	public void setObjectId(long objectId);

}
