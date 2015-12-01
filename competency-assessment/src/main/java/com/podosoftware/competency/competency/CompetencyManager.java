package com.podosoftware.competency.competency;

import java.util.List;

import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;

import architecture.common.user.Company;

public interface CompetencyManager {
		
	public abstract Competency createCompetency(Company company, String name) throws CompetencyAlreadyExistsException;
	
	public abstract Competency createCompetency(Company company, String name, String description) throws CompetencyAlreadyExistsException;

	
	public abstract List<Competency> getCompetencies(Company company) ;
	
	public abstract List<Competency> getCompetencies(Company company, int startIndex, int numResults) ;
	
	public abstract int getCompetencyCount(Company company) ;
	
	
	
	public abstract int getCompetencyCount(Company company, Classification classify);
	
	public abstract List<Competency> getCompetencies(Company company, Classification classify) ;
	
	public abstract List<Competency> getCompetencies(Company company, Classification classify, int startIndex, int numResults) ;		

	
	
	public abstract int getCompetencyCount(Job job);
	
	public abstract List<Competency> getCompetencies(Job job) ;
	
	public abstract List<Competency> getCompetencies(Job job, int startIndex, int numResults) ;		
	
	
		
	public abstract void updateCompetency(Competency competency) throws CompetencyNotFoundException ;
	
	public abstract void saveOrUpdate(Competency competency) throws CompetencyNotFoundException ;
	
	public abstract Competency getCompetency(long competencyId) throws CompetencyNotFoundException;
		
	
	public abstract List<EssentialElement> getEssentialElements(Competency competency) ;
	
	public abstract void createEssentialElement( EssentialElement essentialElement) throws CompetencyNotFoundException ;
	
	public abstract void updateEssentialElement( EssentialElement essentialElement) throws EssentialElementNotFoundException;
	
	public abstract void saveOrUpdate(EssentialElement essentialElement) throws EssentialElementNotFoundException ;

	
}
