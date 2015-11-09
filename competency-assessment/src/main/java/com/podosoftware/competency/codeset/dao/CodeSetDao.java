package com.podosoftware.competency.codeset.dao;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetTreeWalker;

public interface CodeSetDao {
	
	public void saveOrUpdateCodeSet(CodeSet codeset);
	
	public CodeSetTreeWalker getCodeSetTreeWalker(int objectType, long objectId);
	
}
