package com.podosoftware.competency.competency.dao;

import java.util.List;
import java.util.Map;

import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;

import architecture.common.user.Company;

public interface CompetencyDao {

	public abstract Competency createCompetency(Competency competency);
	
	public abstract Competency updateCompetency(Competency competency);
	
	public abstract Competency getCompetencyById(long competencyId);
	
	public abstract Competency getCompetencyByName(Company company, String name);
	
	public abstract void deleteCompetency(Competency competency);
	
	public abstract int getCompetencyCount(Company company);
	
	public abstract List<Long> getCompetencyIds(Company company);
	
	public abstract Map<String, Long> getCompetencyIdsWithCompetencyUnitCode(int objectType, long objectId);
	
	public abstract List<Long> getCompetencyIds(Company company, int startIndex, int numResults);
	
	/**
	 * 직무 분류에 따른 역량 수 리턴. 
	 * @param company
	 * @param classify
	 * @return
	 */
	public abstract int getCompetencyCount(Company company, Classification classify);
	
	/**
	 * 직무분류에 따른 역량 아이디 리턴.
	 * @param company
	 * @param classify
	 * @return
	 */
	public abstract List<Long> getCompetencyIds(Company company, Classification classify);
	
	/**
	 * 직무분류에 따른 역량 아이디 리턴.
	 * @param company
	 * @param classify
	 * @param startIndex
	 * @param numResults
	 * @return
	 */
	public abstract List<Long> getCompetencyIds(Company company, Classification classify, int startIndex, int numResults);
	
	
	/**
	 * 직업에 따른 역량 수 리턴. 
	 * @param job
	 * @return
	 */
	public abstract int getCompetencyCount(Job job);
	
	/**
	 * 직업에 따른 역량 수 리턴. 
	 * @param job
	 * @return
	 */
	public abstract List<Long> getCompetencyIds(Job job);
	
	/**
	 * 직업에 따른 역량 수 리턴. 
	 * @param job
	 * @param startIndex
	 * @param numResults
	 * @return
	 */
	public abstract List<Long> getCompetencyIds(Job job, int startIndex, int numResults);
	
	
	
	public abstract void createEssentialElement(EssentialElement essentialElement);
	
	public abstract void updateEssentialElement(EssentialElement essentialElement);
	
	public abstract EssentialElement getEssentialElementById(long essentialElementId);
		
	public abstract List<Long> getEssentialElementIds(Competency competency);
		
	public abstract Long nextCompetencyId();
	
	public abstract Long nextEssentialElementId();
	
	public abstract void batchInsertCompetency(List<Competency> competencies);
	
	public abstract void batchInsertEssentialElement(List<EssentialElement> competencies);
	
}
