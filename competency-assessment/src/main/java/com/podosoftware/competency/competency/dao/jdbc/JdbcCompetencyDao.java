package com.podosoftware.competency.competency.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.DefaultCompetency;
import com.podosoftware.competency.competency.DefaultEssentialElement;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.competency.dao.CompetencyDao;

import architecture.common.user.Company;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCompetencyDao extends ExtendedJdbcDaoSupport implements CompetencyDao {

	private final RowMapper<Competency> competencyMapper = new RowMapper<Competency>(){

		public Competency mapRow(ResultSet rs, int rowNum) throws SQLException {		
			DefaultCompetency dc = new DefaultCompetency();		
			dc.setObjectType(rs.getInt("OBJECT_TYPE"));
			dc.setObjectId(rs.getLong("OBJECT_ID"));
			dc.setCompetencyId( rs.getLong("COMPETENCY_ID") );			
			dc.setName(rs.getString("NAME")); 
			dc.setDescription( rs.getString("DESCRIPTION")); 		
			return dc;
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

	
	public Competency createCompetency(Competency competency) {
		Long competencyId = getNextId(sequencerName);
		competency.setCompetencyId(competencyId);
		try {
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.CREATE_COMPETENCY").getSql(), 
					new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() ),
					new SqlParameterValue( Types.NUMERIC, competency.getObjectType() ),
					new SqlParameterValue( Types.NUMERIC, competency.getObjectId() ),
					new SqlParameterValue( Types.VARCHAR, competency.getName() ),
					new SqlParameterValue( Types.VARCHAR, competency.getDescription() )
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
					new SqlParameterValue( Types.VARCHAR, essentialElement.getLevel())
			);
			setEssentialElementProperties(essentialElement.getEssentialElementId(), essentialElement.getProperties());
		} catch (DataAccessException e) {
			throw e;
		}		
	}


	@Override
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
	
}
