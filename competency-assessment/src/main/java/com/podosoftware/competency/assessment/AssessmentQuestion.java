package com.podosoftware.competency.assessment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.Cacheable;

public class AssessmentQuestion implements Cacheable{
	
	private int seq;
	private long competencyId;
	private String competencyName;
	private long essentialElementId;
	private String essentialElementName;
	private int competencyLevel;
	private long questionId;
	private String question;
	private int score;
	
	public AssessmentQuestion() {
		seq = 1;
		competencyId = -1L;
		essentialElementId = -1L;
		competencyLevel = 0;
		score = 0;
		questionId = -1L;
	}

	
	
	public long getQuestionId() {
		return questionId;
	}



	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}



	public int getSeq() {
		return seq;
	}



	public void setSeq(int seq) {
		this.seq = seq;
	}



	public long getCompetencyId() {
		return competencyId;
	}



	public void setCompetencyId(long competencyId) {
		this.competencyId = competencyId;
	}


	@JsonGetter
	public String getCompetencyName() {
		return competencyName;
	}


	@JsonIgnore
	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}


	public long getEssentialElementId() {
		return essentialElementId;
	}



	public void setEssentialElementId(long essentialElementId) {
		this.essentialElementId = essentialElementId;
	}


	@JsonGetter
	public String getEssentialElementName() {
		return essentialElementName;
	}


	@JsonIgnore
	public void setEssentialElementName(String essentialElementName) {
		this.essentialElementName = essentialElementName;
	}



	public int getCompetencyLevel() {
		return competencyLevel;
	}



	public void setCompetencyLevel(int competencyLevel) {
		this.competencyLevel = competencyLevel;
	}


	@JsonGetter
	public String getQuestion() {
		return question;
	}


	public int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}



	@JsonIgnore
	public void setQuestion(String question) {
		this.question = question;
	}


	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}

}
