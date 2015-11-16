package com.podosoftware.competency.competency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.competency.dao.CompetencyDao;

import architecture.common.user.Company;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultCompetencyManager implements CompetencyManager {

	private CompetencyDao competencyDao ;
	
	protected Cache competencyCache;
	
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
	
	private Competency getCompetencyInCache(Long competencyId){
		if( competencyCache.get(competencyId) == null )
			return null;		
		return (Competency)competencyCache.get(competencyId).getValue(); 
	}
	
	private void updateCache(Competency competency){
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
		return null;
	}

}