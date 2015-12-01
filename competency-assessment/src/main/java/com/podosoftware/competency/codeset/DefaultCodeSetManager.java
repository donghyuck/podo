package com.podosoftware.competency.codeset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.codeset.dao.CodeSetDao;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.DefaultCompetency;
import com.podosoftware.competency.competency.DefaultEssentialElement;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.competency.dao.CompetencyDao;
import com.podosoftware.competency.job.DefaultClassification;
import com.podosoftware.competency.job.DefaultJob;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobCompetencyRelationship;
import com.podosoftware.competency.job.dao.JobDao;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.UserManager;
import architecture.common.util.LockUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultCodeSetManager implements CodeSetManager {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static final CodeSet ROOT_CODE_SET = new DefaultCodeSet();
	
	private CodeSetDao codeSetDao;
	
	private JobDao jobDao; 
	
	private CompetencyDao competencyDao; 
	
	private UserManager userManager;
	private CompanyManager companyManager;
	protected Cache codeSetCache;
	private Cache treeWalkerCache;
		
	
	public CompetencyDao getCompetencyDao() {
		return competencyDao;
	}

	public void setCompetencyDao(CompetencyDao competencyDao) {
		this.competencyDao = competencyDao;
	}

	public JobDao getJobDao() {
		return jobDao;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public Cache getTreeWalkerCache() {
		return treeWalkerCache;
	}

	public void setTreeWalkerCache(Cache treeWalkerCache) {
		this.treeWalkerCache = treeWalkerCache;
	}

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
		List<Long> codesetIds =  codeSetDao.getCodeSetIds(codeset.getObjectType(), codeset.getObjectId(), codeset.getCodeSetId());
		List<CodeSet> codesets = new ArrayList<CodeSet>(codesetIds.size());		
		for(long codesetId:codesetIds){
			CodeSet item;
			try {
				item = getCodeSet(codesetId);
				codesets.add(item);
			} catch (CodeSetNotFoundException e) {				
			}			
		}		
		return codesets;
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
		return codeSetDao.getCodeSetCount(codeset.getObjectType(), codeset.getObjectId(), codeset.getCodeSetId());
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public CodeSet createCodeSet(CodeSet orgCodeset, String name, String description) {		
		CodeSet codeset = new DefaultCodeSet();
		codeset.setObjectType(orgCodeset.getObjectType());
		codeset.setObjectId(orgCodeset.getObjectId());
		codeset.setParentCodeSetId(orgCodeset.getCodeSetId());
		codeset.setName(name);
		codeset.setDescription(description);				
		return codeset;
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(CodeSet codeset) {
		if( codeset.getCodeSetId() > 0)
		{			
			Date now = new Date();
			codeset.setModifiedDate(now);		
			clearCodeSetCache(codeset);
		}else{
			codeset.setCodeSetId(-1L);
		}
		codeSetDao.saveOrUpdateCodeSet(codeset);
		
	}


	private void clearCodeSetCache(long codeSetId){
		if(codeSetCache.get(codeSetId) != null)
			codeSetCache.remove( codeSetId);
	}
	
	private void clearCodeSetCache(CodeSet codeSet){
		String key = getTreeWalkerCacheKey( codeSet.getObjectType(), codeSet.getObjectId());
		clearCodeSetCache(codeSet.getCodeSetId()); 
		synchronized(key){
			treeWalkerCache.remove(key);
		}
	}
	
	public CodeSetTreeWalker getCodeSetTreeWalker(Company company) {
		CodeSetTreeWalker treeWalker ;
		treeWalker = codeSetDao.getCodeSetTreeWalker(company.getModelObjectType(), company.getCompanyId());
		return treeWalker;
	}
	
	public CodeSetTreeWalker getCodeSetTreeWalker(int objectType, long objectId)
	{
		String key = getTreeWalkerCacheKey(objectType, objectId);
		CodeSetTreeWalker treeWalker ;
		if(treeWalkerCache.get(key) != null ){
			treeWalker = (CodeSetTreeWalker)treeWalkerCache.get(key).getValue();
		}else{
			synchronized(key){
				treeWalker = codeSetDao.getCodeSetTreeWalker(objectType, objectId);
				treeWalkerCache.put(new Element(key, treeWalker));
			}
		}				
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


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(List<CodeSet> codesets) {
		for(CodeSet code : codesets){
			if(code.getCodeSetId() > 0 )
			{
				clearCodeSetCache(code.getCodeSetId());
			}
		}
		this.codeSetDao.saveOrUpdateCodeSet(codesets);		
	}


	 private static String getTreeWalkerCacheKey( int objectType, long objectId) {
		 return LockUtils.intern((new StringBuilder("codesetTreeWalker-")).append(objectType).append("-").append(objectId).toString());
	 }

	@Override
	public List<CodeSet> getRecrusiveCodesets(CodeSet codeset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRecrusiveCodeSetCount(CodeSet codeset) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	/**
	 * 
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void batchUpdate(CodeSet codeSet, List<CodeItem> items) {
		List<CodeSet> list = new ArrayList<CodeSet>();
		List<Job> jobs = new ArrayList<Job>();
		List<Competency> competencies = new ArrayList<Competency>();
		List<JobCompetencyRelationship> relationships = new ArrayList<JobCompetencyRelationship>();
		for(CodeItem item : items)
		{
			DefaultCodeSet newCodeSet = new DefaultCodeSet();
			newCodeSet.setCodeSetId(codeSetDao.nextCodeSetId());
			newCodeSet.setObjectType(codeSet.getObjectType());
			newCodeSet.setObjectId(codeSet.getObjectId());
			newCodeSet.setParentCodeSetId(codeSet.getCodeSetId());
			newCodeSet.setCode(item.getCode());
			newCodeSet.setName(item.getName());		
			list.add(newCodeSet);			
			for(CodeItem item2 : item.getItems().values()){				
				DefaultCodeSet newCodeSet2 = new DefaultCodeSet();
				newCodeSet2.setCodeSetId(codeSetDao.nextCodeSetId());
				newCodeSet2.setObjectType(newCodeSet.getObjectType());
				newCodeSet2.setObjectId(newCodeSet.getObjectId());
				newCodeSet2.setParentCodeSetId(newCodeSet.getCodeSetId());
				newCodeSet2.setCode(item2.getCode());
				newCodeSet2.setName(item2.getName());		
				list.add(newCodeSet2);					
				for(CodeItem item3 :item2.getItems().values()){
					DefaultCodeSet newCodeSet3 = new DefaultCodeSet();
					newCodeSet3.setCodeSetId(codeSetDao.nextCodeSetId());
					newCodeSet3.setObjectType(newCodeSet2.getObjectType());
					newCodeSet3.setObjectId(newCodeSet2.getObjectId());
					newCodeSet3.setParentCodeSetId(newCodeSet2.getCodeSetId());
					newCodeSet3.setCode(item3.getCode());
					newCodeSet3.setName(item3.getName());		
					list.add(newCodeSet3);					
					for( CodeItem item4 : item3.getItems().values()){		
						Job job = new DefaultJob();
						job.setJobId(jobDao.nextJobId());
						job.setObjectType(codeSet.getObjectType());
						job.setObjectId(codeSet.getObjectId());
						job.setClassification(new DefaultClassification(newCodeSet.getCodeSetId(), newCodeSet2.getCodeSetId(), newCodeSet3.getCodeSetId() ));
						job.setName(item4.getName());
						jobs.add(job);						
						for( CodeItem item5 : item4.getItems().values()){		
							Competency competency = new DefaultCompetency();
							competency.setCompetencyId(competencyDao.nextCompetencyId());
							competency.setObjectId(job.getObjectId());
							competency.setObjectType(job.getObjectType());
							competency.setName(item5.getName());
							competency.setLevel(Integer.parseInt(item5.getEtc1()));
							competency.setCompetencyUnitCode(item5.getCode());						
							competencies.add(competency);
							relationships.add(new JobCompetencyRelationship(job.getObjectType(),job.getObjectId(),job.getJobId(), competency.getCompetencyId()) );
						}
					}
				}
			}
		}
		
		String key = getTreeWalkerCacheKey( codeSet.getObjectType(), codeSet.getObjectId());
		synchronized(key){
			treeWalkerCache.remove(key);
		}		
		
		log.debug("batch processing codeset:" + list.size() );
		codeSetDao.batchInsertCodeSet(list);
		
		if(jobDao != null){			
			log.debug("batch processing jobs:" + jobs.size() );
			jobDao.batchInsertJob(jobs);			
			log.debug("batch processing compentecy:" + jobs.size() );
			competencyDao.batchInsertCompetency(competencies);			
			log.debug("batch processing job and competency relations:" + jobs.size() );
			jobDao.batchInsertJobCompetencyRelationship(relationships);
		}		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void batchUpdateForEssentialElement(CodeSet codeSet, List<CodeItem> items) {
		List<EssentialElement> elements = new ArrayList<EssentialElement>();
		Map<String, Long> ids =  competencyDao.getCompetencyIdsWithCompetencyUnitCode(codeSet.getObjectType(), codeSet.getObjectId());
		for( CodeItem code : items){
			Long competencyId = ids.get(code.getCode());
			if(StringUtils.equals(code.getType(), "element") && competencyId != null) {
				EssentialElement element = new DefaultEssentialElement();
				//element.setEssentialElementId(competencyDao.nextEssentialElementId());
				element.setCompetencyId(competencyId);
				element.setName(code.getName());
				element.setLevel(Integer.parseInt(code.getEtc1()));
				elements.add(element);
			}
		}
		competencyDao.batchInsertEssentialElement(elements);
		
	}
}
