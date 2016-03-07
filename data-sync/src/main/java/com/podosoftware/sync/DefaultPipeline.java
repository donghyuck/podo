package com.podosoftware.sync;

import architecture.common.adaptor.Pipeline;

public class DefaultPipeline implements Pipeline {

	/**
	 * @uml.property  name="name"
	 */
	protected String name;
	
	/**
	 * @uml.property  name="index"
	 */
	protected int index;
	
	/**
	 * @uml.property  name="match"
	 */
	protected boolean match;
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return
	 * @uml.property  name="match"
	 */
	public boolean isMatch() {
		return match;
	}

	/**
	 * @param match
	 * @uml.property  name="match"
	 */
	public void setMatch(boolean match) {
		this.match = match;
	}

}