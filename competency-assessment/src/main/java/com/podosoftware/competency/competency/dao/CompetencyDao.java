package com.podosoftware.competency.competency.dao;

import java.util.List;

import com.podosoftware.competency.competency.Competency;

import architecture.common.user.Company;

public interface CompetencyDao {

	public abstract Competency createCompetency(Competency competency);
	
	public abstract Competency updateCompetency(Competency competency);
	
	public abstract Competency getCompetencyById(long competencyId);
	
	public abstract Competency getCompetencyByName(Company company, String name);
	
	public abstract void deleteCompetency(Competency competency);
	
	public abstract int getCompetencyCount(Company company);
	
	public abstract List<Long> getCompetencyIds(Company company);
	
	public abstract List<Long> getCompetencyIds(Company company, int startIndex, int numResults);
	
}
