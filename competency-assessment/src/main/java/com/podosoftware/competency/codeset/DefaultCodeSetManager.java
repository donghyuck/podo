package com.podosoftware.competency.codeset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.podosoftware.competency.codeset.dao.CodeSetDao;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.UserManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultCodeSetManager implements CodeSetManager {
	
	public static final CodeSet ROOT_CODE_SET = new DefaultCodeSet();
	private CodeSetDao codeSetDao;
	private UserManager userManager;
	private CompanyManager companyManager;
	protected Cache codeSetCache;

	@Override
	public CodeSet getCodeSet(long codeSetId) throws CodeSetNotFoundException {
		CodeSet codeset = getCodeSetInCache(codeSetId);		
		if (codeset == null) {			
			codeset = codeSetDao.getCodeSetById(codeSetId);
			if(codeset == null)
				throw new CodeSetNotFoundException();
			codeSetCache.put(new Element(codeSetId, codeset));
		}
		return codeset;
	}
	
	protected CodeSet getCodeSetInCache(long codeSetId){
		if( codeSetCache.get(codeSetId) != null)
			return  (CodeSet) codeSetCache.get( codeSetId ).getValue();
		else 
			return null;
	}


	public List<CodeSet> getCodeSets(CodeSet codeset) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CodeSet> getCodeSets(Company company) {
		List<Long> codesetIds =  codeSetDao.getCodeSetIds(1, company.getCompanyId());
		List<CodeSet> codesets = new ArrayList<CodeSet>(codesetIds.size());		
		for(long codesetId:codesetIds){
			CodeSet codeset;
			try {
				codeset = getCodeSet(codesetId);
				codesets.add(codeset);
			} catch (CodeSetNotFoundException e) {				
			}			
		}		
		return codesets;
	}
	
	@Override
	public int getCodeSetCount(Company company) {	
		return codeSetDao.getCodeSetCount(1, company.getCompanyId());
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
		}else{
			codeset.setCodeSetId(-1L);
		}
		codeSetDao.saveOrUpdateCodeSet(codeset);
		
	}

	@Override
	public CodeSetTreeWalker getCodeSetTreeWalker(Company company) {
		CodeSetTreeWalker treeWalker ;
		
		treeWalker = codeSetDao.getCodeSetTreeWalker(company.getModelObjectType(), company.getCompanyId());
		
		return treeWalker;
	}

	
	public CodeSetDao getCodeSetDao() {
		return codeSetDao;
	}

	public void setCodeSetDao(CodeSetDao codeSetDao) {
		this.codeSetDao = codeSetDao;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public Cache getCodeSetCache() {
		return codeSetCache;
	}

	public void setCodeSetCache(Cache codeSetCache) {
		this.codeSetCache = codeSetCache;
	}
	
	
	

}
