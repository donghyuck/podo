package com.podosoftware.competency.codeset;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.podosoftware.competency.codeset.json.CustomJsonLongDeserializer;
import com.podosoftware.competency.codeset.json.CustomJsonLongSerializer;

import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.json.JsonMapPropertyDeserializer;
import architecture.common.model.json.JsonMapPropertySerializer;
import architecture.common.model.support.PropertyAwareSupport;

public class DefaultCodeSet extends PropertyAwareSupport implements CodeSet {

    private int objectType;
    private long objectId;

    private long codeSetId;
    private Long parentCodeSetId;
    private String code;
    private String name;
    private String description;
    private boolean enabled;
    private Date creationDate;
    private Date modifiedDate;
    private List<Code> codes;
    private boolean hasChildren;
    private String groupCode;

    public DefaultCodeSet() {
	this.codeSetId = -1L;
	this.parentCodeSetId = -1L;
	this.objectType = -1;
	this.objectId = -1L;
	this.creationDate = new Date();
	this.modifiedDate = this.creationDate;
	this.hasChildren = false;
	this.code = null;
	this.codes = Collections.EMPTY_LIST;
	this.groupCode = null;
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

    @JsonDeserialize(using = JsonMapPropertyDeserializer.class)
    public void setProperties(Map<String, String> properties) {
	super.setProperties(properties);
    }

    @JsonSerialize(using = JsonMapPropertySerializer.class)
    public Map<String, String> getProperties() {
	return super.getProperties();
    }

    public List<Code> getCodes() {
	return codes;
    }

    public void setCodes(List<Code> codes) {
	this.codes = codes;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
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

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return codeSetId;
    }

    public String getGroupCode() {
	return groupCode;
    }

    public void setGroupCode(String groupCode) {
	this.groupCode = groupCode;
    }

    public int getModelObjectType() {
	return 50;
    }

    @Override
    public String toString() {
	return "DefaultCodeSet [codeSetId=" + codeSetId + ", name=" + name + ", enabled=" + enabled + "]";
    }

}
