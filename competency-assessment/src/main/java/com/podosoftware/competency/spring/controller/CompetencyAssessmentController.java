package com.podosoftware.competency.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.podosoftware.competency.assessment.Assessment;
import com.podosoftware.competency.assessment.AssessmentManager;
import com.podosoftware.competency.assessment.AssessmentNotFoundException;
import com.podosoftware.competency.assessment.JobSelection;
import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.DefaultClassification;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobManager;

import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;

@Controller ("competency-assessment-data-controller")
@RequestMapping("/data/me/competency")
public class CompetencyAssessmentController {

	
	private Log log = LogFactory.getLog(getClass());
	
	@Inject
	@Qualifier("codeSetManager")
	private CodeSetManager codeSetManager;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	@Inject
	@Qualifier("competencyManager")
	private CompetencyManager competencyManager;

	
	@Inject
	@Qualifier("jobManager")
	private JobManager jobManager;

	@Inject
	@Qualifier("assessmentManager")
	private AssessmentManager assessmentManager;
	
	
	public JobManager getJobManager() {
		return jobManager;
	}

	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	public CodeSetManager getCodeSetManager() {
		return codeSetManager;
	}

	public void setCodeSetManager(CodeSetManager codeSetManager) {
		this.codeSetManager = codeSetManager;
	}

	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public CompetencyManager getCompetencyManager() {
		return competencyManager;
	}

	public void setCompetencyManager(CompetencyManager competencyManager) {
		this.competencyManager = competencyManager;
	}
	

	public AssessmentManager getAssessmentManager() {
		return assessmentManager;
	}

	public void setAssessmentManager(AssessmentManager assessmentManager) {
		this.assessmentManager = assessmentManager;
	}
	
	public CompetencyAssessmentController() {
	}

	@RequestMapping(value="/assessment/list.json", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public List<Assessment> listAssessment(){	
		User user = SecurityHelper.getUser();	
		if(!user.isAnonymous()){			
			List<Assessment> list = assessmentManager.getUserAssessments(user);
			Collections.sort(list, new Comparator<Assessment>(){
				public int compare(Assessment o1, Assessment o2) {
					if( o1.getStartDate().after( o2.getStartDate() ) ){
						return 1;
					}
					return 0;
				}});
			return list;
		}
		return Collections.EMPTY_LIST;
	}
	
	@RequestMapping(value="/assessment/job/list.json", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public List<Job> listJob(
			@RequestParam(value="assessmentId", defaultValue="0", required=false ) Integer assessmentId) throws AssessmentNotFoundException{	
		User user = SecurityHelper.getUser();		
		Assessment assessment = assessmentManager.getAssessment(assessmentId);
		int objectType = assessment.getObjectType();
		long objectId = assessment.getObjectId();			
		List<Job> list = new ArrayList<Job>();		
		for( JobSelection jobSelection : assessment.getJobSelections() ){				
			Classification classify = new DefaultClassification(jobSelection.getClassifyType(), jobSelection.getClassifiedMajorityId() , jobSelection.getClassifiedMiddleId(), jobSelection.getClassifiedMinorityId());	
			List<Job> jobs = jobManager.getJobs(objectType, objectId, classify);
			list.addAll(jobs);
		}
		return list;
	}
}
