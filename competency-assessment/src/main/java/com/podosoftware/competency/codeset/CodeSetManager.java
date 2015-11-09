package com.podosoftware.competency.codeset;

import java.util.List;

import architecture.common.user.Company;

public interface CodeSetManager {

	public void saveOrUpdate(CodeSet codeset);
	
	public List<CodeSet> getCodeSets(Company company);
	
	public List<CodeSet> getCodeSets(CodeSet codeset);
		
	public int getCodeSetCount(Company company);
	
	public int getCodeSetCount(CodeSet codeset);
		
	public CodeSet getCodeSet(long codeSetId) throws CodeSetNotFoundException;
	
	/**
	 * 인자로 전달된 company 의  표준 코드 세트을 생성한다.
	 *   
	 * @param company
	 * @param name
	 * @param desctiption
	 * @return
	 */
	public CodeSet createCodeSet(Company company, String name, String desctiption);
	
	/**
	 * 인자로 전달된 표준 코드 세트의 하위 표준 코드 세트를 생성한다
	 * 
	 * @param codeset
	 * @param name
	 * @param desctiption
	 * @return
	 */
	public CodeSet createCodeSet(CodeSet codeset, String name, String desctiption);
	
	public CodeSetTreeWalker getCodeSetTreeWalker(Company company);
}
