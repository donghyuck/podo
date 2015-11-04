package com.podosoftware.competency.code;

import java.util.Date;
import java.util.List;

import architecture.common.user.Company;

public class DefaultCodeSetManager implements CodeSetManager {

	@Override
	public List<CodeSet> getCodeSets(Company company) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CodeSet> getCodeSets(CodeSet codeset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCodeSetCount(Company company) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCodeSetCount(CodeSet codeset) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CodeSet createCodeSet(Company company, String name, String description) {		
		Date now = new Date();
		int objectType = 1;
		long objectId = company.getCompanyId();		
		CodeSet set = new DefaultCodeSet();
		set.setObjectType(objectType);
		set.setObjectId(objectId);
		set.setName(name);
		set.setDescription(description);				
		return set;
	}

	@Override
	public CodeSet createCodeSet(CodeSet codeset, String name, String desctiption) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCodeSet(CodeSet codeset) {
		// TODO Auto-generated method stub
		
	}

}
