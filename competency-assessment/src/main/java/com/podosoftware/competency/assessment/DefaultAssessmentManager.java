package com.podosoftware.competency.assessment;

import java.util.ArrayList;
import java.util.List;

import com.podosoftware.competency.assessment.dao.AssessmentDao;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultAssessmentManager implements AssessmentManager {

	private AssessmentDao assessmentDao;
	
	protected Cache ratingSchemeCache;
	
	protected Cache assessmentSchemeCache;
	
	protected Cache assessmentCache;
	
	public DefaultAssessmentManager() {
		
	}

	public AssessmentDao getAssessmentDao() {
		return assessmentDao;
	}

	public void setAssessmentDao(AssessmentDao accessmentDao) {
		this.assessmentDao = accessmentDao;
	}
	
	public Cache getAssessmentSchemeCache() {
		return assessmentSchemeCache;
	}

	public void setAssessmentSchemeCache(Cache assessmentSchemeCache) {
		this.assessmentSchemeCache = assessmentSchemeCache;
	}

	public Cache getAssessmentCache() {
		return assessmentCache;
	}

	public void setAssessmentCache(Cache assessmentCache) {
		this.assessmentCache = assessmentCache;
	}

	public Cache getRatingSchemeCache() {
		return ratingSchemeCache;
	}

	public void setRatingSchemeCache(Cache ratingSchemeCache) {
		this.ratingSchemeCache = ratingSchemeCache;
	}
 
	public List<RatingScheme> getRatingSchemes(int objectType, long objectId) {
		List<Long> ids = assessmentDao.getRatingSchemeIds(objectType, objectId);		
		return loadRatingSchemes(ids);
	}
 
	public void saveOrUpdateRatingScheme(RatingScheme ratingScheme) {
		if(ratingScheme.getRatingSchemeId() > 0 ){
			if( ratingSchemeCache.get(ratingScheme.getRatingSchemeId()) != null ){
				ratingSchemeCache.remove(ratingScheme.getRatingSchemeId());
			}
		}
		assessmentDao.saveOrUpdateRatingScheme(ratingScheme);
	}

	public RatingScheme getRatingScheme(long ratingSchemeId) throws RatingSchemeNotFoundException {
		RatingScheme scheme = getRatingSchemeInCache(ratingSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getRatingSchemeById(ratingSchemeId);			
			if( scheme == null ){				
				throw new RatingSchemeNotFoundException();
			}
			updateCache(scheme);
		}
		return scheme;	
	}
	 
	public int getRatingSchemeCount(int objectType, long objectId) {
		return assessmentDao.getRatingSchemeCount(objectType, objectId);
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

	@Override
	public List<AssessmentScheme> getAssessmentSchemes(int objectType, long objectId) {
		List<Long> ids = assessmentDao.getAssessmentSchemeIds(objectType, objectId);		
		return loadAssessmentSchemes(ids);
	}

	@Override
	public int getAssessmentSchemeCount(int objectType, long objectId) {
		return assessmentDao.getAssessmentSchemeCount(objectType, objectId);
	}

	@Override
	public void saveOrUpdateAssessmentScheme(AssessmentScheme assessmentScheme) {
		if(assessmentScheme.getAssessmentSchemeId() > 0 ){
			if( assessmentSchemeCache.get(assessmentScheme.getAssessmentSchemeId()) != null ){
				assessmentSchemeCache.remove(assessmentScheme.getAssessmentSchemeId());
			}
		}
		assessmentDao.saveOrUpdateAssessmentScheme(assessmentScheme);		
	}

	public AssessmentScheme getAssessmentScheme(long assessmentSchemeId) throws AssessmentSchemeNotFoundException {
		AssessmentScheme scheme = getAssessmentSchemeInCache(assessmentSchemeId);
		if(scheme == null){
			scheme = assessmentDao.getAssessmentSchemeById(assessmentSchemeId);			
			
			RatingScheme ratingScheme;
			try {
				ratingScheme = getRatingScheme(scheme.getRatingScheme().getRatingSchemeId());
				scheme.setRatingScheme(ratingScheme);
			} catch (RatingSchemeNotFoundException e) {
			}
			if( scheme == null ){				
				throw new AssessmentSchemeNotFoundException();
			}
			updateCache(scheme);
		}
		return scheme;	
	}

	private List<AssessmentScheme> loadAssessmentSchemes(List<Long> ids){
		ArrayList<AssessmentScheme> list = new ArrayList<AssessmentScheme>(ids.size());
		for( Long id : ids ){			
			try {
				list.add(getAssessmentScheme(id));
			} catch (AssessmentSchemeNotFoundException e) {}			
		}		
		return list;		
	}
	
	private void updateCache( AssessmentScheme assessmentScheme){
		if( assessmentSchemeCache.get(assessmentScheme.getAssessmentSchemeId()) != null ){
			assessmentSchemeCache.remove(assessmentScheme.getAssessmentSchemeId());
		}
		assessmentSchemeCache.put(new Element(assessmentScheme.getAssessmentSchemeId(), assessmentScheme));
	}
	
	private AssessmentScheme getAssessmentSchemeInCache(long assessmentSchemeId){
		if(assessmentSchemeCache.get(assessmentSchemeId)!=null){
			return (AssessmentScheme) assessmentSchemeCache.get(assessmentSchemeId).getValue();
		}
		return null;
	}
}
