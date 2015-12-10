package com.podosoftware.competency.competency.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import com.podosoftware.competency.competency.Ability;
import com.podosoftware.competency.competency.AbilityType;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyType;
import com.podosoftware.competency.competency.DefaultAbility;
import com.podosoftware.competency.competency.DefaultCompetency;
import com.podosoftware.competency.competency.DefaultEssentialElement;
import com.podosoftware.competency.competency.DefaultPerformanceCriteria;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.competency.PerformanceCriteria;
import com.podosoftware.competency.competency.dao.CompetencyDao;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.Job;

import architecture.common.user.Company;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCompetencyDao extends ExtendedJdbcDaoSupport implements CompetencyDao {

	
	private final RowMapper<Ability> abilityMapper = new RowMapper<Ability>(){

		public Ability mapRow(ResultSet rs, int rowNum) throws SQLException {		
			Ability ability = new DefaultAbility();		
			if(rs.getInt("ABILITY_TYPE") > 0) 
				ability.setAbilityType(AbilityType.getAbilityTypeById(rs.getInt("ABILITY_TYPE")));				
			ability.setAbilityId(rs.getLong("ABILITY_ID"));
			ability.setObjectType(rs.getInt("OBJECT_TYPE"));
			ability.setObjectId(rs.getLong("OBJECT_ID"));
			ability.setName( rs.getString("NAME")); 	
			ability.setDescription( rs.getString("DESCRIPTION")); 	
			return ability;
		}		
	};
	
	private final RowMapper<PerformanceCriteria> performanceCriteriaMapper = new RowMapper<PerformanceCriteria>(){

		public PerformanceCriteria mapRow(ResultSet rs, int rowNum) throws SQLException {		
			PerformanceCriteria criteria = new DefaultPerformanceCriteria();		
			criteria.setPerformanceCriteriaId(rs.getLong("PERFORMANCE_CRITERIA_ID"));
			criteria.setObjectType(rs.getInt("OBJECT_TYPE"));
			criteria.setObjectId(rs.getLong("OBJECT_ID"));
			criteria.setSortOrder( rs.getInt("SORT_ORDER")); 	
			criteria.setDescription( rs.getString("DESCRIPTION")); 	
			criteria.setCreationDate(rs.getDate("CREATION_DATE"));
			criteria.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return criteria;
		}		
	};
	
	private final RowMapper<Competency> competencyMapper = new RowMapper<Competency>(){

		public Competency mapRow(ResultSet rs, int rowNum) throws SQLException {		
			DefaultCompetency competency = new DefaultCompetency();		
			if(rs.getInt("COMPETENCY_TYPE")>0) 
				competency.setCompetencyType(CompetencyType.getCompetencyTypeById(rs.getInt("COMPETENCY_TYPE")));			
			competency.setObjectType(rs.getInt("OBJECT_TYPE"));
			competency.setObjectId(rs.getLong("OBJECT_ID"));
			competency.setCompetencyId( rs.getLong("COMPETENCY_ID") );			
			competency.setName(rs.getString("NAME")); 
			competency.setDescription( rs.getString("DESCRIPTION")); 		
			competency.setLevel(rs.getInt("COMPETENCY_LEVEL"));
			competency.setCompetencyUnitCode(rs.getString("COMPETENCY_UNIT_CODE"));
			return competency;
		}		
	};	
	
	private final RowMapper<EssentialElement> essentialElementMapper = new RowMapper<EssentialElement>(){

		public EssentialElement mapRow(ResultSet rs, int rowNum) throws SQLException {		
			DefaultEssentialElement element = new DefaultEssentialElement();	
			element.setCompetencyId(rs.getLong("COMPETENCY_ID"));
			element.setEssentialElementId(rs.getLong("ESSENTIAL_ELEMENT_ID"));
			element.setName(rs.getString("NAME"));
			element.setLevel(rs.getInt("COMPETENCY_LEVEL"));			
			return element;
		}		
	};	
	
	private String sequencerName = "COMPETENCY";
	
	private String competencyPropertyTableName = "CA_COMPETENCY_PROPERTY";
	
	private String competencyPropertyPrimaryColumnName = "COMPETENCY_ID";
		
	private String essentialElementSequencerName = "ESSENTIAL_ELEMENT";
	
	private String essentialElementPropertyTableName = "CA_ESSENTIAL_ELEMENT_PROPERTY";
	
	private String essentialElementPropertyPrimaryColumnName = "ESSENTIAL_ELEMENT_ID";
	
	
	private String performanceCriteriaSequencerName = "PERFORMANCE_CRITERIA";
	
	private String performanceCriteriaPropertyTableName = "CA_PERFORMANCE_CRITERIA_PROP";
	
	private String performanceCriteriaPropertyPrimaryColumnName = "PERFORMANCE_CRITERIA_ID";	
	
	private String abilitySequencerName = "ABILITY";
	
	private ExtendedPropertyDao extendedPropertyDao;
	
	
	
	public JdbcCompetencyDao() {
	}


	public String getSequencerName() {
		return sequencerName;
	}


	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}


	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}


	public String getCompetencyPropertyTableName() {
		return competencyPropertyTableName;
	}


	public void setCompetencyPropertyTableName(String competencyPropertyTableName) {
		this.competencyPropertyTableName = competencyPropertyTableName;
	}


	public String getCompetencyPropertyPrimaryColumnName() {
		return competencyPropertyPrimaryColumnName;
	}


	public void setCompetencyPropertyPrimaryColumnName(String competencyPropertyPrimaryColumnName) {
		this.competencyPropertyPrimaryColumnName = competencyPropertyPrimaryColumnName;
	}


	public String getEssentialElementSequencerName() {
		return essentialElementSequencerName;
	}


	public void setEssentialElementSequencerName(String essentialElementSequencerName) {
		this.essentialElementSequencerName = essentialElementSequencerName;
	}


	public String getEssentialElementPropertyTableName() {
		return essentialElementPropertyTableName;
	}


	public void setEssentialElementPropertyTableName(String essentialElementPropertyTableName) {
		this.essentialElementPropertyTableName = essentialElementPropertyTableName;
	}


	public String getEssentialElementPropertyPrimaryColumnName() {
		return essentialElementPropertyPrimaryColumnName;
	}


	public void setEssentialElementPropertyPrimaryColumnName(String essentialElementPropertyPrimaryColumnName) {
		this.essentialElementPropertyPrimaryColumnName = essentialElementPropertyPrimaryColumnName;
	}


	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	
	public String getPerformanceCriteriaSequencerName() {
		return performanceCriteriaSequencerName;
	}


	public void setPerformanceCriteriaSequencerName(String performanceCriteriaSequencerName) {
		this.performanceCriteriaSequencerName = performanceCriteriaSequencerName;
	}


	public String getPerformanceCriteriaPropertyTableName() {
		return performanceCriteriaPropertyTableName;
	}


	public void setPerformanceCriteriaPropertyTableName(String performanceCriteriaPropertyTableName) {
		this.performanceCriteriaPropertyTableName = performanceCriteriaPropertyTableName;
	}


	public String getPerformanceCriteriaPropertyPrimaryColumnName() {
		return performanceCriteriaPropertyPrimaryColumnName;
	}


	public void setPerformanceCriteriaPropertyPrimaryColumnName(String performanceCriteriaPropertyPrimaryColumnName) {
		this.performanceCriteriaPropertyPrimaryColumnName = performanceCriteriaPropertyPrimaryColumnName;
	}


	public Map<String, String> getCompetencyProperties(long competencyId) {
		return extendedPropertyDao.getProperties(competencyPropertyTableName, competencyPropertyPrimaryColumnName, competencyId);
	}

	public void setCompetencyProperties(long competencyId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(competencyPropertyTableName, competencyPropertyPrimaryColumnName, competencyId, props);
	}

	public Map<String, String> getEssentialElementProperties(long essentialElementId) {
		return extendedPropertyDao.getProperties(essentialElementPropertyTableName, essentialElementPropertyPrimaryColumnName, essentialElementId);
	}

	public void setEssentialElementProperties(long essentialElementId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(essentialElementPropertyTableName, essentialElementPropertyPrimaryColumnName, essentialElementId, props);
	}
	
	public Map<String, String> getPerformanceCriteriaProperties(long performanceCriteriaId) {
		return extendedPropertyDao.getProperties(this.performanceCriteriaPropertyTableName, this.performanceCriteriaPropertyPrimaryColumnName, performanceCriteriaId);
	}

	public void setPerformanceCriteriaProperties(long performanceCriteriaId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(this.performanceCriteriaPropertyTableName, this.performanceCriteriaPropertyPrimaryColumnName, performanceCriteriaId, props);
	}
	
	public void deletePerformanceCriteriaProperties(long performanceCriteriaId){
		extendedPropertyDao.deleteProperties(this.performanceCriteriaPropertyTableName, this.performanceCriteriaPropertyPrimaryColumnName, performanceCriteriaId);
	}
	

	public Long nextCompetencyId() {
		return getNextId(sequencerName);
	}
	
	public Long nextEssentialElementId(){
		return getNextId(essentialElementSequencerName);
	}

	public Long nextPerformanceCriteriaId(){
		return getNextId(this.performanceCriteriaSequencerName);
	}
	
	public Competency createCompetency(Competency competency) {
		Long competencyId = getNextId(sequencerName);
		competency.setCompetencyId(competencyId);
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.CREATE_COMPETENCY").getSql(), 
					new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() ),
					new SqlParameterValue( Types.NUMERIC, competency.getCompetencyType().getId() ),
					new SqlParameterValue( Types.NUMERIC, competency.getObjectType() ),
					new SqlParameterValue( Types.NUMERIC, competency.getObjectId() ),
					new SqlParameterValue( Types.VARCHAR, competency.getName() ),
					new SqlParameterValue( Types.VARCHAR, competency.getDescription() ),
					new SqlParameterValue( Types.VARCHAR, competency.getLevel()),
					new SqlParameterValue( Types.VARCHAR, competency.getCompetencyUnitCode())
			);
			
			if(competency.getProperties().size() > 0)
				setCompetencyProperties(competency.getCompetencyId(), competency.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}		
		return competency;
	}


	public Competency updateCompetency(Competency competency) {
		Date now = new Date();		
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_COMPETENCY").getSql(), 
					new SqlParameterValue( Types.VARCHAR, competency.getName() ),
					new SqlParameterValue( Types.VARCHAR, competency.getDescription() ),
					new SqlParameterValue( Types.VARCHAR, competency.getLevel()),
					new SqlParameterValue( Types.VARCHAR, competency.getCompetencyUnitCode()),
					new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() )
			);
			setCompetencyProperties(competency.getCompetencyId(), competency.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}		
		return competency;
	}


	public Competency getCompetencyById(long competencyId) {
		Competency competency = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_BY_ID").getSql(), 
			competencyMapper,
			new SqlParameterValue( Types.NUMERIC, competencyId )
		);
		competency.setProperties(getCompetencyProperties(competency.getCompetencyId()));		
		return competency;
	}


	public Competency getCompetencyByName(Company company, String name) {
		Competency competency = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_NAME").getSql(), 
				competencyMapper,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId() ),
				new SqlParameterValue( Types.VARCHAR, name )
			);
		competency.setProperties(getCompetencyProperties(competency.getCompetencyId()));		
		return competency;
	}

	public void deleteCompetency(Competency competency) {
		getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.DELETE_COMPETENCY").getSql(), 
				new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() )
		);		
		extendedPropertyDao.deleteProperties(competencyPropertyTableName, competencyPropertyPrimaryColumnName, competency.getCompetencyId());
	}

	public int getCompetencyCount(Company company) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.COUNT_COMPETENCY_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId() )
			);
	}


	public List<Long> getCompetencyIds(Company company) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_ID_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId() )				
		);
	}


	public List<Long> getCompetencyIds(Company company, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_ID_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ 1, company.getCompanyId()}, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
	}


	@Override
	public void createEssentialElement(EssentialElement essentialElement) {
		Long essentialElementId = getNextId(essentialElementSequencerName);
		essentialElement.setEssentialElementId(essentialElementId);
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.CREATE_ESSENTIAL_ELEMENT").getSql(), 
					new SqlParameterValue( Types.NUMERIC, essentialElement.getCompetencyId() ),
					new SqlParameterValue( Types.NUMERIC, essentialElement.getEssentialElementId()),
					new SqlParameterValue( Types.VARCHAR, essentialElement.getName() ),
					new SqlParameterValue( Types.NUMERIC, essentialElement.getLevel() )
			);
			
			if(essentialElement.getProperties().size() > 0)
				setEssentialElementProperties(essentialElement.getEssentialElementId(), essentialElement.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}			
	}


	@Override
	public void updateEssentialElement(EssentialElement essentialElement) {
		Date now = new Date();		
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_ESSENTIAL_ELEMENT").getSql(), 
					new SqlParameterValue( Types.VARCHAR, essentialElement.getName() ),
					new SqlParameterValue( Types.VARCHAR, essentialElement.getLevel()),
					new SqlParameterValue( Types.NUMERIC, essentialElement.getEssentialElementId())
			);
			setEssentialElementProperties(essentialElement.getEssentialElementId(), essentialElement.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}		
	}

	public EssentialElement getEssentialElementById(long essentialElementId) {
		EssentialElement essentialElement = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_ESSENTIAL_ELEMENT_BY_ID").getSql(), 
				essentialElementMapper,
				new SqlParameterValue( Types.NUMERIC, essentialElementId )
			);
		essentialElement.setProperties(getEssentialElementProperties(essentialElement.getEssentialElementId()));		
		return essentialElement;
	}


	@Override
	public List<Long> getEssentialElementIds(Competency competency) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_ESSENTIAL_ELEMENT_IDS_BY_COMPETENCY_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() )				
		);
	}


	@Override
	public void createCompetency(List<Competency> competencies) {
		final List<Competency> inserts = competencies;
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.CREATE_COMPETENCY").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						Competency competency = inserts.get(i);		
						ps.setLong(1, competency.getCompetencyId());
						ps.setInt(2, competency.getObjectType());
						ps.setLong(3, competency.getObjectId());
						ps.setString(4, competency.getName());
						ps.setString(5, competency.getDescription());
						ps.setInt(6, competency.getLevel());	
						ps.setString(7,  competency.getCompetencyUnitCode());
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}
	}


	@Override
	public Map<String, Long> getCompetencyIdsWithCompetencyUnitCode(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_ID_WITH_COMPETENCY_UNIT_CODE").getSql(), 				
				new ResultSetExtractor<Map<String, Long>>(){
					public Map<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, Long> map = new HashMap<String, Long>();
						while(rs.next()){
							String key = rs.getString(2);
							Long value = rs.getLong(1);
							map.put(key, value);
						}						
						return map;
					}}, 				
				new SqlParameterValue( Types.NUMERIC, objectType),
				new SqlParameterValue( Types.NUMERIC, objectId));
	}


	
	
	@Override
	public void createEssentialElement(List<EssentialElement> elements) {
		final List<EssentialElement> inserts = elements;
		if(inserts.size() > 0){
			for( EssentialElement item : inserts){
				if(item.getEssentialElementId() <= 0 ){
					item.setEssentialElementId(nextEssentialElementId());
				}
			}
			
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.CREATE_ESSENTIAL_ELEMENT").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						EssentialElement element = inserts.get(i);		
						ps.setLong(1, element.getCompetencyId());
						ps.setLong(2, element.getEssentialElementId());
						ps.setString(3, element.getName());
						ps.setInt(4, element.getLevel());
					}					
					public int getBatchSize() {
						return inserts.size();
					}
			});		
		}
	}

	public int getCompetencyCount(Company company, Classification classify) {
		return getExtendedJdbcTemplate().queryForObject( 
				getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.COUNT_COMPETENCY_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(),
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId() )
			);
	}


	public List<Long> getCompetencyIds(Company company, Classification classify) {
		return getExtendedJdbcTemplate().queryForList(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId())				
		);
	}

	public List<Long> getCompetencyIds(Company company, Classification classify, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ 1, company.getCompanyId()}, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
	}

 
	public int getCompetencyCount(Job job) {
		return getExtendedJdbcTemplate().queryForObject( 
				getBoundSql("COMPETENCY_ACCESSMENT.COUNT_COMPETENCY_BY_JOB_ID").getSql(),
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, job.getJobId() )
			);
	}
 
	public List<Long> getCompetencyIds(Job job) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_JOB_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, job.getJobId())				
		);
	}
 
	public List<Long> getCompetencyIds(Job job, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_JOB_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ job.getJobId() }, 
				new int[] {Types.NUMERIC}, 
				Long.class);
	}


	public int getCompetencyCount(int objectType, long objectId, CompetencyType competencyType) {
		return getExtendedJdbcTemplate().queryForObject( 
				getBoundSql("COMPETENCY_ACCESSMENT.COUNT_COMPETENCY_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_TYPE").getSql(),
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, objectType ),
				new SqlParameterValue( Types.NUMERIC, objectId ),
				new SqlParameterValue( Types.NUMERIC, competencyType.getId() )
			);
	}

	public List<Long> getCompetencyIds(int objectType, long objectId, CompetencyType competencyType) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_TYPE").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, objectType ),
				new SqlParameterValue( Types.NUMERIC, objectId ),
				new SqlParameterValue( Types.NUMERIC, competencyType.getId() )		
		);
	}

	public List<Long> getCompetencyIds(int objectType, long objectId, CompetencyType competencyType, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_TYPE").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ objectType, objectId, competencyType.getId() }, 
				new int[] {Types.NUMERIC, Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
	}

	public PerformanceCriteria getPerformanceCriteriaById(long performanceCriteriaId) {
		PerformanceCriteria performanceCriteria = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_PERFORMANCE_CRITERIAL_BY_ID").getSql(), 
				performanceCriteriaMapper,
				new SqlParameterValue( Types.NUMERIC, performanceCriteriaId )
			);
		performanceCriteria.setProperties(getPerformanceCriteriaProperties(performanceCriteria.getPerformanceCriteriaId()));		
		return performanceCriteria;
	}


	public List<Long> getPerformanceCriteriaIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_PERFORMANCE_CRITERIA_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, objectType ),
				new SqlParameterValue( Types.NUMERIC, objectId ) );
	}

 
	public void createPerformanceCriteria(PerformanceCriteria performanceCriteria) {
		Long performanceCriteriaId = nextPerformanceCriteriaId();
		performanceCriteria.setPerformanceCriteriaId(performanceCriteriaId);
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.CREATE_PERFORMANCE_CRITERIA").getSql(), 
					new SqlParameterValue( Types.NUMERIC, performanceCriteria.getPerformanceCriteriaId() ),
					new SqlParameterValue( Types.NUMERIC, performanceCriteria.getObjectType()),
					new SqlParameterValue( Types.NUMERIC, performanceCriteria.getObjectId() ),
					new SqlParameterValue( Types.VARCHAR, performanceCriteria.getSortOrder()),
					new SqlParameterValue( Types.VARCHAR, performanceCriteria.getDescription()),
					new SqlParameterValue( Types.TIMESTAMP, performanceCriteria.getCreationDate()),
					new SqlParameterValue( Types.TIMESTAMP, performanceCriteria.getModifiedDate())
			);
			if(performanceCriteria.getProperties().size() > 0)
				setPerformanceCriteriaProperties(performanceCriteria.getPerformanceCriteriaId(), performanceCriteria.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}
	}
 
	public void updatePerformanceCriteria(PerformanceCriteria performanceCriteria) {
		getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_PERFORMANCE_CRITERIA").getSql(), 
				new SqlParameterValue( Types.NUMERIC, performanceCriteria.getObjectType()),
				new SqlParameterValue( Types.NUMERIC, performanceCriteria.getObjectId()),
				new SqlParameterValue( Types.VARCHAR, performanceCriteria.getSortOrder()),
				new SqlParameterValue( Types.VARCHAR, performanceCriteria.getDescription()),
				new SqlParameterValue( Types.TIMESTAMP, performanceCriteria.getModifiedDate()),
				new SqlParameterValue( Types.TIMESTAMP, performanceCriteria.getPerformanceCriteriaId())
		);
		deletePerformanceCriteriaProperties(performanceCriteria.getPerformanceCriteriaId());
		setPerformanceCriteriaProperties(performanceCriteria.getPerformanceCriteriaId(), performanceCriteria.getProperties());		
	}

	public void saveOrUpdatePerformanceCriterias(List<PerformanceCriteria> performanceCriterias) {
		final List<PerformanceCriteria> inserts = new ArrayList<PerformanceCriteria>();
		final List<PerformanceCriteria> updates = new ArrayList<PerformanceCriteria>();		
		Date now = new Date();
		for( PerformanceCriteria pc : performanceCriterias ){
			if(pc.getPerformanceCriteriaId() > 0){
				pc.setModifiedDate(now);
				updates.add(pc);
			}else{
				pc.setPerformanceCriteriaId(nextPerformanceCriteriaId());
				pc.setCreationDate(now);
				pc.setModifiedDate(now);
				inserts.add(pc);
			}
		}		
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.CREATE_PERFORMANCE_CRITERIA").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						PerformanceCriteria pc = inserts.get(i);		
						ps.setLong(1, pc.getPerformanceCriteriaId());
						ps.setInt(2, pc.getObjectType());
						ps.setLong(3, pc.getObjectId());
						ps.setInt(4,  pc.getSortOrder());
						ps.setString(5, pc.getDescription());
						ps.setTimestamp(6, new java.sql.Timestamp(pc.getCreationDate().getTime()));	
						ps.setTimestamp(7,  new java.sql.Timestamp(pc.getModifiedDate().getTime()));
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}
		if(updates.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_PERFORMANCE_CRITERIA").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						PerformanceCriteria pc = updates.get(i);
						ps.setInt(1, pc.getObjectType());
						ps.setLong(2, pc.getObjectId());
						ps.setInt(3,  pc.getSortOrder());
						ps.setString(4, pc.getDescription());
						ps.setTimestamp(5, new java.sql.Timestamp(pc.getModifiedDate().getTime()));	
						ps.setLong(6, pc.getPerformanceCriteriaId() );
					}					
					public int getBatchSize() {
						return updates.size();
					}
				});		
		}		
	}

	public void removePerformanceCriterias(List<PerformanceCriteria> performanceCriterias) {
		final List<PerformanceCriteria> deletes = performanceCriterias;
		if(deletes.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.DELETE_PERFORMANCE_CRITERIA").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						PerformanceCriteria pc = deletes.get(i);
						ps.setLong(1, pc.getPerformanceCriteriaId());
					}					
					public int getBatchSize() {
						return deletes.size();
					}
			});		
			getExtendedJdbcTemplate().batchUpdate(				
					getBoundSql("COMPETENCY_ACCESSMENT.DELETE_PERFORMANCE_CRITERIA_PROPERTY").getSql(), 
					new BatchPreparedStatementSetter() {					
						public void setValues(PreparedStatement ps, int i) throws SQLException {						
							PerformanceCriteria pc = deletes.get(i);
							ps.setLong(1, pc.getPerformanceCriteriaId());
						}					
						public int getBatchSize() {
							return deletes.size();
						}
			});		
		}			
	}

	
	public String getAbilitySequencerName() {
		return abilitySequencerName;
	}


	public void setAbilitySequencerName(String abilitySequencerName) {
		this.abilitySequencerName = abilitySequencerName;
	}


	public Long nextAbilityId() {
		return this.getNextId(abilitySequencerName);
	}

	public Ability getAbilityById(long abilityId) {
		Ability ability = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_ABILITY_BY_ID").getSql(), 
				abilityMapper,
				new SqlParameterValue( Types.NUMERIC, abilityId )
			);	
		return ability;
	}

	public List<Long> getAbilityIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_ABILITY_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, objectType ),
				new SqlParameterValue( Types.NUMERIC, objectId ) );
	}

	public void createAbility(Ability ability) {
		getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.CREATE_ABILITY").getSql(), 
				new SqlParameterValue( Types.NUMERIC, ability.getAbilityId()),
				new SqlParameterValue( Types.NUMERIC, ability.getObjectType()),
				new SqlParameterValue( Types.NUMERIC, ability.getObjectId()),
				new SqlParameterValue( Types.NUMERIC, ability.getAbilityType().getId()),
				new SqlParameterValue( Types.VARCHAR, ability.getName()),
				new SqlParameterValue( Types.VARCHAR, ability.getDescription())
		);
	}

	public void updateAbility(Ability ability) {
		getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_ABILITY").getSql(), 
			//	new SqlParameterValue( Types.NUMERIC, ability.getObjectType()),
				new SqlParameterValue( Types.NUMERIC, ability.getAbilityType().getId()),
				new SqlParameterValue( Types.VARCHAR, ability.getName()),
				new SqlParameterValue( Types.VARCHAR, ability.getDescription()),
				new SqlParameterValue( Types.NUMERIC, ability.getAbilityId())
		);		
	}

	public void saveOrUpdateAbilities(List<Ability> abilities) {
		final List<Ability> inserts = new ArrayList<Ability>();
		final List<Ability> updates = new ArrayList<Ability>();		

		for( Ability ability : abilities ){
			if(ability.getAbilityId() > 0){
				updates.add(ability);
			}else{
				ability.setAbilityId(nextAbilityId());
				inserts.add(ability);
			}
		}		
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.CREATE_ABILITY").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						Ability ability = inserts.get(i);		
						ps.setLong(1, ability.getAbilityId());
						ps.setInt(2, ability.getObjectType());
						ps.setLong(3, ability.getObjectId());
						ps.setInt(4, ability.getAbilityType().getId());
						ps.setString(5,  ability.getName());
						ps.setString(6, ability.getDescription());
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}
		if(updates.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_ABILITY").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						Ability ability = updates.get(i);
						ps.setInt(1, ability.getAbilityType().getId());
						ps.setString(2,  ability.getName());
						ps.setString(3, ability.getDescription());
						ps.setLong(4, ability.getAbilityId() );
					}					
					public int getBatchSize() {
						return updates.size();
					}
				});		
		}		
	}

	public void removeAbilities(List<Ability> abilities) {
		final List<Ability> deletes = abilities;
		if(deletes.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.DELETE_ABILITY").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {						
						Ability ability = deletes.get(i);
						ps.setLong(1, ability.getAbilityId());
					}					
					public int getBatchSize() {
						return deletes.size();
					}
			});	
		}
	} 
	
}
