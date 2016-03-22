package com.podosoftware.competency.competency;

import architecture.ee.exception.NotFoundException;

public class PerformanceCriteriaNotFoundException extends NotFoundException {

    public PerformanceCriteriaNotFoundException() {
	super();
    }

    public PerformanceCriteriaNotFoundException(int errorCode, String msg, Throwable cause) {
	super(errorCode, msg, cause);
    }

    public PerformanceCriteriaNotFoundException(int errorCode, String msg) {
	super(errorCode, msg);
    }

    public PerformanceCriteriaNotFoundException(int errorCode, Throwable cause) {
	super(errorCode, cause);
    }

    public PerformanceCriteriaNotFoundException(int errorCode) {
	super(errorCode);
    }

    public PerformanceCriteriaNotFoundException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public PerformanceCriteriaNotFoundException(String msg) {
	super(msg);
	// TODO Auto-generated constructor stub
    }

    public PerformanceCriteriaNotFoundException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

}
