package com.podosoftware.competency.assessment;

import architecture.common.cache.Cacheable;

/**
 * 
 *진단 평정 척도
 * 
 * @author donghyuck
 *
 */
public interface RatingLevel extends Cacheable {
			
	/**
	 * 
	 * @return 진단 평정 척도 제도 ID
	 */
	public Long getRatingSchemeId();

	/**
	 * 
	 * @param ratingSchemeId 진단 평정 척도 제도 ID
	 */
	public void setRatingSchemeId(Long ratingSchemeId) ;
	
	/**
	 * 진단 평정 척도 레벨 ID
	 * @return
	 */
	public Long getRatingLevelId();
	
	/**
	 * 
	 * @param ratingLevelId 진단 평정 척도 레벨 ID
	 */
	public void setRatingLevelId(Long ratingLevelId);
	
	/**
	 * 
	 * @return 진단 평정 척도 레벨 예시 
	 */
	public String getTitle();
	
	/**
	 * 
	 * @param title 진단 평정 척도 레벨 예시
	 */
	public void setTitle(String title);
	
	/**
	 * 
	 * @return 진단 평정 척도점
	 */
	public int getScore();
	
	/**
	 * 
	 * @param score 진단 평정 척도점
	 */
	public void setScore(int score);
	
}
