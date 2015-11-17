package com.podosoftware.competency.competency;

import java.util.List;

import architecture.common.user.Company;

public interface CompetencyManager {
		
	public abstract Competency createCompetency(Company company, String name) throws CompetencyAlreadyExistsException;
	
	public abstract Competency createCompetency(Company company, String name, String description) throws CompetencyAlreadyExistsException;

	public abstract List<Competency> getCompetencies(Company company) ;
	
	public abstract List<Competency> getCompetencies(Company company, int startIndex, int numResults) ;
	
	public abstract int getCompetencyCount(Company company) ;
		
	public abstract void updateCompetency(Competency competency) throws CompetencyNotFoundException ;
	
	public abstract void saveOrUpdate(Competency competency) ;
	
	public Competency getCompetency(long competencyId) throws CompetencyNotFoundException;
	
}
