package com.podosoftware.competency.competency;

import architecture.ee.exception.NotFoundException;

public class AbilityNotFoundException extends NotFoundException {

    public AbilityNotFoundException() {
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(int errorCode, String msg, Throwable cause) {
	super(errorCode, msg, cause);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(int errorCode, String msg) {
	super(errorCode, msg);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(int errorCode, Throwable cause) {
	super(errorCode, cause);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(int errorCode) {
	super(errorCode);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(String msg, Throwable cause) {
	super(msg, cause);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(String msg) {
	super(msg);
	// TODO Auto-generated constructor stub
    }

    public AbilityNotFoundException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

}
