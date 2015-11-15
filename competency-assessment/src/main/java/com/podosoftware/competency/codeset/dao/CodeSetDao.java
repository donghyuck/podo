package com.podosoftware.competency.codeset.dao;

import java.util.List;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetTreeWalker;

public interface CodeSetDao {
	
	public void saveOrUpdateCodeSet(CodeSet codeset);
	
	public CodeSet getCodeSetById(long codesetId);
	
	public int getCodeSetCount(int objectType, long objectId);
	
	public List<Long> getCodeSetIds(int objectType, long objectId);
	
	public CodeSetTreeWalker getCodeSetTreeWalker(int objectType, long objectId);
	
}
