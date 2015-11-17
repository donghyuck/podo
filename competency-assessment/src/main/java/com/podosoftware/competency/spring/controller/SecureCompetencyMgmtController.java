package com.podosoftware.competency.spring.controller;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.DefaultCodeSet;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyAlreadyExistsException;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.competency.CompetencyNotFoundException;
import com.podosoftware.competency.competency.DefaultCompetency;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.web.spring.controller.MyCloudDataController.ItemList;

@Controller ("secure-competency-data-controller")
@RequestMapping("/secure/data")
public class SecureCompetencyMgmtController {
	
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
	
	
	
	@RequestMapping(value="/mgmt/codeset/list.json", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public List<CodeSet> listCodeSet(@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId ){		
		
		User user = SecurityHelper.getUser();		
		if( companyId > 0 ){
			return codeSetManager.getCodeSets(new CompanyTemplate(companyId));
		}		
		return Collections.EMPTY_LIST;
	}
	
	@RequestMapping(value="/mgmt/codeset/update.json", method=RequestMethod.POST)
	@ResponseBody
	public CodeSet createCodeSet(@RequestBody DefaultCodeSet codeset ){		
		
		User user = SecurityHelper.getUser();
		
		codeSetManager.saveOrUpdate(codeset);
		
		return codeset;
	}

	
	@RequestMapping(value="/mgmt/competency/list.json", method=RequestMethod.POST)
	@ResponseBody
	public ItemList listCompetency(
		@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId,
		@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
		@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,		
		NativeWebRequest request
	){		
		
		User user = SecurityHelper.getUser();
		Company company = user.getCompany();
		if( companyId > 0){
			try {
				company = companyManager.getCompany(companyId);
			} catch (CompanyNotFoundException e) {
			}
		}
		
		List<Competency> items = Collections.EMPTY_LIST;
		int totalCount = competencyManager.getCompetencyCount(company);
		if( totalCount > 0 ){
			if( pageSize > 0)
				items = competencyManager.getCompetencies(company, startIndex, pageSize);
			else
				items = competencyManager.getCompetencies(company);
		}
		
		log.debug(items);
		
		ItemList list = new ItemList(items, totalCount);
		
		return list;
	}
	
	@RequestMapping(value="/mgmt/competency/create.json", method=RequestMethod.POST)
	@ResponseBody
	public Competency createCompetency(@RequestBody DefaultCompetency competency ) throws CompetencyAlreadyExistsException{		
		
		User user = SecurityHelper.getUser();
		Company company = user.getCompany();
		if( competency.getObjectType() == 1 && competency.getObjectId() > 0){
			try {
				company = companyManager.getCompany(competency.getObjectId());
			} catch (CompanyNotFoundException e) {
			}
			return competencyManager.createCompetency(company, competency.getName(), competency.getDescription());
		}
		return competency;
	}
	
	@RequestMapping(value="/mgmt/competency/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Competency updateCompetency(@RequestBody DefaultCompetency competency ) throws CompetencyNotFoundException, CompetencyAlreadyExistsException{		
		
		User user = SecurityHelper.getUser();		
		Company company = user.getCompany();
		Competency competencyToUse = competency;
		if( competencyToUse.getCompetencyId() > 0)
			competencyManager.updateCompetency(competencyToUse);
		else{
			if( competencyToUse.getObjectType() == 1 && competencyToUse.getObjectId() > 0){
				try {
					company = companyManager.getCompany(competency.getObjectId());
				} catch (CompanyNotFoundException e) {
					throw new CompetencyNotFoundException();
				}
				competencyToUse = competencyManager.createCompetency(company, competencyToUse.getName(), competencyToUse.getDescription());
			}
			
		}
		return competencyToUse;
	}	

	
}
