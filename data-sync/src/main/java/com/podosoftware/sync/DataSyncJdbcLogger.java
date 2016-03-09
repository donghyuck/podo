package com.podosoftware.sync;

import java.sql.Types;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DataSyncJdbcLogger extends JdbcDaoSupport implements DataSyncLogger {

	private static final Log log = LogFactory.getLog(DataSyncJdbcLogger.class);
	
	private String queryString ;
	
	public DataSyncJdbcLogger() {
		
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	@Override
	public void write(SyncLog syncLog) {
		log.debug(syncLog);
		getJdbcTemplate().update(
			queryString, 
			new SqlParameterValue (Types.VARCHAR, syncLog.getUid()),			
			new SqlParameterValue (Types.VARCHAR, syncLog.getState().name().toUpperCase()),
			new SqlParameterValue (Types.NUMERIC, syncLog.getRowCount()),
			new SqlParameterValue (Types.VARCHAR, syncLog.getErrorMsg()),
			new SqlParameterValue (Types.TIMESTAMP, syncLog.getDate()),			
			new SqlParameterValue (Types.VARCHAR, syncLog.getUid()),
			new SqlParameterValue (Types.VARCHAR, syncLog.getService()),
			new SqlParameterValue (Types.VARCHAR, syncLog.getState().name().toUpperCase()),
			new SqlParameterValue (Types.NUMERIC, syncLog.getRowCount()),
			new SqlParameterValue (Types.VARCHAR, syncLog.getErrorMsg()),
			new SqlParameterValue (Types.TIMESTAMP, syncLog.getDate())		
		);		
	}

	@Override
	public SyncLog newSyncLog(String guid, String service) {
		return new SyncLog( guid, service );
	}

	@Override
	public SyncLog newSyncLog() {
		return new SyncLog();
	}
	
	

}
