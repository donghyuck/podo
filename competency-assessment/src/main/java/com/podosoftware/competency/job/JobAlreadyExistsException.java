package com.podosoftware.competency.job;

import architecture.ee.exception.ApplicationException;

public class JobAlreadyExistsException extends ApplicationException {

    public JobAlreadyExistsException() {
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(int errorCode) {
	super(errorCode);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(String msg, Throwable cause) {
	super(msg, cause);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(int errorCode, String msg, Throwable cause) {
	super(errorCode, msg, cause);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(String msg) {
	super(msg);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(int errorCode, String msg) {
	super(errorCode, msg);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public JobAlreadyExistsException(int errorCode, Throwable cause) {
	super(errorCode, cause);
	// TODO Auto-generated constructor stub
    }

}
