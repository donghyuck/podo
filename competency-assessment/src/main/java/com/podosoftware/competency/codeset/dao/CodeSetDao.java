package com.podosoftware.competency.codeset.dao;

import java.util.List;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetTreeWalker;

public interface CodeSetDao {
	
	public Long nextCodeSetId();
	
	public void batchInsertCodeSet(List<CodeSet> codesets);
	
	public void saveOrUpdateCodeSet(List<CodeSet> codesets);
	
	public void saveOrUpdateCodeSet(CodeSet codeset);
	
	public CodeSet getCodeSetById(long codesetId);
	
	public int getCodeSetCount(int objectType, long objectId);
	
	public List<Long> getCodeSetIds(int objectType, long objectId);
	
	public int getCodeSetCount(int objectType, long objectId, Long codeSetId);
	
	public List<Long> getCodeSetIds(int objectType, long objectId, Long codeSetId);
	
	public CodeSetTreeWalker getCodeSetTreeWalker(int objectType, long objectId);
	
}
