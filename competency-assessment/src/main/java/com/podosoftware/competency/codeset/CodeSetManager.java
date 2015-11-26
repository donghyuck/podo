package com.podosoftware.competency.codeset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.common.user.Company;

public interface CodeSetManager {
	
	public void batchUpdate(CodeSet codeSet, List<CodeItem> items);
	
	public void saveOrUpdate(List<CodeSet> codesets);
	
	public void saveOrUpdate(CodeSet codeset);
	
	public List<CodeSet> getCodeSets(Company company);
	
	public List<CodeSet> getCodeSets(CodeSet codeset);
		
	public List<CodeSet> getRecrusiveCodesets(CodeSet codeset);
	
	public int getCodeSetCount(Company company);
		
	public int getCodeSetCount(CodeSet codeset);
	
	public int getRecrusiveCodeSetCount(CodeSet codeset);
	
	
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
	
	
	
	public static class CodeItem {
		private String name;
		private String code;
		private Map<String, CodeItem> items;
		public CodeItem() {
			this.name = null;
			this.code = null;
			items = new HashMap<String, CodeItem>();
		}
		
		
		public CodeItem(String name, String code) {
			this.name = name;
			this.code = code;
			items = new HashMap<String, CodeItem>();
		}


		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Map<String, CodeItem> getItems() {
			return items;
		}
		public void setItems(Map<String, CodeItem> items) {
			this.items = items;
		}
	}
}
