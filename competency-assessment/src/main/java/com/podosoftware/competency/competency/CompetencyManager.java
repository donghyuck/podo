package com.podosoftware.competency.competency;

import java.util.List;

import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;

import architecture.common.user.Company;

public interface CompetencyManager {
	
	
	public abstract Competency createCompetency(int objectType, long objectId, CompetencyType competencyType) throws CompetencyAlreadyExistsException;
		
	public abstract Competency createCompetency(Company company, String name) throws CompetencyAlreadyExistsException;
	
	public abstract Competency createCompetency(Company company, String name, String description) throws CompetencyAlreadyExistsException;

	
	public abstract List<Competency> getCompetencies(Company company) ;
	
	public abstract List<Competency> getCompetencies(Company company, int startIndex, int numResults) ;
	
	public abstract int getCompetencyCount(Company company) ;

	public abstract List<Competency> getCompetencies(Company company, CompetencyType competencyType ) ;
	
	public abstract List<Competency> getCompetencies(Company company, CompetencyType competencyType, int startIndex, int numResults) ;
	
	public abstract int getCompetencyCount(Company company, CompetencyType competencyType) ;	
	
	public abstract int getCompetencyCount(Company company, Classification classify);
	
	public abstract List<Competency> getCompetencies(Company company, Classification classify) ;
	
	public abstract List<Competency> getCompetencies(Company company, Classification classify, int startIndex, int numResults) ;	
	
	
		
	/**
	 * 
	 * @param company
	 * @param groupCode
	 * @param level
	 * @param name
	 * @param classify
	 * @return
	 */
	public abstract List<Competency> findCompetency(Company company, String groupCode, int level, String name, Classification classify, long jobId, int startIndex, int numResults) ;		
		
	public abstract List<Competency> findCompetency(Company company, String groupCode, int level, String name, Classification classify, long jobId);
	
	/**
	 * 
	 * @param company
	 * @param groupCode
	 * @param level
	 * @param name
	 * @param classify
	 * @return
	 */
	public abstract int getCompetencyCount(Company company, String groupCode, int level, String name, Classification classify, long jobId);
	
	
	
	
	public abstract void updateCompetency(Competency competency) throws CompetencyNotFoundException ;
	
	public abstract void saveOrUpdate(Competency competency) throws CompetencyNotFoundException ;
	
	public abstract Competency getCompetency(long competencyId) throws CompetencyNotFoundException;
		
	public abstract int getCompetencyCount(Job job);
	
	public abstract List<Competency> getCompetencies(Job job) ;
	
	public abstract List<Competency> getCompetencies(Job job, int startIndex, int numResults) ;		
		
	
	public abstract List<EssentialElement> getEssentialElements(Competency competency) ;
	
	public abstract void createEssentialElement( EssentialElement essentialElement) throws CompetencyNotFoundException ;
	
	public abstract void updateEssentialElement( EssentialElement essentialElement) throws EssentialElementNotFoundException;
	
	public abstract void saveOrUpdate(EssentialElement essentialElement) throws EssentialElementNotFoundException ;
		
	
	public abstract List<PerformanceCriteria> getPerformanceCriterias( int objectType, long objectId );
	
	public abstract PerformanceCriteria getPerformanceCriteria(long performanceCriteriaId) throws PerformanceCriteriaNotFoundException;
	
	public abstract void saveOrUpdate(PerformanceCriteria performanceCriteria) throws PerformanceCriteriaNotFoundException ;
	
	public abstract void saveOrUpdatePerformanceCriterias(List<PerformanceCriteria> performanceCriterias);
		
	public abstract void removePerformanceCriterias(List<PerformanceCriteria> performanceCriterias);
	
	
	/**
	 * 능력 목록을 리턴한다.
	 * 
	 * @param objectType
	 * @param objectId
	 * @return
	 */
	public abstract List<Ability> getAbilities( int objectType, long objectId );
		
	public abstract Ability getAbility(long abilityId) throws AbilityNotFoundException;
	
	public abstract void saveOrUpdateAbility(Ability ability) throws AbilityNotFoundException ;
	
	public abstract void saveOrUpdateAblilities(List<Ability> abilities);
	
	public abstract void removeAbilities(List<Ability> abilities);	
	
}
