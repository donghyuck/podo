package com.podosoftware.competency.assessment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;

public class DefaultRatingLevel implements RatingLevel {

    private Long ratingSchemeId;
    private Long ratingLevelId;
    private String title;
    private int score;

    public DefaultRatingLevel(Long ratingSchemeId, Long ratingLevelId, String title, int score) {
	this.ratingSchemeId = ratingSchemeId;
	this.ratingLevelId = ratingLevelId;
	this.title = title;
	this.score = score;
    }

    public DefaultRatingLevel() {
	this.ratingSchemeId = -1L;
	this.ratingLevelId = -1L;
	this.title = null;
	this.score = 0;
    }

    public Long getRatingSchemeId() {
	return ratingSchemeId;
    }

    public void setRatingSchemeId(Long ratingSchemeId) {
	this.ratingSchemeId = ratingSchemeId;
    }

    public Long getRatingLevelId() {
	return ratingLevelId;
    }

    public void setRatingLevelId(Long ratingLevelId) {
	this.ratingLevelId = ratingLevelId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    @JsonIgnore
    public int getCachedSize() {
	int size = CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt() + CacheSizes.sizeOfString(title);
	return size;
    }

    @Override
    public String toString() {
	return "DefaultRatingLevel [ratingSchemeId=" + ratingSchemeId + ", ratingLevelId=" + ratingLevelId + ", title="
		+ title + ", score=" + score + "]";
    }

}
