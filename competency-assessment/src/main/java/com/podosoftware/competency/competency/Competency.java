package com.podosoftware.competency.competency;

import com.podosoftware.competency.job.Job;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

/**
 * 
 * An important skill that is needed to do a job.
 * 
 * 
 * @author donghyuck
 *
 */
public interface Competency extends PropertyAware, Cacheable {

    public Integer getObjectType();

    public void setObjectType(Integer objectType);

    public Long getObjectId();

    public void setObjectId(Long objectId);

    public Long getCompetencyId();

    public void setCompetencyId(Long competencyId);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Integer getLevel();

    public void setLevel(Integer level);

    public abstract String getCompetencyUnitCode();

    public abstract void setCompetencyUnitCode(String competencyUnitCode);

    public abstract String getCompetencyGroupCode();

    public abstract void setCompetencyGroupCode(String competencyGroupCode);

    public abstract CompetencyType getCompetencyType();

    public abstract Job getJob();

}
