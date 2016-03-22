package com.podosoftware.competency.competency;

import architecture.ee.exception.ApplicationException;

public class CompetencyAlreadyExistsException extends ApplicationException {

    public CompetencyAlreadyExistsException() {
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(int errorCode) {
	super(errorCode);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(String msg, Throwable cause) {
	super(msg, cause);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(int errorCode, String msg, Throwable cause) {
	super(errorCode, msg, cause);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(String msg) {
	super(msg);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(int errorCode, String msg) {
	super(errorCode, msg);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public CompetencyAlreadyExistsException(int errorCode, Throwable cause) {
	super(errorCode, cause);
	// TODO Auto-generated constructor stub
    }

}
