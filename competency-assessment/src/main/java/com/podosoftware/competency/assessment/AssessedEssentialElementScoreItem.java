package com.podosoftware.competency.assessment;

public class AssessedEssentialElementScoreItem {

	private long competencyId;
	private long essentialElementId;
	private int totalScore ;
	private int totalCount ;
	private double finalScore;
	
	public AssessedEssentialElementScoreItem() {
		competencyId = -1L;
		essentialElementId = -1L;
		totalScore = 0;
		totalCount = 0;
		finalScore = 0;		
	}

	public long getCompetencyId() {
		return competencyId;
	}

	public void setCompetencyId(long competencyId) {
		this.competencyId = competencyId;
	}

	public long getEssentialElementId() {
		return essentialElementId;
	}

	public void setEssentialElementId(long essentialElementId) {
		this.essentialElementId = essentialElementId;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}

	
}
