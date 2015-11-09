package com.podosoftware.competency.codeset;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import architecture.common.cache.Cacheable;

public class DefaultCodeSet implements CodeSet, Cacheable {
	private int objectType;
	private long objectId;
	private long codeSetId;
	private long parentCodeSetId;
	private String name;	
    private String description;
	private boolean enabled;
    private Date creationDate;
    private Date modifiedDate;
    private List<Code> codes;
    
	public DefaultCodeSet() {
		this.codeSetId = -1L;
		this.parentCodeSetId = -1L;
		this.objectType = -1;
		this.objectId = -1L;		
		this.creationDate = new Date();
		this.modifiedDate = this.creationDate;
		this.codes = Collections.EMPTY_LIST;
	}	
	
	public DefaultCodeSet(long codeGroupId) {
		this.codeSetId = codeGroupId;
	}
	
	
	public long getParentCodeSetId() {
		return parentCodeSetId;
	}

	public void setParentCodeSetId(long parentCodeSetId) {
		this.parentCodeSetId = parentCodeSetId;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public long getCodeSetId() {
		return codeSetId;
	}

	public void setCodeSetId(long codeSetId) {
		this.codeSetId = codeSetId;
	}

	public List<Code> getCodes() {
		return codes;
	}

	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}



	@Override
	public int getCachedSize() {
		return 0;
	}
	
}
