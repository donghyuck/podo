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

import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetManager.CodeItem;
import com.podosoftware.competency.codeset.DefaultCodeSet;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyAlreadyExistsException;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.competency.CompetencyNotFoundException;
import com.podosoftware.competency.competency.DefaultCompetency;
import com.podosoftware.competency.competency.DefaultEssentialElement;
import com.podosoftware.competency.competency.EssentialElement;
import com.podosoftware.competency.competency.EssentialElementNotFoundException;

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
	
	
	
	@RequestMapping(value="/mgmt/competency/codeset/list.json", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public List<CodeSet> listCodeSet(@RequestParam(value="companyId", defaultValue="0", required=false ) Long companyId ){		
		User user = SecurityHelper.getUser();		
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
			
		@RequestParam(value="codeSetId", defaultValue="0", required=false ) Integer codeSetId,		
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
			
			ExcelReader reader = new ExcelReader(is);			
			int rowCount = reader.getPhysicalNumberOfRows();	
			int firstRowCount = reader.getFirstRowNum();			
			log.debug("row:"+ firstRowCount + "-" + rowCount);
			for( int i = skipRowCount ; i < rowCount ; i ++ ){				
				Row row = reader.getRow(i);
				log.debug("row[" + i + "]:" + row );
				if( row != null ){
					int columnCount = row.getPhysicalNumberOfCells();
					int firstColumnCount = row.getFirstCellNum();
					log.debug("column[" + columnCount + "]:"+ firstColumnCount );
					if( columnCount > 0){
						/*
						for( int c = 0; c < columnCount; c++ ){
							Cell cell = row.getCell(c);			
							log.debug( c + ":"+ cell.toString() + ",boolean=" + reader.isBooleanCell(cell) + ", numinuc=" + reader.isNumericCell(cell) + ", string=" + reader.isStringCell(cell));			 
						}
						*/
						String l_code = getStringCellValue(reader, row.getCell(0));
						String m_code = getStringCellValue(reader, row.getCell(2));
						String s_code = getStringCellValue(reader, row.getCell(4));
						String l_code_name = getStringCellValue(reader, row.getCell(1));
						String m_code_name = getStringCellValue(reader, row.getCell(3));
						String s_code_name = getStringCellValue(reader, row.getCell(5));					
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
					}
				}
			}
		}		
	//	CodeSet codeset = codeSetManager.getCodeSet(codeSetId);
	//	codeSetManager.batchUpdate(codeset, new ArrayList<CodeItem>(codes.values()));
		
		/*
		List<CodeSet> list = new ArrayList<CodeSet>();
		for(CodeItem item : codes.values())
		{
			DefaultCodeSet newCodeSet = new DefaultCodeSet();
			newCodeSet.setObjectType(codeset.getObjectType());
			newCodeSet.setObjectId(codeset.getObjectId());
			newCodeSet.setParentCodeSetId(codeset.getCodeSetId());
			newCodeSet.setCode(item.getCode());
			newCodeSet.setName(item.getName());		
			list.add(newCodeSet);
		}
		codeSetManager.saveOrUpdate(list);		
		List<CodeSet> list2 = new ArrayList<CodeSet>();
		for(CodeItem item : codes.values()){			
			for(CodeItem item2 : item.getItems().values() ){
				DefaultCodeSet newCodeSet = new DefaultCodeSet();
				newCodeSet.setObjectType(codeset.getObjectType());
				newCodeSet.setObjectId(codeset.getObjectId());
				newCodeSet.setParentCodeSetId(item.getCodeSetId());
				newCodeSet.setCode(item.getCode());
				newCodeSet.setName(item.getName());		
				list2.add(newCodeSet);	
			}
		}
		*/		
		return codes;
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
		
		if(competencyToUse.getObjectType() == 0 || competencyToUse.getObjectId() == 0 ){
			throw new IllegalArgumentException();
		}
		competencyManager.saveOrUpdate(competencyToUse);
		return competencyToUse;
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

	
}
