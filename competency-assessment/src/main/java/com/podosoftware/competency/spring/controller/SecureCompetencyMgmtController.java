package com.podosoftware.competency.spring.controller;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.podosoftware.competency.assessment.AssessmentManager;
import com.podosoftware.competency.assessment.AssessmentScheme;
import com.podosoftware.competency.assessment.DefaultAssessmentScheme;
import com.podosoftware.competency.assessment.DefaultRatingScheme;
import com.podosoftware.competency.assessment.RatingLevel;
import com.podosoftware.competency.assessment.RatingScheme;
import com.podosoftware.competency.assessment.RatingSchemeNotFoundException;
import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetManager.CodeItem;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.codeset.DefaultCodeSet;
import com.podosoftware.competency.competency.Ability;
import com.podosoftware.competency.competency.AbilityNotFoundException;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyAlreadyExistsException;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.competency.CompetencyNotFoundException;
import com.podosoftware.competency.competency.CompetencyType;
import com.podosoftware.competency.competency.DefaultAbility;
import com.podosoftware.competency.competency.DefaultCompetency;
import com.podosoftware.competency.competency.DefaultEssentialElement;
import com.podosoftware.competency.competency.DefaultPerformanceCriteria;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.competency.EssentialElementNotFoundException;
import com.podosoftware.competency.competency.PerformanceCriteria;
import com.podosoftware.competency.competency.PerformanceCriteriaNotFoundException;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.DefaultClassification;
import com.podosoftware.competency.job.DefaultJob;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobManager;
import com.podosoftware.competency.job.JobNotFoundException;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.CompanyTemplate;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.component.impl.ExcelReader;
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

	@RequestMapping(value="/mgmt/competency/codeset/list.json", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public List<CodeSet> listCodeSet(
		@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId,
		@RequestParam(value="codeSetId", defaultValue="0", required=false ) Integer codeSetId
			){		
		
		User user = SecurityHelper.getUser();		
		if( codeSetId > 0 ){
			try {
				CodeSet codeset = codeSetManager.getCodeSet(codeSetId);
				return codeSetManager.getCodeSets(codeset);
			} catch (CodeSetNotFoundException e) {
			}
		}
		
		if( companyId > 0 ){
			return codeSetManager.getCodeSets(new CompanyTemplate(companyId));
		}	
		
		return Collections.EMPTY_LIST;
	}
	
	@RequestMapping(value="/mgmt/competency/codeset/update.json", method=RequestMethod.POST)
	@ResponseBody
	public CodeSet updateCodeSet(@RequestBody DefaultCodeSet codeset ){		
		User user = SecurityHelper.getUser();
		if( codeset.getParentCodeSetId() == null)
			codeset.setParentCodeSetId(-1L);
		
		codeSetManager.saveOrUpdate(codeset);
		return codeset;
	}
	
	private String getStringCellValue(ExcelReader reader, Cell cell){
		if( reader.isStringCell(cell))
			return reader.getStringCellValue(cell);
		else if( reader.isBooleanCell(cell))
			return Boolean.toString(reader.getBooleanCellValue(cell));
		else if( reader.isNumericCell(cell))
			return Integer.toString( (int) reader.getNumericCellValue(cell) );
		else 
			return cell.toString();		
	}
	
	@RequestMapping(value="/mgmt/competency/codeset/import.json", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, CodeItem> importExcle(		
		@RequestParam(value="type", defaultValue="0", required=false ) Integer type,		
		@RequestParam(value="codeSetId", defaultValue="0", required=false ) Integer codeSetId,		
		@RequestParam(value="sheetIndex", defaultValue="0", required=false ) Integer sheetIndex,
		@RequestParam(value="skipColumnCount", defaultValue="0", required=false ) Integer skipColumnCount,	
		@RequestParam(value="skipRowCount", defaultValue="1", required=false ) Integer skipRowCount,
		
		MultipartHttpServletRequest request) throws Exception {
		log.debug("codeSetId " + codeSetId );		
		log.debug("skipColumnCount " + skipColumnCount );	
		log.debug("skipRowCount " + skipRowCount );	
		Iterator<String> names = request.getFileNames(); 		
		Map<String, CodeItem> codes = new HashMap<String, CodeItem>();		
		
		while(names.hasNext()){
			String fileName = names.next();	
			log.debug(fileName);			
			MultipartFile mpf = request.getFile(fileName);
			BufferedInputStream is = new BufferedInputStream(mpf.getInputStream()); 
			
			log.debug("file name: " + mpf.getOriginalFilename());
			log.debug("file size: " + mpf.getSize());
			log.debug("file type: " + mpf.getContentType());
			
			if( type == 1 || type == 2){			
				ExcelReader reader = new ExcelReader(is);		
				if( sheetIndex > 0)
					reader.setSheetIndex(sheetIndex);
				
				int rowCount = reader.getPhysicalNumberOfRows();	
				int firstRowCount = reader.getFirstRowNum();			
				log.debug("row:"+ firstRowCount + "-" + rowCount);
				if ( type == 1){
					for( int i = skipRowCount ; i < rowCount ; i ++ ){				
						Row row = reader.getRow(i);
						if( row != null ){
							int columnCount = row.getPhysicalNumberOfCells();
							int firstColumnCount = row.getFirstCellNum();
							//log.debug("column[" + columnCount + "]:"+ firstColumnCount );
							if( columnCount > 0){						
								String l_code = getStringCellValue(reader, row.getCell(0));
								String m_code = getStringCellValue(reader, row.getCell(2));
								String s_code = getStringCellValue(reader, row.getCell(4));
								String job_code = getStringCellValue(reader, row.getCell(6));
								
								String l_code_name = getStringCellValue(reader, row.getCell(1));
								String m_code_name = getStringCellValue(reader, row.getCell(3));
								String s_code_name = getStringCellValue(reader, row.getCell(5));				
								String job_name = getStringCellValue(reader, row.getCell(7));				
								
								String competencyUnitCode = getStringCellValue(reader, row.getCell(8));	
								String competencyUnitTitle = getStringCellValue(reader, row.getCell(9));	
								String competencyLevel = getStringCellValue(reader, row.getCell(10));
								
								if(!codes.containsKey(l_code)){
									codes.put(l_code, new CodeItem(l_code_name, l_code));			
								}				
								CodeItem l_item = codes.get(l_code);
								if( !l_item.getItems().containsKey(m_code)){
									l_item.getItems().put(m_code, new CodeItem(m_code_name, m_code));						
								}				
								CodeItem m_item = l_item.getItems().get(m_code);
								if( !m_item.getItems().containsKey(s_code)){
									m_item.getItems().put(s_code, new CodeItem(s_code_name, s_code));
								}
								
								CodeItem s_item = m_item.getItems().get(s_code);
								if( !s_item.getItems().containsKey(job_code)){
									s_item.getItems().put(job_code, new CodeItem("job", job_name, job_code ));
								}		
								
								log.debug("competencyUnitCode:" + competencyUnitCode);
								
								CodeItem job_item = s_item.getItems().get(job_code);
								if( !job_item.getItems().containsKey(competencyUnitCode)){						
									job_item.getItems().put(competencyUnitCode, new CodeItem("competency", competencyUnitTitle, competencyUnitCode, competencyLevel ));
								}
							}
						}
					}
				}else if ( type == 2){
					Integer key = 0; 
					for( int i = skipRowCount ; i < rowCount ; i ++ ){				
						Row row = reader.getRow(i);
						if( row != null ){
							key = key + 1;
							int columnCount = row.getPhysicalNumberOfCells();
							int firstColumnCount = row.getFirstCellNum();
							if( columnCount > 0){
								String code = getStringCellValue(reader, row.getCell(8));	
								String name = getStringCellValue(reader, row.getCell(11));	
								String level = getStringCellValue(reader, row.getCell(12));	
								codes.put(key.toString(), new CodeItem("element", name, code, level, null));
							}					
						}
					}
				}	
			}
		}		
		CodeSet codeset = codeSetManager.getCodeSet(codeSetId);
		if( type == 1){			
			codeSetManager.batchUpdate(codeset, new ArrayList<CodeItem>(codes.values()));
		}else if (type == 2){
			codeSetManager.batchUpdateForEssentialElement(codeset, new ArrayList<CodeItem>(codes.values()));
		}		
		return codes;
	}

	
	
	@RequestMapping(value="/mgmt/competency/list.json", method=RequestMethod.POST)
	@ResponseBody
	public ItemList listCompetency(
		
		@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId,
		@RequestParam(value="classifiedMajorityId", defaultValue="0", required=false ) Long classifiedMajorityId,
		@RequestParam(value="classifiedMiddleId", defaultValue="0", required=false ) Long classifiedMiddleId,	
		@RequestParam(value="classifiedMinorityId", defaultValue="0", required=false ) Long classifiedMinorityId,			
		@RequestParam(value="jobId", defaultValue="0", required=false ) Long jobId,		
		@RequestParam(value="competencyType", defaultValue="0", required=false ) Integer competencyType,	
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
		
		CompetencyType competencyTypeToUse = CompetencyType.getCompetencyTypeById(competencyType);		
		Classification classify = new DefaultClassification(classifiedMajorityId, classifiedMiddleId, classifiedMinorityId);
		int totalCount = 0 ;
		List<Competency> items = Collections.EMPTY_LIST;		
		if( jobId > 0){
			try {
				Job job = jobManager.getJob(jobId);
				totalCount = competencyManager.getCompetencyCount(job);
				if( totalCount > 0 ){
					if( pageSize > 0)
						items = competencyManager.getCompetencies(job, startIndex, pageSize);
					else
						items = competencyManager.getCompetencies(job);				
				}
			} catch (JobNotFoundException e) { }
		}else if( classify.getClassifiedMajorityId() > 0 || classify.getClassifiedMiddleId() > 0 || classify.getClassifiedMinorityId() > 0)
		{
			totalCount = competencyManager.getCompetencyCount(company, classify);
			if( totalCount > 0 ){
				if( pageSize > 0)
					items = competencyManager.getCompetencies(company, classify, startIndex, pageSize);
				else
					items = competencyManager.getCompetencies(company, classify);				
			}
		}else{
			totalCount = competencyManager.getCompetencyCount(company, competencyTypeToUse);
			if( totalCount > 0 ){
				if( pageSize > 0)
					items = competencyManager.getCompetencies(company, competencyTypeToUse, startIndex, pageSize);
				else
					items = competencyManager.getCompetencies(company, competencyTypeToUse);
			}
		}
		
		DefaultJob emptyJob = new DefaultJob();
		emptyJob.setClassification(new DefaultClassification());		
		for(Competency competency : items){
			if( competency.getCompetencyType() == CompetencyType.JOB || competency.getCompetencyType() == CompetencyType.NONE){
				try {					
					((DefaultCompetency)competency).setJob(jobManager.getJob(competency));
				} catch (JobNotFoundException e) {
					((DefaultCompetency)competency).setJob(emptyJob);
				}
			}else{
				((DefaultCompetency)competency).setJob(emptyJob);
			}
			
			log.debug(competency.toString());
		}		
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
		
		if(competencyToUse.getObjectType() == 0 || competencyToUse.getObjectId() == 0 ){
			throw new IllegalArgumentException();
		}
		competencyManager.saveOrUpdate(competencyToUse);
		return competencyToUse;
	}
	
	
	@RequestMapping(value="/mgmt/competency/job/list.json", method=RequestMethod.POST)
	@ResponseBody
	public ItemList listJob(
		@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId,
		@RequestParam(value="classifiedMajorityId", defaultValue="0", required=false ) Long classifiedMajorityId,
		@RequestParam(value="classifiedMiddleId", defaultValue="0", required=false ) Long classifiedMiddleId,	
		@RequestParam(value="classifiedMinorityId", defaultValue="0", required=false ) Long classifiedMinorityId,					
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
		
		List<Job> items = Collections.EMPTY_LIST;
		int totalCount = 0 ;
		
		Classification classify = new DefaultClassification(classifiedMajorityId, classifiedMiddleId, classifiedMinorityId);
		if( classify.getClassifiedMajorityId() > 0 || classify.getClassifiedMiddleId() > 0 || classify.getClassifiedMinorityId() > 0)
		{
			totalCount = jobManager.getJobCount(company, classify);
			if( totalCount > 0 ){
				if( pageSize > 0){
					items = jobManager.getJobs(company, classify, startIndex, pageSize);
				}else{
					items = jobManager.getJobs(company, classify);
				}
			}	
			
		} else {
			totalCount = jobManager.getJobCount(company);
			if( totalCount > 0 ){
				if( pageSize > 0){
					items = jobManager.getJobs(company, startIndex, pageSize);
				}else{
					items = jobManager.getJobs(company);
				}
			}		
		}
		ItemList list = new ItemList(items, totalCount);		
		return list;
	}	
	
	@RequestMapping(value="/mgmt/competency/job/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Job updateJob(
			@RequestBody DefaultJob job
			) throws JobNotFoundException{
		Job jobToUse = job;
		
		jobManager.saveOrUpdate(jobToUse);
		
		return jobToUse;
	}
	
	@RequestMapping(value="/mgmt/competency/job/competencies/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Competency> listJobCompetency(
			@RequestParam(value="jobId", defaultValue="0", required=true ) Long jobId
			) throws JobNotFoundException{
		Job elementToUse = jobManager.getJob(jobId);		
		return jobManager.getJobCompetencies(elementToUse);
	}	
	
	@RequestMapping(value="/mgmt/competency/element/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<EssentialElement> getEssentialElements(
			@RequestParam(value="competencyId", defaultValue="0", required=false ) Long competencyId
	) throws CompetencyNotFoundException{
		Competency competency = competencyManager.getCompetency(competencyId);
		return competencyManager.getEssentialElements(competency);
	}
	
	@RequestMapping(value="/mgmt/competency/element/update.json", method=RequestMethod.POST)
	@ResponseBody
	public EssentialElement updateEssentialElement(
			@RequestBody DefaultEssentialElement element
			) throws EssentialElementNotFoundException, CompetencyNotFoundException{
		EssentialElement elementToUse = element;
		Competency competencyToUse = competencyManager.getCompetency(element.getCompetencyId());
		competencyManager.saveOrUpdate(elementToUse);		
		return elementToUse;
	}
	
	
	
	@RequestMapping(value="/mgmt/competency/performance-criteria/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<PerformanceCriteria> listPerformanceCriteria(
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId
	) {
		if( objectType < 1 || objectId < 1)
			return Collections.EMPTY_LIST;		
		return competencyManager.getPerformanceCriterias(objectType, objectId);
	}
	
	
	@RequestMapping(value="/mgmt/competency/performance-criteria/update.json", method=RequestMethod.POST)
	@ResponseBody
	public PerformanceCriteria updatePerformanceCriteria(
			@RequestBody DefaultPerformanceCriteria performanceCriteria
			) throws PerformanceCriteriaNotFoundException{
			
		if(performanceCriteria.getObjectType() < 1 || performanceCriteria.getObjectId() < 1)
			throw new IllegalArgumentException("PerformanceCriteria not allowed for objectType[" + performanceCriteria.getObjectType() + "] ");	
		
		competencyManager.saveOrUpdate(performanceCriteria);		
		return performanceCriteria;
	}
	
	@RequestMapping(value="/mgmt/competency/performance-criteria/batch/update.json", method=RequestMethod.POST)
	@ResponseBody
	public List<PerformanceCriteria> updatePerformanceCriteria(
			@RequestBody List<DefaultPerformanceCriteria> performanceCriterias
			) throws PerformanceCriteriaNotFoundException{
			
		List<PerformanceCriteria> listToUse = new ArrayList<PerformanceCriteria>();
		for( DefaultPerformanceCriteria performanceCriteria : performanceCriterias ){
			if(performanceCriteria.getObjectType() < 1 || performanceCriteria.getObjectId() < 1)
				throw new IllegalArgumentException("PerformanceCriteria not allowed for objectType[" + performanceCriteria.getObjectType() + "] ");	
			
			listToUse.add(performanceCriteria);
		}
		competencyManager.saveOrUpdatePerformanceCriterias(listToUse);		
		return listToUse;
	}	

	@RequestMapping(value="/mgmt/competency/performance-criteria/batch/remove.json", method=RequestMethod.POST)
	@ResponseBody
	public List<PerformanceCriteria> removePerformanceCriteria(
			@RequestBody List<DefaultPerformanceCriteria> performanceCriterias
			) throws PerformanceCriteriaNotFoundException{
			
		List<PerformanceCriteria> listToUse = new ArrayList<PerformanceCriteria>();
		for( DefaultPerformanceCriteria performanceCriteria : performanceCriterias ){
			if(performanceCriteria.getObjectType() < 1 || performanceCriteria.getObjectId() < 1)
				throw new IllegalArgumentException("PerformanceCriteria not allowed for objectType[" + performanceCriteria.getObjectType() + "] ");	
			
			listToUse.add(performanceCriteria);
		}
		competencyManager.removePerformanceCriterias(listToUse);		
		return listToUse;
	}	

	
	
	@RequestMapping(value="/mgmt/competency/ability/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Ability> listAbility(
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId
	) {
		if( objectType < 1 || objectId < 1)
			return Collections.EMPTY_LIST;		
		return competencyManager.getAbilities(objectType, objectId);
	}
	
	
	@RequestMapping(value="/mgmt/competency/ability/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Ability updateAbility(
			@RequestBody DefaultAbility ability
			) throws AbilityNotFoundException{
			
		if(ability.getObjectType() < 1 || ability.getObjectId() < 1)
			throw new IllegalArgumentException("Ability not allowed for objectType[" + ability.getObjectType() + "] ");	
		
		competencyManager.saveOrUpdateAbility(ability);		
		return ability;
	}
	
	@RequestMapping(value="/mgmt/competency/ability/batch/update.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Ability> updateAbilities(
			@RequestBody List<DefaultAbility> abilities
			) throws AbilityNotFoundException{
			
		List<Ability> listToUse = new ArrayList<Ability>();
		for( Ability ability : abilities ){
			if(ability.getObjectType() < 1 || ability.getObjectId() < 1)
				throw new IllegalArgumentException("Ability not allowed for objectType[" + ability.getObjectType() + "] ");				
			listToUse.add(ability);
		}
		competencyManager.saveOrUpdateAblilities(listToUse);		
		return listToUse;
	}	

	@RequestMapping(value="/mgmt/competency/ability/batch/remove.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Ability> removeAbilities(
			@RequestBody List<DefaultAbility> abilities
			) throws AbilityNotFoundException{
			
		List<Ability> listToUse = new ArrayList<Ability>();
		for( Ability ability : abilities ){
			if(ability.getObjectType() < 1 || ability.getObjectId() < 1)
				throw new IllegalArgumentException("Ability not allowed for objectType[" + ability.getObjectType() + "] ");	
			
			listToUse.add(ability);
		}
		competencyManager.removeAbilities(listToUse);		
		return listToUse;
	}	
	
		
	@RequestMapping(value="/mgmt/competency/assessment/rating-scheme/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<RatingScheme> listRatingScheme(
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId
	) {
		return assessmentManager.getRatingSchemes(objectType, objectId);
	}
	
	
	@RequestMapping(value="/mgmt/competency/assessment/rating-scheme/update.json", method=RequestMethod.POST)
	@ResponseBody
	public RatingScheme updateRatingScheme(
			@RequestBody DefaultRatingScheme ratingScheme
			) throws RatingSchemeNotFoundException{
		for( RatingLevel rl : ratingScheme.getRatingLevels()){
			if( rl.getRatingSchemeId() < 1 ){
				rl.setRatingSchemeId(ratingScheme.getRatingSchemeId());
			}
		}
		assessmentManager.saveOrUpdateRatingScheme(ratingScheme);
		return ratingScheme;
	}

	@RequestMapping(value="/mgmt/competency/assessment-scheme/list.json", method=RequestMethod.POST)
	@ResponseBody
	public List<AssessmentScheme> listAssessmentScheme(
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId
	) {
		return assessmentManager.getAssessmentSchemes(objectType, objectId);
	}
	
	
	@RequestMapping(value="/mgmt/competency/assessment-scheme/update.json", method=RequestMethod.POST)
	@ResponseBody
	public AssessmentScheme updateAssessmentScheme(
			@RequestBody DefaultAssessmentScheme assessmentScheme
			) {
		
		log.debug(assessmentScheme);
		
		assessmentManager.saveOrUpdateAssessmentScheme(assessmentScheme);
		return assessmentScheme;
	}
}
