package com.podosoftware.competency.codeset;

import java.util.Date;
import java.util.List;

import com.podosoftware.competency.codeset.dao.CodeSetDao;

import architecture.common.user.Company;

public class DefaultCodeSetManager implements CodeSetManager {
	
	public static final CodeSet ROOT_CODE_SET = new DefaultCodeSet();
	private CodeSetDao codeSetDao;
	
	
	
	public CodeSetDao getCodeSetDao() {
		return codeSetDao;
	}

	public void setCodeSetDao(CodeSetDao codeSetDao) {
		this.codeSetDao = codeSetDao;
	}

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


	public CodeSet createCodeSet(Company company, String name, String description) {		
		int objectType = 1;
		long objectId = company.getCompanyId();				
		CodeSet codeset = new DefaultCodeSet();
		codeset.setObjectType(objectType);
		codeset.setObjectId(objectId);
		codeset.setName(name);
		codeset.setDescription(description);	
		return codeset;
	}

	public CodeSet createCodeSet(CodeSet orgCodeset, String name, String description) {		
		CodeSet codeset = new DefaultCodeSet();
		codeset.setObjectType(orgCodeset.getObjectType());
		codeset.setObjectId(orgCodeset.getObjectId());
		codeset.setParentCodeSetId(orgCodeset.getCodeSetId());
		codeset.setName(name);
		codeset.setDescription(description);				
		return codeset;
	}

	@Override
	
	public void saveOrUpdate(CodeSet codeset) {
		if( codeset.getCodeSetId() > 0)
		{			
			Date now = new Date();
			codeset.setModifiedDate(now);		
		}		
		
		codeSetDao.saveOrUpdateCodeSet(codeset);
		
	}

	@Override
	public CodeSetTreeWalker getCodeSetTreeWalker(Company company) {
		CodeSetTreeWalker treeWalker ;
		
		treeWalker = codeSetDao.getCodeSetTreeWalker(company.getModelObjectType(), company.getCompanyId());
		
		return treeWalker;
	}

	@Override
	public CodeSet getCodeSet(long codeSetId) throws CodeSetNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
