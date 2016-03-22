package com.podosoftware.competency.competency;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.podosoftware.competency.competency.dao.CompetencyDao;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobLevel;

import architecture.common.user.Company;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultCompetencyManager implements CompetencyManager {

    private CompetencyDao competencyDao;

    protected Cache competencyCache;

    protected Cache essentialElementCache;

    protected Cache performanceCriteriaCache;

    protected Cache performanceCriteriaIdsCache;

    protected Cache abilityCache;

    public DefaultCompetencyManager() {
    }

    public CompetencyDao getCompetencyDao() {
	return competencyDao;
    }

    public void setCompetencyDao(CompetencyDao competencyDao) {
	this.competencyDao = competencyDao;
    }

    public Cache getAbilityCache() {
	return abilityCache;
    }

    public void setAbilityCache(Cache abilityCache) {
	this.abilityCache = abilityCache;
    }

    public Cache getPerformanceCriteriaCache() {
	return performanceCriteriaCache;
    }

    public void setPerformanceCriteriaCache(Cache performanceCriteriaCache) {
	this.performanceCriteriaCache = performanceCriteriaCache;
    }

    public Cache getPerformanceCriteriaIdsCache() {
	return performanceCriteriaIdsCache;
    }

    public void setPerformanceCriteriaIdsCache(Cache performanceCriteriaIdsCache) {
	this.performanceCriteriaIdsCache = performanceCriteriaIdsCache;
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

    private Competency getCompetencyInCache(Long competencyId) {
	if (competencyCache.get(competencyId) == null)
	    return null;
	return (Competency) competencyCache.get(competencyId).getValue();
    }

    private void updateCache(Competency competency) {
	if (competencyCache.get(competency.getCompetencyId()) != null) {
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

    public Competency createCompetency(int objectType, long objectId, CompetencyType competencyType) {
	DefaultCompetency competency = new DefaultCompetency();
	competency.setObjectType(objectType);
	competency.setObjectId(objectId);
	competency.setCompetencyType(competencyType);
	return competency;
    }

    public int getCompetencyCount(Company company) {
	return competencyDao.getCompetencyCount(company);
    }

    public List<Competency> getCompetencies(Company company) {
	List<Long> ids = competencyDao.getCompetencyIds(company);
	return loadCompetencies(ids);
    }

    public List<Competency> getCompetencies(Company company, int startIndex, int numResults) {
	List<Long> ids = competencyDao.getCompetencyIds(company, startIndex, numResults);
	return loadCompetencies(ids);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateCompetency(Competency competency) throws CompetencyNotFoundException {
	if (competency.getCompetencyId() > 0) {
	    competencyDao.updateCompetency(competency);
	    if (competencyCache.get(competency.getCompetencyId()) != null) {
		competencyCache.remove(competency.getCompetencyId());
	    }
	    updateCache(getCompetency(competency.getCompetencyId()));
	}
    }

    public Competency getCompetency(long competencyId) throws CompetencyNotFoundException {
	Competency competency = getCompetencyInCache(competencyId);
	if (competency == null) {
	    competency = competencyDao.getCompetencyById(competencyId);
	    if (competency == null) {
		throw new CompetencyNotFoundException();
	    }
	    updateCache(competency);
	}
	return competency;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(Competency competency) throws CompetencyNotFoundException {
	if (competency.getCompetencyId() > 0) {
	    updateCompetency(competency);
	} else {
	    competencyDao.createCompetency(competency);
	}

    }

    public EssentialElement getEssentialElement(long essentialElementId) throws EssentialElementNotFoundException {

	EssentialElement essentialElement = getEssentialElementInCache(essentialElementId);
	if (essentialElement == null) {
	    essentialElement = competencyDao.getEssentialElementById(essentialElementId);
	    if (essentialElement == null) {
		throw new EssentialElementNotFoundException();
	    }
	    updateCache(essentialElement);
	}
	return essentialElement;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void createEssentialElement(EssentialElement essentialElement) throws CompetencyNotFoundException {
	if (essentialElement.getCompetencyId() < 1) {
	    throw new CompetencyNotFoundException();
	}
	getCompetency(essentialElement.getCompetencyId());
	competencyDao.createEssentialElement(essentialElement);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateEssentialElement(EssentialElement essentialElement) throws EssentialElementNotFoundException {
	if (essentialElement.getEssentialElementId() > 0) {
	    competencyDao.updateEssentialElement(essentialElement);
	    updateCache(getEssentialElement(essentialElement.getEssentialElementId()));
	}
    }

    private EssentialElement getEssentialElementInCache(Long essentialElementId) {
	if (essentialElementCache.get(essentialElementId) == null)
	    return null;
	return (EssentialElement) essentialElementCache.get(essentialElementId).getValue();
    }

    private void updateCache(EssentialElement essentialElement) {
	if (competencyCache.get(essentialElement.getEssentialElementId()) != null) {
	    competencyCache.remove(essentialElement.getEssentialElementId());
	}
	essentialElementCache.put(new Element(essentialElement.getEssentialElementId(), essentialElement));
    }

    @Override
    public List<EssentialElement> getEssentialElements(Competency competency) {
	List<Long> ids = competencyDao.getEssentialElementIds(competency);
	ArrayList<EssentialElement> list = new ArrayList<EssentialElement>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getEssentialElement(id));
	    } catch (EssentialElementNotFoundException e) {
	    }
	}
	return list;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(EssentialElement essentialElement) throws EssentialElementNotFoundException {
	if (essentialElement.getEssentialElementId() > 0) {
	    updateEssentialElement(essentialElement);
	} else {
	    competencyDao.createEssentialElement(essentialElement);
	}
    }

    public int getCompetencyCount(Company company, Classification classify) {
	return competencyDao.getCompetencyCount(company, classify);
    }

    public List<Competency> getCompetencies(Company company, Classification classify) {
	List<Long> ids = competencyDao.getCompetencyIds(company, classify);
	return loadCompetencies(ids);
    }

    public List<Competency> getCompetencies(Company company, Classification classify, int startIndex, int numResults) {
	List<Long> ids = competencyDao.getCompetencyIds(company, classify, startIndex, numResults);
	return loadCompetencies(ids);
    }

    public int getCompetencyCount(Job job) {
	return competencyDao.getCompetencyCount(job);
    }

    public List<Competency> getCompetencies(Job job) {
	List<Long> ids = competencyDao.getCompetencyIds(job);
	return loadCompetencies(ids);
    }

    public List<Competency> getCompetencies(Job job, int startIndex, int numResults) {
	List<Long> ids = competencyDao.getCompetencyIds(job, startIndex, numResults);
	return loadCompetencies(ids);
    }

    public List<Competency> getCompetencies(Company company, CompetencyType competencyType) {
	List<Long> ids = competencyDao.getCompetencyIds(1, company.getCompanyId(), competencyType);
	return loadCompetencies(ids);
    }

    public List<Competency> getCompetencies(Company company, CompetencyType competencyType, int startIndex,
	    int numResults) {
	List<Long> ids = competencyDao.getCompetencyIds(1, company.getCompanyId(), competencyType, startIndex,
		numResults);
	return loadCompetencies(ids);
    }

    public int getCompetencyCount(Company company, CompetencyType competencyType) {
	return competencyDao.getCompetencyCount(1, company.getCompanyId(), competencyType);
    }

    public List<Competency> getCompetenciesByJobAndJobLevel(Job job, long jobLevelId) {

	int levelToUse = 0;
	boolean isStrong = false;
	for (JobLevel jobLevel : job.getJobLevels()) {
	    if (jobLevel.getJobLevelId() == jobLevelId) {
		levelToUse = jobLevel.getLevel();
		isStrong = jobLevel.isStrong();
		break;
	    }
	}
	if (isStrong) {
	    return loadCompetencies(competencyDao.getCompetencyIdsByJobAndJobLevel(job, jobLevelId));
	} else {
	    return loadCompetencies(competencyDao.getCompetencyIdsByJobAndLevel(job, levelToUse));
	}
    }

    @Override
    public List<Competency> findCompetency(Company company, String groupCode, int level, String name,
	    Classification classify, long jobId, int startIndex, int numResults) {
	List<Long> ids = competencyDao.getCompetencyIds(1, company.getCompanyId(), groupCode, level, name, classify,
		jobId, startIndex, numResults);
	return loadCompetencies(ids);
    }

    public List<Competency> findCompetency(Company company, String groupCode, int level, String name,
	    Classification classify, long jobId) {
	List<Long> ids = competencyDao.getCompetencyIds(1, company.getCompanyId(), groupCode, level, name, classify,
		jobId);
	return loadCompetencies(ids);
    }

    @Override
    public int getCompetencyCount(Company company, String groupCode, int level, String name, Classification classify,
	    long jobId) {
	return competencyDao.getCompetencyCount(1, company.getCompanyId(), groupCode, level, name, classify, jobId);
    }

    private List<Competency> loadCompetencies(List<Long> ids) {
	ArrayList<Competency> list = new ArrayList<Competency>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getCompetency(id));
	    } catch (CompetencyNotFoundException e) {

	    }
	}
	return list;
    }

    private String getPerformanceCriteriaIdsCacheKey(int objectType, long objectId) {
	return new StringBuilder("performanceCriteriaIDs-").append(objectType).append("-").append(objectId).toString();
    }

    private String getAbilityIdsCacheKey(int objectType, long objectId) {
	return new StringBuilder("abilityIDs-").append(objectType).append("-").append(objectId).toString();
    }

    private List<PerformanceCriteria> loadPerformanceCriterias(List<Long> ids) {
	ArrayList<PerformanceCriteria> list = new ArrayList<PerformanceCriteria>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getPerformanceCriteria(id));
	    } catch (PerformanceCriteriaNotFoundException e) {
	    }
	}
	return list;
    }

    private PerformanceCriteria getPerformanceCriteriaInCache(long performanceCriteriaId) {
	PerformanceCriteria performanceCriteria = null;
	if (this.performanceCriteriaCache.get(performanceCriteriaId) != null) {
	    performanceCriteria = (PerformanceCriteria) this.performanceCriteriaCache.get(performanceCriteriaId)
		    .getObjectValue();
	}
	return performanceCriteria;
    }

    private void updateCache(PerformanceCriteria performanceCriteria) {
	if (this.performanceCriteriaCache.get(performanceCriteria.getPerformanceCriteriaId()) != null) {
	    this.performanceCriteriaCache.remove(performanceCriteria.getPerformanceCriteriaId());
	}
	this.performanceCriteriaCache
		.put(new Element(performanceCriteria.getPerformanceCriteriaId(), performanceCriteria));
    }

    public List<PerformanceCriteria> getPerformanceCriterias(int objectType, long objectId) {
	List<Long> ids = null;
	String cacheKey = getPerformanceCriteriaIdsCacheKey(objectType, objectId);
	if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
	    ids = (List<Long>) this.performanceCriteriaIdsCache.get(cacheKey).getObjectValue();
	}
	if (ids == null) {
	    ids = competencyDao.getPerformanceCriteriaIds(objectType, objectId);
	    this.performanceCriteriaIdsCache.put(new Element(cacheKey, ids));
	}
	return loadPerformanceCriterias(ids);
    }

    @Override
    public PerformanceCriteria getPerformanceCriteria(long performanceCriteriaId)
	    throws PerformanceCriteriaNotFoundException {
	PerformanceCriteria performanceCriteria = getPerformanceCriteriaInCache(performanceCriteriaId);
	if (performanceCriteria == null) {
	    performanceCriteria = competencyDao.getPerformanceCriteriaById(performanceCriteriaId);
	    if (performanceCriteria == null) {
		throw new PerformanceCriteriaNotFoundException();
	    }
	    updateCache(performanceCriteria);
	}
	return performanceCriteria;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(PerformanceCriteria performanceCriteria) throws PerformanceCriteriaNotFoundException {
	if (performanceCriteria.getPerformanceCriteriaId() > 0) {
	    competencyDao.updatePerformanceCriteria(performanceCriteria);
	} else {
	    competencyDao.createPerformanceCriteria(performanceCriteria);
	}
	updateCache(performanceCriteria);

	String cacheKey = getPerformanceCriteriaIdsCacheKey(performanceCriteria.getObjectType(),
		performanceCriteria.getObjectId());
	if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
	    this.performanceCriteriaIdsCache.remove(cacheKey);
	}
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdatePerformanceCriterias(List<PerformanceCriteria> performanceCriterias) {
	competencyDao.saveOrUpdatePerformanceCriterias(performanceCriterias);
	for (PerformanceCriteria pc : performanceCriterias) {
	    String cacheKey = getPerformanceCriteriaIdsCacheKey(pc.getObjectType(), pc.getObjectId());
	    if (this.performanceCriteriaCache.get(pc.getPerformanceCriteriaId()) != null) {
		performanceCriteriaCache.remove(pc.getPerformanceCriteriaId());
	    }
	    if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
		this.performanceCriteriaIdsCache.remove(cacheKey);
	    }
	}

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void removePerformanceCriterias(List<PerformanceCriteria> performanceCriterias) {
	competencyDao.removePerformanceCriterias(performanceCriterias);
	for (PerformanceCriteria pc : performanceCriterias) {
	    String cacheKey = getPerformanceCriteriaIdsCacheKey(pc.getObjectType(), pc.getObjectId());
	    if (this.performanceCriteriaCache.get(pc.getPerformanceCriteriaId()) != null) {
		performanceCriteriaCache.remove(pc.getPerformanceCriteriaId());
	    }
	    if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
		this.performanceCriteriaIdsCache.remove(cacheKey);
	    }
	}
    }

    @Override
    public List<Ability> getAbilities(int objectType, long objectId) {
	List<Long> ids = null;
	String cacheKey = getAbilityIdsCacheKey(objectType, objectId);
	if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
	    ids = (List<Long>) this.performanceCriteriaIdsCache.get(cacheKey).getObjectValue();
	}
	if (ids == null) {
	    ids = competencyDao.getAbilityIds(objectType, objectId);
	    this.performanceCriteriaIdsCache.put(new Element(cacheKey, ids));
	}
	return loadAbilities(ids);
    }

    private List<Ability> loadAbilities(List<Long> ids) {
	ArrayList<Ability> list = new ArrayList<Ability>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getAbility(id));
	    } catch (AbilityNotFoundException e) {
	    }
	}
	return list;
    }

    private Ability getAbilityInCache(long abilityId) {
	Ability ability = null;
	if (this.abilityCache.get(abilityId) != null) {
	    ability = (Ability) this.abilityCache.get(abilityId).getObjectValue();
	}
	return ability;
    }

    private void updateCache(Ability ability) {
	if (this.abilityCache.get(ability.getAbilityId()) != null) {
	    this.abilityCache.remove(ability.getAbilityId());
	}
	this.abilityCache.put(new Element(ability.getAbilityId(), ability));
    }

    @Override
    public Ability getAbility(long abilityId) throws AbilityNotFoundException {
	Ability ability = getAbilityInCache(abilityId);
	if (ability == null) {
	    ability = competencyDao.getAbilityById(abilityId);
	    if (ability == null) {
		throw new AbilityNotFoundException();
	    }
	    updateCache(ability);
	}
	return ability;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateAbility(Ability ability) throws AbilityNotFoundException {
	if (ability.getAbilityId() > 0) {
	    competencyDao.updateAbility(ability);
	} else {
	    competencyDao.createAbility(ability);
	}
	updateCache(ability);
	String cacheKey = getAbilityIdsCacheKey(ability.getObjectType(), ability.getObjectId());
	if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
	    this.performanceCriteriaIdsCache.remove(cacheKey);
	}
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateAblilities(List<Ability> abilities) {
	competencyDao.saveOrUpdateAbilities(abilities);
	for (Ability ability : abilities) {
	    String cacheKey = getAbilityIdsCacheKey(ability.getObjectType(), ability.getObjectId());
	    if (this.abilityCache.get(ability.getAbilityId()) != null) {
		abilityCache.remove(ability.getAbilityId());
	    }
	    if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
		this.performanceCriteriaIdsCache.remove(cacheKey);
	    }
	}
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void removeAbilities(List<Ability> abilities) {
	competencyDao.removeAbilities(abilities);
	for (Ability ability : abilities) {
	    String cacheKey = getAbilityIdsCacheKey(ability.getObjectType(), ability.getObjectId());
	    if (this.abilityCache.get(ability.getAbilityId()) != null) {
		abilityCache.remove(ability.getAbilityId());
	    }
	    if (this.performanceCriteriaIdsCache.get(cacheKey) != null) {
		this.performanceCriteriaIdsCache.remove(cacheKey);
	    }
	}
    }

}