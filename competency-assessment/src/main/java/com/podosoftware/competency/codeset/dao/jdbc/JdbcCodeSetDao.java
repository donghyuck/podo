package com.podosoftware.competency.codeset.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetTreeWalker;
import com.podosoftware.competency.codeset.DefaultCodeSet;
import com.podosoftware.competency.codeset.DefaultCodeSetTreeWalker;
import com.podosoftware.competency.codeset.dao.CodeSetDao;

import architecture.common.util.LongTree;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCodeSetDao extends ExtendedJdbcDaoSupport implements CodeSetDao {
	
	/*
	 * 		CODESET_ID,
		OBJECT_TYPE,
		OBJECT_ID,
		PARENT_CODESET_ID,
		NAME,
		DESCRIPTION,
		CREATION_DATE,
		MODIFIED_DATE
		
	 */
	
	private final RowMapper<CodeSet> codesetMapper = new RowMapper<CodeSet>(){		
		public CodeSet mapRow(ResultSet rs, int rowNum) throws SQLException {			
			CodeSet g = new DefaultCodeSet();
			g.setCodeSetId(rs.getLong("CODESET_ID"));
			g.setObjectType(rs.getInt("OBJECT_TYPE"));
			g.setObjectId(rs.getLong("OBJECT_ID"));
			g.setParentCodeSetId(rs.getLong("PARENT_CODESET_ID"));
			
			g.setName(rs.getString("NAME"));			
			g.setCode(rs.getString("CODE"));
			g.setDescription(rs.getString("DESCRIPTION"));
			g.setCreationDate( rs.getDate("CREATION_DATE") ); 
			g.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 		
			return g;
		}		
	};
	
	private String sequencerName = "CODESET";	
	private String codesetValueSequencerName = "CODESET_VALUE";
	
	public void batchInsertCodeSet(List<CodeSet> codesets){
		final List<CodeSet> inserts = codesets;		
		if(inserts.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.INSERT_CODESET").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						CodeSet c = inserts.get(i);
						ps.setLong(1, c.getCodeSetId());	
						ps.setInt(2, c.getObjectType());
						ps.setLong(3,  c.getObjectId());
						ps.setLong(4, c.getParentCodeSetId());
						ps.setString(5, c.getName());
						ps.setString(6, c.getCode());
						ps.setString(7, c.getDescription());
						ps.setDate(8, new java.sql.Date(c.getCreationDate().getTime()));
						ps.setDate(9, new java.sql.Date(c.getModifiedDate().getTime()));
					}					
					public int getBatchSize() {
						return inserts.size();
					}
				});		
		}	
	}

	public void batchUpdateCodeSet(List<CodeSet> codesets){
		final List<CodeSet> updates = codesets;		
		if(updates.size() > 0){
			getExtendedJdbcTemplate().batchUpdate(				
				getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_CODESET").getSql(), 
				new BatchPreparedStatementSetter() {					
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						CodeSet c = updates.get(i);		
						ps.setLong(1, c.getParentCodeSetId());
						ps.setString(2, c.getName());
						ps.setString(3, c.getCode());
						ps.setString(4, c.getDescription());
						ps.setDate(5, new java.sql.Date(c.getModifiedDate().getTime()));
						ps.setLong(6, c.getCodeSetId());
					}					
					public int getBatchSize() {
						return updates.size();
					}
				});		
		}	
	}
	
	public void saveOrUpdateCodeSet(List<CodeSet> codesets){		
		List<CodeSet> updates = new ArrayList<CodeSet>();		
		List<CodeSet> inserts = new ArrayList<CodeSet>();		
		for(CodeSet codeSet : codesets)
		{
			if(codeSet.getCodeSetId() < 1 ){
				long codeSetId = getNextId(sequencerName);
				codeSet.setCodeSetId(codeSetId);	
				inserts.add(codeSet);
			}else{
				updates.add(codeSet);
			}
		}		
		batchUpdateCodeSet(updates);
		batchInsertCodeSet(inserts);
	}
	

	public Long newCodeSetId() {
		return getNextId(sequencerName);
	} 
			
	public void saveOrUpdateCodeSet(CodeSet codeset) {		
		if( codeset.getCodeSetId() == -1L ){		
			long codeSetId = getNextId(sequencerName);
			codeset.setCodeSetId(codeSetId);
			getJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.INSERT_CODESET").getSql(),
					new SqlParameterValue (Types.NUMERIC, codeset.getCodeSetId()),
					new SqlParameterValue (Types.NUMERIC, codeset.getObjectType()),	
					new SqlParameterValue (Types.NUMERIC, codeset.getObjectId()),	
					new SqlParameterValue (Types.NUMERIC, codeset.getParentCodeSetId()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getName()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getCode()),
					new SqlParameterValue (Types.VARCHAR, codeset.getDescription()),	
					new SqlParameterValue (Types.TIMESTAMP, codeset.getCreationDate()),	
					new SqlParameterValue (Types.TIMESTAMP, codeset.getModifiedDate())				
					);
		}else{
			getJdbcTemplate().update(getBoundSql("COMPETENCY_ACCESSMENT.UPDATE_CODESET").getSql(),
					new SqlParameterValue (Types.NUMERIC, codeset.getParentCodeSetId()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getName()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getCode()),
					new SqlParameterValue (Types.VARCHAR, codeset.getDescription()),			
					new SqlParameterValue (Types.TIMESTAMP, codeset.getModifiedDate()),
					new SqlParameterValue (Types.NUMERIC, codeset.getCodeSetId())
			);			
		}
	}
	/*
	CODESET_ID
	OBJECT_TYPE
	OBJECT_ID
	PARENT_CODESET_ID
	NAME
	DESCRIPTION
	CREATION_DATE
	MODIFIED_DATE
	*/
	public CodeSetTreeWalker getCodeSetTreeWalker(int objectType, long objectId){		
		int numCodeSets = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("COMPETENCY_ACCESSMENT.COUNT_CODESET_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		numCodeSets++;	
		
		final LongTree tree = new LongTree(-1L, numCodeSets);	
		
		getExtendedJdbcTemplate().query(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_ROOT_CODESET").getSql(), 
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException {					
							long commentId = rs.getLong(1);							
							tree.addChild(-1L, commentId);
							log.debug("tree add : " + commentId );				
				}},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		
		getExtendedJdbcTemplate().query(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_CHILD_CODESET").getSql(), 
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException {						
							long commentId = rs.getLong(1);
							long parentCommentId = rs.getLong(2);
							tree.addChild(parentCommentId, commentId);
							log.debug("tree add : " + parentCommentId + "," +  commentId );				
					}},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		
		log.debug("total:" + tree.getChildCount(-1L));
		StringBuilder sb = new StringBuilder();
		for( long id : tree.getRecursiveChildren(-1L) ){
			sb.append(id).append(", ");
		}
		
		log.debug( sb.toString());
		
		return new DefaultCodeSetTreeWalker(objectType, objectId, tree);		
	}

	public List<Long> getCodeSetIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("COMPETENCY_ACCESSMENT.SELECT_CODESET_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Long.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}


	@Override
	public CodeSet getCodeSetById(long codesetId) {
		CodeSet codeset = null;
		try {
			codeset = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("COMPETENCY_ACCESSMENT.SELECT_CODESET_BY_ID").getSql(), 
					codesetMapper, 
					new SqlParameterValue(Types.NUMERIC, codesetId ) );
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same codeset ID found: ").append(codesetId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load codeset by ID : ").append(codesetId).append(".").toString();
			 log.fatal(message, e);
		}			
		return codeset;
	}


	@Override
	public int getCodeSetCount(int objectType, long objectId) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("COMPETENCY_ACCESSMENT.COUNT_CODESET_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
				
	}

	
}
