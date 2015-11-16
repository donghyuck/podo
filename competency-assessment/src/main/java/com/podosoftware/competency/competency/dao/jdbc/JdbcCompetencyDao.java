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
	
	
	private String sequencerName = "COMPETENCY";
	
	private String competencyPropertyTableName = "CA_COMPETENCY_PROPERTY";
	
	private String competencyPropertyPrimaryColumnName = "COMPETENCY_ID";
	
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


	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	public Map<String, String> getCompetencyProperties(long competencyId) {
		return extendedPropertyDao.getProperties(competencyPropertyTableName, competencyPropertyPrimaryColumnName, competencyId);
	}

	public void setCompetencyProperties(long competencyId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(competencyPropertyTableName, competencyPropertyPrimaryColumnName, competencyId, props);
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
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_COMPETENCY").getSql(), 
			competencyMapper,
			new SqlParameterValue( Types.NUMERIC, competencyId )
		);
	}


	public Competency getCompetencyByName(Company company, String name) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_NAME").getSql(), 
				competencyMapper,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId() ),
				new SqlParameterValue( Types.VARCHAR, name )
			);
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
	
}
