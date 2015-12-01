package com.podosoftware.competency.competency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.competency.dao.CompetencyDao;
import com.podosoftware.competency.job.Classification;

import architecture.common.user.Company;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultCompetencyManager implements CompetencyManager {

	private CompetencyDao competencyDao ;
	
	protected Cache competencyCache;
	
	protected Cache essentialElementCache;
	
	
	
	public DefaultCompetencyManager() {
	}

	public CompetencyDao getCompetencyDao() {
		return competencyDao;
	}

	public void setCompetencyDao(CompetencyDao competencyDao) {
		this.competencyDao = competencyDao;
	}

	public Cache getCompetencyCache() {
		return competencyCache;
	}

	public void setCompetencyCache(Cache competencyCache) {
		this.competencyCache = competencyCache;
	}
	
	public Cache getEssentialElementCache() {
		return essentialElementCache;
	}

	public void setEssentialElementCache(Cache essentialElementCache) {
		this.essentialElementCache = essentialElementCache;
	}

	private Competency getCompetencyInCache(Long competencyId){
		if( competencyCache.get(competencyId) == null )
			return null;		
		return (Competency)competencyCache.get(competencyId).getValue(); 
	}
	
	private void updateCache(Competency competency){
		if( competencyCache.get(competency.getCompetencyId()) != null ){
			competencyCache.remove(competency.getCompetencyId());
		}
		competencyCache.put(new Element(competency.getCompetencyId(), competency));
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Competency createCompetency(Company company, String name) throws CompetencyAlreadyExistsException {
		Competency competency = new DefaultCompetency();
		competency.setObjectType(1);
		competency.setObjectId(company.getCompanyId());
		competency.setName(name);
		competencyDao.createCompetency(competency);		
		return competency;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Competency createCompetency(Company company, String name, String description)
			throws CompetencyAlreadyExistsException {
		Competency competency = new DefaultCompetency();
		competency.setObjectType(1);
		competency.setObjectId(company.getCompanyId());
		competency.setName(name);
		competency.setDescription(description);
		competencyDao.createCompetency(competency);
		return competency;
	}


	public int getCompetencyCount(Company company){
		return competencyDao.getCompetencyCount(company);
	}
	
	public List<Competency> getCompetencies(Company company) {			
		List<Long> ids = competencyDao.getCompetencyIds(company);
		ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getCompetency(id));
			} catch (CompetencyNotFoundException e) {
				
			}			
		}		
		return list;
	}
	
	public List<Competency> getCompetencies(Company company, int startIndex, int numResults) {		
		List<Long> ids = competencyDao.getCompetencyIds(company, startIndex, numResults);
		ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getCompetency(id));
			} catch (CompetencyNotFoundException e) {
				
			}			
		}		
		return list;
	}
	

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateCompetency(Competency competency) throws CompetencyNotFoundException {
		if( competency.getCompetencyId() > 0)
		{
			competencyDao.updateCompetency(competency);		
			updateCache( getCompetency(competency.getCompetencyId() ));
		}
	}

	public Competency getCompetency(long competencyId) throws CompetencyNotFoundException {		
		Competency competency = getCompetencyInCache(competencyId);
		if(competency == null){
			competency = competencyDao.getCompetencyById(competencyId);			
			if( competency == null ){				
				throw new CompetencyNotFoundException();
			}
			updateCache(competency);
		}
		return competency;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(Competency competency) throws CompetencyNotFoundException {
		if(competency.getCompetencyId() > 0 ){
			updateCompetency(competency);
		}else{
			competencyDao.createCompetency(competency);
		}
		
	}

	
	public EssentialElement getEssentialElement(long essentialElementId) throws EssentialElementNotFoundException {		
		
		EssentialElement essentialElement = getEssentialElementInCache(essentialElementId);
		if(essentialElement == null){
			essentialElement = competencyDao.getEssentialElementById(essentialElementId);			
			if( essentialElement == null ){				
				throw new EssentialElementNotFoundException();
			}
			updateCache(essentialElement);
		}
		return essentialElement;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void createEssentialElement(EssentialElement essentialElement) throws CompetencyNotFoundException {
		if(essentialElement.getCompetencyId() < 1){
			throw new CompetencyNotFoundException();
		}
		getCompetency(essentialElement.getCompetencyId());
		competencyDao.createEssentialElement(essentialElement);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateEssentialElement(EssentialElement essentialElement) throws EssentialElementNotFoundException {
		if( essentialElement.getEssentialElementId() > 0)
		{
			competencyDao.updateEssentialElement(essentialElement);		
			updateCache( getEssentialElement(essentialElement.getEssentialElementId() ));
		}			
	}
	
	private EssentialElement getEssentialElementInCache(Long essentialElementId){
		if( essentialElementCache.get(essentialElementId) == null )
			return null;		
		return (EssentialElement)essentialElementCache.get(essentialElementId).getValue(); 
	}
	
	private void updateCache( EssentialElement essentialElement){
		if( competencyCache.get(essentialElement.getEssentialElementId()) != null ){
			competencyCache.remove(essentialElement.getEssentialElementId());
		}
		essentialElementCache.put(new Element(essentialElement.getEssentialElementId(), essentialElement));
	}

	@Override
	public List<EssentialElement> getEssentialElements(Competency competency) {
		List<Long> ids = competencyDao.getEssentialElementIds(competency);
		ArrayList<EssentialElement> list = new ArrayList<EssentialElement>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getEssentialElement(id));
			} catch (EssentialElementNotFoundException e) {
				
			}			
		}		
		return list;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveOrUpdate(EssentialElement essentialElement) throws EssentialElementNotFoundException {
		if(essentialElement.getEssentialElementId() > 0 ){
			updateEssentialElement(essentialElement);
		}else{
			competencyDao.createEssentialElement(essentialElement);
		}		
	}

 
	public int getCompetencyCount(Company company, Classification classify) {
		return competencyDao.getCompetencyCount(company, classify);
	}
 
	public List<Competency> getCompetencies(Company company, Classification classify) {
		List<Long> ids = competencyDao.getCompetencyIds(company, classify);
		ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getCompetency(id));
			} catch (CompetencyNotFoundException e) {
				
			}			
		}		
		return list;
	}

 
	public List<Competency> getCompetencies(Company company, Classification classify, int startIndex, int numResults) {
		List<Long> ids = competencyDao.getCompetencyIds(company, classify, startIndex, numResults);
		ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getCompetency(id));
			} catch (CompetencyNotFoundException e) {
				
			}			
		}		
		return list;
	}
	

}