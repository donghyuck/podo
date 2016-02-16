package com.podosoftware.competency.assessment;

public class AssessedEssentialElementScore {

	private long assessmentId;
	private long competencyId;
	private String competencyName;
	private long essentialElementId;
	private String essentialElementName;
	private long candidateId;
	private long assessorId;
	private int totalScore ;
	private int totalCount ;
	private double finalScore;
	private double othersAverageScore;
	
	
	public AssessedEssentialElementScore() {
		assessmentId = -1L;
		competencyId = -1L;
		essentialElementId = -1L;
		candidateId = -1L;
		assessorId = -1L;
		totalScore = 0;
		totalCount = 0;
		finalScore = 0;
		othersAverageScore = 0 ;
	}


	public double getOthersAverageScore() {
		return othersAverageScore;
	}


	public void setOthersAverageScore(double othersAverageScore) {
		this.othersAverageScore = othersAverageScore;
	}


	public long getAssessmentId() {
		return assessmentId;
	}


	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}


	public long getCompetencyId() {
		return competencyId;
	}


	public void setCompetencyId(long competencyId) {
		this.competencyId = competencyId;
	}


	public String getCompetencyName() {
		return competencyName;
	}


	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}


	public long getEssentialElementId() {
		return essentialElementId;
	}


	public void setEssentialElementId(long essentialElementId) {
		this.essentialElementId = essentialElementId;
	}


	public String getEssentialElementName() {
		return essentialElementName;
	}


	public void setEssentialElementName(String essentialElementName) {
		this.essentialElementName = essentialElementName;
	}


	public long getCandidateId() {
		return candidateId;
	}


	public void setCandidateId(long candidateId) {
		this.candidateId = candidateId;
	}


	public long getAssessorId() {
		return assessorId;
	}


	public void setAssessorId(long assessorId) {
		this.assessorId = assessorId;
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


	@Override
	public String toString() {
		return "AssessedEssentialElementSummary [assessmentId=" + assessmentId + ", competencyId=" + competencyId
				+ ", competencyName=" + competencyName + ", essentialElementId=" + essentialElementId
				+ ", essentialElementName=" + essentialElementName + ", candidateId=" + candidateId + ", assessorId="
				+ assessorId + ", totalScore=" + totalScore + ", totalCount=" + totalCount + ", finalScore="
				+ finalScore + "]";
	}

}
