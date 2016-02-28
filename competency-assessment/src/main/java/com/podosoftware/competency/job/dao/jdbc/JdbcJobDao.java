package com.podosoftware.competency.job.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.DefaultClassification;
import com.podosoftware.competency.job.DefaultJob;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobCompetencyRelationship;
import com.podosoftware.competency.job.JobLevel;
import com.podosoftware.competency.job.dao.JobDao;

import architecture.common.user.Company;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcJobDao extends ExtendedJdbcDaoSupport implements JobDao{

	private String sequencerName = "JOB";
	
	private String jobLevelSequencerName = "JOB_LEVEL";
	
	private String jobPropertyTableName = "CA_JOB_PROPERTY";
	
	private String jobPropertyPrimaryColumnName = "JOB_ID";
	
	private ExtendedPropertyDao extendedPropertyDao;
	
	/*
	 * 
			 Name					   Null?    Type
		 ----------------------------------------- -------- ----------------------------
		 JOB_ID 				   NOT NULL NUMBER(38)
		 OBJECT_TYPE				   NOT NULL NUMBER(38)
		 OBJECT_ID				   NOT NULL NUMBER(38)
		 L_CLASSIFIED_ID			   NOT NULL NUMBER(38)
		 M_CLASSIFIED_ID			   NOT NULL NUMBER(38)
		 S_CLASSIFIED_ID			   NOT NULL NUMBER(38)
		 NAME					   NOT NULL VARCHAR2(255)
		 DESCRIPTION					    VARCHAR2(4000)
		 CREATION_DATE				   NOT NULL TIMESTAMP(6)
		 MODIFIED_DATE				   NOT NULL TIMESTAMP(6)

	 * 
	 */
	
	private final RowMapper<Job>  jobMapper = new RowMapper<Job>(){
		public Job mapRow(ResultSet rs, int rowNum) throws SQLException {		
			DefaultJob job = new DefaultJob();		
			job.setJobId(rs.getLong("JOB_ID"));
			job.setObjectType(rs.getInt("OBJECT_TYPE"));
			job.setObjectId(rs.getLong("OBJECT_ID"));
			job.setClassification(new DefaultClassification(rs.getLong("CLASSIFY_TYPE"), rs.getLong("L_CLASSIFIED_ID"),rs.getLong("M_CLASSIFIED_ID"),rs.getLong("S_CLASSIFIED_ID")));
			job.setName(rs.getString("NAME"));
			job.setDescription(rs.getString("DESCRIPTION"));
			job.setCreationDate(rs.getDate("CREATION_DATE"));
			job.setModifiedDate(rs.getDate("MODIFIED_DATE"));
			return job;
		}		
	};		
	
	private final RowMapper<JobLevel>  jobLevelMapper = new RowMapper<JobLevel>(){
		public JobLevel mapRow(ResultSet rs, int rowNum) throws SQLException {		
			JobLevel jobLevel = new JobLevel();		
			jobLevel.setJobLevelId(rs.getLong("JOB_LEVEL_ID"));
			jobLevel.setJobId(rs.getLong("JOB_ID"));
			jobLevel.setName(rs.getString("NAME"));
			jobLevel.setDescription(rs.getString("DESCRIPTION"));
			jobLevel.setLevel(rs.getInt("JOB_LEVEL"));
			jobLevel.setMinWorkExperienceYear(rs.getInt("MIN_YEAR"));
			jobLevel.setMaxWorkExperienceYear(rs.getInt("MAX_YEAR"));
			jobLevel.setStrong(rs.getInt("IS_STRONG_REL") == 1 ? true : false);
			return jobLevel;
		}		
	};
	
	public JdbcJobDao() {
		
	}

	public String getSequencerName() {
		return sequencerName;
	}

	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public String getJobPropertyTableName() {
		return jobPropertyTableName;
	}

	public void setJobPropertyTableName(String jobPropertyTableName) {
		this.jobPropertyTableName = jobPropertyTableName;
	}

	public String getJobPropertyPrimaryColumnName() {
		return jobPropertyPrimaryColumnName;
	}

	public void setJobPropertyPrimaryColumnName(String jobPropertyPrimaryColumnName) {
		this.jobPropertyPrimaryColumnName = jobPropertyPrimaryColumnName;
	}

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}

	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	
	public Map<String, String> getJobProperties(long jobId) {
		return extendedPropertyDao.getProperties(jobPropertyTableName, jobPropertyPrimaryColumnName, jobId);
	}

	public void setJobProperties(long jobId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(jobPropertyTableName, jobPropertyPrimaryColumnName, jobId, props);
	}
	
	public void deleteJobProperties(long jobId) {
		extendedPropertyDao.deleteProperties(jobPropertyTableName, jobPropertyPrimaryColumnName, jobId);
	}
	

	public Long nextJobId() {
		return getNextId(sequencerName);
	}

	public Long nextJobLevelId(){
		return getNextId(jobLevelSequencerName);
	}


	public void saveOrUpdateJob(Job job) {
		if(job.getJobId() > 0){
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_JOB").getSql(), 
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifyType() ),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMajorityId() ),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMiddleId()),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMinorityId()),
					new SqlParameterValue( Types.VARCHAR, job.getName() ),
					new SqlParameterValue( Types.VARCHAR, job.getDescription() ),
					new SqlParameterValue( Types.TIMESTAMP, job.getModifiedDate()),
					new SqlParameterValue( Types.NUMERIC, job.getJobId() )
			);			
			deleteJobProperties(job.getJobId());
			setJobProperties(job.getJobId(), job.getProperties());
		}else{
			job.setJobId(nextJobId());			
			getExtendedJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.INSERT_JOB").getSql(), 
					new SqlParameterValue( Types.NUMERIC, job.getJobId() ),
					new SqlParameterValue( Types.NUMERIC, job.getObjectType() ),
					new SqlParameterValue( Types.NUMERIC, job.getObjectId() ),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifyType() ),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMajorityId() ),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMiddleId()),
					new SqlParameterValue( Types.NUMERIC, job.getClassification().getClassifiedMinorityId()),
					new SqlParameterValue( Types.VARCHAR, job.getName() ),
					new SqlParameterValue( Types.VARCHAR, job.getDescription() ),
					new SqlParameterValue( Types.TIMESTAMP, job.getCreationDate()),
					new SqlParameterValue( Types.TIMESTAMP, job.getModifiedDate())
			);
			if(job.getProperties().size() > 0)
				setJobProperties(job.getJobId(), job.getProperties());
		}
	}
	

	public Job getJobById(long jobId) {
		Job job = getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_BY_ID").getSql(), 
			jobMapper,
			new SqlParameterValue( Types.NUMERIC, jobId )
		);
		job.setProperties(getJobProperties(job.getJobId()));		
		return job;
	}

	public void deleteJob(Job job) {
				
	}

	public int getJobCount(Company company) {
		return getJobCount(1, company.getCompanyId());
	}
	
	public int getJobCount(int objectType, long objectId){
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.COUNT_JOB_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, objectType),
				new SqlParameterValue( Types.NUMERIC, objectId )
			);
	}

	public List<Long> getJobIds(Company company) {
		return getJobIds(1, company.getCompanyId());
	}

	public List<Long> getJobIds(Company company, int startIndex, int numResults) {
		return getJobIds(1, company.getCompanyId(), startIndex, numResults);
	}

	public List<Long> getJobIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, objectType),
				new SqlParameterValue( Types.NUMERIC, objectId)				
		);
	}
	
	public List<Long> getJobIds(int objectType, long objectId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ objectType, objectId}, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
	}

	public int getJobCount(Company company, Classification classify) {
		return getJobCount(1, company.getCompanyId(), classify);
/*		return getExtendedJdbcTemplate().queryForObject( 
			getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.COUNT_JOB_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(),
			Integer.class,
			new SqlParameterValue( Types.NUMERIC, 1),
			new SqlParameterValue( Types.NUMERIC, company.getCompanyId() )
		);*/
	}
	

	public List<Long> getJobIds(Company company, Classification classify) {
/*		return getExtendedJdbcTemplate().queryForList(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, 1),
				new SqlParameterValue( Types.NUMERIC, company.getCompanyId())				
		);*/		
		return getJobIds(1, company.getCompanyId(), classify);
	}

	public List<Long> getJobIds(Company company, Classification classify, int startIndex, int numResults) {
		
/*		return getExtendedJdbcTemplate().queryScrollable(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				startIndex, 
				numResults, 
				new Object[]{ 1, company.getCompanyId()}, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);*/
		return getJobIds(1, company.getCompanyId(), classify, startIndex, numResults);
	}
	
	
	public int getJobCount(int objectType, long objectId, Classification classify){
		return getExtendedJdbcTemplate().queryForObject( 
				getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.COUNT_JOB_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(),
				Integer.class,
				new SqlParameterValue( Types.NUMERIC, objectType),
				new SqlParameterValue( Types.NUMERIC, objectId )
			);
	}
	
	public List<Long> getJobIds(int objectType, long objectId, Classification classify) {
		return getExtendedJdbcTemplate().queryForList(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, objectType),
				new SqlParameterValue( Types.NUMERIC, objectId)				
		);
	}
	
	public List<Long> getJobIds(int objectType, long objectId, Classification classify, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(getBoundSqlWithAdditionalParameter("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_OBJECT_TYPE_AND_OBJECT_ID_AND_CLASSIFY", classify.toMap()).getSql(), 
				startIndex, 
				numResults, 
				new Object[]{objectType, objectId}, 
				new int[] {Types.NUMERIC, Types.NUMERIC}, 
				Long.class);
		
	}
	
	public void batchInsertJob(List<Job> jobs) {
		final List<Job> inserts = jobs;		
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.INSERT_JOB").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Job job = inserts.get(i);
						
						ps.setLong(1, job.getJobId() );
						ps.setInt(2, job.getObjectType() );
						ps.setLong(3, job.getObjectId());
						ps.setLong(4, job.getClassification().getClassifyType() );
						ps.setLong(5, job.getClassification().getClassifiedMajorityId() );
						ps.setLong(6, job.getClassification().getClassifiedMiddleId());
						ps.setLong(7, job.getClassification().getClassifiedMinorityId());
						ps.setString( 8, job.getName() );
						ps.setString( 9, job.getDescription() );
						ps.setTimestamp(10, new java.sql.Timestamp(job.getCreationDate().getTime()));
						ps.setTimestamp(11, new java.sql.Timestamp(job.getModifiedDate().getTime()));
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}			
	}

	
	
	public void batchInsertJobCompetencyRelationship(List<JobCompetencyRelationship> relationships) {
		final List<JobCompetencyRelationship> inserts = relationships;
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.INSERT_JOB_COMPETENCY_RELATIONSHIP").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						JobCompetencyRelationship relationship = inserts.get(i);		
						ps.setInt(1, relationship.getObjectType() );
						ps.setLong(2, relationship.getObjectId());
						ps.setLong(3, relationship.getJobId() );
						ps.setLong(4, relationship.getJobLevelId() );
						ps.setLong(5, relationship.getCompetencyId());
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}	
	}

	@Override
	public List<Long> getJobCompetencyIds(Job job) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_COMPETENCY_ID_BY_JOB_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, job.getJobId())		
		);
	}

	@Override
	public Long getJobIdByCompetency(Competency competency) {
		Long jobId = 0L;
		if(competency.getCompetencyId() < 1)
			return jobId;
		try {
			jobId = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_ID_BY_COMPETENCY_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, competency.getCompetencyId() ) );
		} catch (DataAccessException e) {
		}
		return jobId;
	}

	
	
	public JobLevel getJobLevelById(long jobLevelId) {
		JobLevel scheme = null;
		try {
			scheme = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_LEVEL_BY_ID").getSql(), 
					jobLevelMapper, 
					new SqlParameterValue(Types.NUMERIC, jobLevelId ) );			
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same JobLevel ID found: ").append(jobLevelId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load JobLevel by ID : ").append(jobLevelId).append(".").toString();
			 log.fatal(message, e);
		}			
		return scheme;
	}

	public List<Long> getJobLevelIds(long jobId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("COMPETENCY_ACCESSMENT.SELECT_JOB_LEVEL_IDS_BY_JOB_ID").getSql(), 
				Long.class,
				new SqlParameterValue( Types.NUMERIC, jobId)		
		);
	}

	public void removeJobLevels(final List<JobLevel> jobLevels) {
		getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.REMOVE_JOB_LEVEL").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						JobLevel jobLevel= jobLevels.get(i);
						ps.setLong(1, jobLevel.getJobLevelId());	
					}					
					public int getBatchSize() {
						return jobLevels.size();
					}
				});	
	}
	
	public void saveOrUpdateJobLevels(List<JobLevel> subjects){	
		final List<JobLevel> inserts = new ArrayList<JobLevel>();		
		final List<JobLevel> updates = new ArrayList<JobLevel>();				
		for(JobLevel jobSel : subjects){
			if(jobSel.getJobLevelId() > 0){
				updates.add(jobSel);
			}else{
				jobSel.setJobLevelId(this.nextJobLevelId());
				inserts.add(jobSel);
			}
		}	
		if(updates.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_JOB_LEVEL").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						JobLevel sbj= updates.get(i);
						ps.setString(1, sbj.getName());	
						ps.setString(2, sbj.getDescription());
						ps.setInt(3, sbj.getMinWorkExperienceYear());
						ps.setInt(4, sbj.getMaxWorkExperienceYear());
						ps.setInt(5, sbj.getLevel());
						ps.setInt(6, sbj.isStrong() ? 1 : 0);
						ps.setLong(7, sbj.getJobLevelId());
					}					
					public int getBatchSize() {
						return updates.size();
					}
				});		
		}	
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.INSERT_JOB_LEVEL").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						JobLevel sbj= inserts.get(i);
						ps.setLong(1, sbj.getJobLevelId());
						ps.setLong(2, sbj.getJobId());
						ps.setString(3, sbj.getName());	
						ps.setString(4, sbj.getDescription());
						ps.setInt(5, sbj.getMinWorkExperienceYear());
						ps.setInt(6, sbj.getMaxWorkExperienceYear());
						ps.setInt(7, sbj.getLevel());
						ps.setInt(8, sbj.isStrong()?1:0);
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}			
	}
	
}
