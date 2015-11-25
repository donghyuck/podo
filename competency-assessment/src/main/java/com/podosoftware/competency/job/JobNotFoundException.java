package com.podosoftware.competency.job;

import java.io.PrintStream;
import java.io.PrintWriter;

import architecture.ee.exception.NotFoundException;

public class JobNotFoundException extends NotFoundException {

	public JobNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public JobNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


}
