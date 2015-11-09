package com.podosoftware.competency.codeset.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameterValue;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetTreeWalker;
import com.podosoftware.competency.codeset.DefaultCodeSetTreeWalker;
import com.podosoftware.competency.codeset.dao.CodeSetDao;

import architecture.common.util.LongTree;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCodeSetDao extends ExtendedJdbcDaoSupport implements CodeSetDao {

	private String sequencerName = "CODESET";
	
	private String codesetValueSequencerName = "CODESET_VALUE";

	@Override
	public void saveOrUpdateCodeSet(CodeSet codeset) {
		
		if( codeset.getCodeSetId() == -1L ){		
			long codeSetId = getNextId(sequencerName);
			codeset.setCodeSetId(codeSetId);
			getJdbcTemplate().update(getBoundSql("COMPETENCY.INSERT_CODESET").getSql(),
					new SqlParameterValue (Types.NUMERIC, codeset.getCodeSetId()),
					new SqlParameterValue (Types.NUMERIC, codeset.getObjectType()),	
					new SqlParameterValue (Types.NUMERIC, codeset.getObjectId()),	
					new SqlParameterValue (Types.NUMERIC, codeset.getParentCodeSetId()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getName()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getDescription()),	
					new SqlParameterValue (Types.TIMESTAMP, codeset.getCreationDate()),	
					new SqlParameterValue (Types.TIMESTAMP, codeset.getModifiedDate())				
					);
		}else{
			getJdbcTemplate().update(getBoundSql("COMPETENCY.UPDATE_CODESET").getSql(),
					new SqlParameterValue (Types.NUMERIC, codeset.getParentCodeSetId()),	
					new SqlParameterValue (Types.VARCHAR, codeset.getName()),	
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
				getBoundSql("COMPETENCY.COUNT_CODESET_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		numCodeSets++;	
		
		final LongTree tree = new LongTree(-1L, numCodeSets);	
		
		getExtendedJdbcTemplate().query(
				getBoundSql("COMPETENCY.SELECT_ROOT_CODESET").getSql(), 
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException {					
							long commentId = rs.getLong(1);							
							tree.addChild(-1L, commentId);
							log.debug("tree add : " + commentId );				
				}},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		
		getExtendedJdbcTemplate().query(
				getBoundSql("COMPETENCY.SELECT_CHILD_CODESET").getSql(), 
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
}
