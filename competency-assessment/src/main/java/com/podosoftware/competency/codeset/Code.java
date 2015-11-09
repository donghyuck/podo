package com.podosoftware.competency.codeset;

public interface Code {

	public long getCodeId() ;

	public void setCodeId(long codeId) ;

	public long getCodeSetId() ;

	public void setCodeSetId(long codeSetId) ;

	public String getName();

	public void setName(String name) ;

	public int getIndex() ;

	public void setIndex(int index);

	public String getDescription();

	public void setDescription(String description) ;
}
