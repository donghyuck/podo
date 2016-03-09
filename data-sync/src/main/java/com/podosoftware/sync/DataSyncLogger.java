package com.podosoftware.sync;

import java.util.Date;

public interface DataSyncLogger {

	
	public void write( SyncLog log );
	
	public SyncLog newSyncLog();
	
	public SyncLog newSyncLog(String guid, String service);
	
	
	public enum State {		
		SUCCESS,
		FAIL,
		NONE
	}
	
	public static class SyncLog {
		
		private String uid ;
		
		private State state ;
		
		private String service ;
		
		private int rowCount;
		
		private Date date;		
		
		private String errorMsg;

		private Throwable error;
		
		public SyncLog() {
			this.rowCount = 0;
			this.date = new Date();
			this.state = State.NONE;
			
		}
		
		public SyncLog(String uid, String service) {
			this.uid = uid;
			this.service = service;
			this.rowCount = 0;
			this.date = new Date();
			this.state = State.NONE;			
		}
		

		public Throwable getError() {
			return error;
		}

		public void setError(Throwable error) {
			this.error = error;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public State getState() {
			return state;
		}

		public void setState(State state) {
			this.state = state;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public int getRowCount() {
			return rowCount;
		}

		public void setRowCount(int rowCount) {
			this.rowCount = rowCount;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String error) {
			this.errorMsg = error;
		}

		@Override
		public String toString() {
			return "SyncLog [uid=" + uid + ", state=" + state.name().toUpperCase() + ", service=" + service + ", rowCount=" + rowCount
					+ ", date=" + date + ", error=" + errorMsg + "]";
		}
		
		
	
	}
	
}
