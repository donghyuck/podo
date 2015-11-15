package com.podosoftware.competency.codeset;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.podosoftware.competency.codeset.json.CustomJsonLongDeserializer;
import com.podosoftware.competency.codeset.json.CustomJsonLongSerializer;

import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;

public class DefaultCodeSet implements CodeSet {
	
	private int objectType;
	private long objectId;
	private long codeSetId;
	private Long parentCodeSetId;
	private String name;	
    private String description;
	private boolean enabled;
    private Date creationDate;
    private Date modifiedDate;
    private List<Code> codes;    
    private boolean hasChildren;
    
	public DefaultCodeSet() {
		this.codeSetId = -1L;
		this.parentCodeSetId = -1L;
		this.objectType = -1;
		this.objectId = -1L;		
		this.creationDate = new Date();
		this.modifiedDate = this.creationDate;
		this.hasChildren = false;
		this.codes = Collections.EMPTY_LIST;
	}	
	
	public DefaultCodeSet(long codeGroupId) {
		this.codeSetId = codeGroupId;
	}
	
	@JsonSerialize(using = CustomJsonLongSerializer.class)	
	public Long getParentCodeSetId() {
		return parentCodeSetId;
	}

	@JsonDeserialize(using = CustomJsonLongDeserializer.class)	
	public void setParentCodeSetId(Long parentCodeSetId) {
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

	@JsonSerialize(using = CustomJsonDateSerializer.class)	
	public Date getCreationDate() {
		return creationDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)	
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
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

	@JsonDeserialize(using = CustomJsonLongDeserializer.class)	
	public void setCodeSetId(long codeSetId) {
		this.codeSetId = codeSetId;
	}

	public List<Code> getCodes() {
		return codes;
	}

	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}



	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}
	
}
