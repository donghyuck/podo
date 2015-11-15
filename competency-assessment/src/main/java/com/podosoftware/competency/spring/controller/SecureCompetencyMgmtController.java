package com.podosoftware.competency.spring.controller;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.DefaultCodeSet;

import architecture.common.user.CompanyTemplate;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;



@Controller ("secure-competency-data-controller")
@RequestMapping("/secure/data")
public class SecureCompetencyMgmtController {

	
	@Inject
	@Qualifier("codeSetManager")
	private CodeSetManager codeSetManager;
	
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

	public CodeSetManager getCodeSetManager() {
		return codeSetManager;
	}

	public void setCodeSetManager(CodeSetManager codeSetManager) {
		this.codeSetManager = codeSetManager;
	}
	
	
}
