package com.podosoftware.competency.assessment;

import java.util.ArrayList;
import java.util.List;

import com.podosoftware.competency.assessment.dao.AccessmentDao;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyNotFoundException;
import com.podosoftware.competency.competency.EssentialElement;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultAssessmentManager implements AssessmentManager {

	private AccessmentDao accessmentDao;
	
	protected Cache ratingSchemeCache;
	
	public DefaultAssessmentManager() {
		
	}

	public AccessmentDao getAccessmentDao() {
		return accessmentDao;
	}

	public void setAccessmentDao(AccessmentDao accessmentDao) {
		this.accessmentDao = accessmentDao;
	}
	
	public Cache getRatingSchemeCache() {
		return ratingSchemeCache;
	}

	public void setRatingSchemeCache(Cache ratingSchemeCache) {
		this.ratingSchemeCache = ratingSchemeCache;
	}
 
	public List<RatingScheme> getRatingSchemes(int objectType, long objectId) {
		List<Long> ids = accessmentDao.getRatingSchemeIds(objectType, objectId);		
		return loadRatingSchemes(ids);
	}
 
	public void saveOrUpdateRatingScheme(RatingScheme ratingScheme) {
		if(ratingScheme.getRatingSchemeId() > 0 ){
			if( ratingSchemeCache.get(ratingScheme.getRatingSchemeId()) != null ){
				ratingSchemeCache.remove(ratingScheme.getRatingSchemeId());
			}
		}
		accessmentDao.saveOrUpdateRatingScheme(ratingScheme);
	}

	public RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException {
		RatingScheme scheme = getRatingSchemeInCache(ratingSchemeId);
		if(scheme == null){
			scheme = accessmentDao.getRatingSchemeById(ratingSchemeId);			
			if( scheme == null ){				
				throw new RatingSchemeNotFoundException();
			}
			updateCache(scheme);
		}
		return scheme;	
	}
	 
	public int getRatingSchemeCount(int objectType, long objectId) {
		return accessmentDao.getRatingSchemeCount(objectType, objectId);
	}
	
	private void updateCache( RatingScheme ratingScheme){
		if( ratingSchemeCache.get(ratingScheme.getRatingSchemeId()) != null ){
			ratingSchemeCache.remove(ratingScheme.getRatingSchemeId());
		}
		ratingSchemeCache.put(new Element(ratingScheme.getRatingSchemeId(), ratingScheme));
	}
	
	private RatingScheme getRatingSchemeInCache(long ratingSchemeId){
		if(ratingSchemeCache.get(ratingSchemeId)!=null){
			return (RatingScheme) ratingSchemeCache.get(ratingSchemeId).getValue();
		}
		return null;
	}
	
	private List<RatingScheme> loadRatingSchemes(List<Long> ids){
		ArrayList<RatingScheme> list = new ArrayList<RatingScheme>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getRatingScheme(id));
			} catch (RatingSchemeNotFoundException e) {}			
		}		
		return list;		
	}

	
}
