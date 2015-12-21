package com.podosoftware.competency.assessment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.podosoftware.competency.assessment.json.JsonRatingLevelsDeserializer;
import com.podosoftware.competency.assessment.json.JsonRatingLevelsSerializer;

import architecture.common.model.json.JsonMapPropertyDeserializer;
import architecture.common.model.json.JsonMapPropertySerializer;
import architecture.common.model.support.PropertyAndDateAwareSupport;

public class DefaultRatingScheme extends PropertyAndDateAwareSupport implements RatingScheme {

	private int objectType;
	private long objectId;
	private long ratingSchemeId;
	private String name;
	private String description;
	private int scale;
	private List<RatingLevel> ratingLevels;
	
	public DefaultRatingScheme() {
		this.ratingSchemeId = -1L;
		this.name = null;
		this.description = null;
		this.scale = 2;		
		this.ratingLevels = null;
		this.objectType = 0;
		this.objectId = -1L;
		Date now = new Date();
		setCreationDate(now);
		setModifiedDate(now);	
	}

	public Integer getObjectType() {
		return objectType;
	}

	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getRatingSchemeId() {
		return ratingSchemeId;
	}

	public void setRatingSchemeId(long ratingSchemeId) {
		this.ratingSchemeId = ratingSchemeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@JsonSerialize(using = JsonRatingLevelsSerializer.class)	
	public List<RatingLevel> getRatingLevels() {
		return ratingLevels;
	}

	@JsonDeserialize(using = JsonRatingLevelsDeserializer.class)	
	public void setRatingLevels(List<RatingLevel> ratingLevels) {
		this.ratingLevels = ratingLevels;
	}
	
	@JsonDeserialize(using = JsonMapPropertyDeserializer.class)	
	public void setProperties(Map<String, String> properties) {
		super.setProperties(properties);
	}

	@JsonSerialize(using = JsonMapPropertySerializer.class)	
	public Map<String, String> getProperties() {
		return super.getProperties();
	}
	
	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}

	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return ratingSchemeId;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return 65;
	}
	

}
