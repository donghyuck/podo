package com.podosoftware.competency.codeset;

import java.io.Serializable;
import java.util.List;


public interface CodeSetTreeWalker extends Serializable {
	
	public abstract int getCodeSetDepth(CodeSet codeset);

    public abstract int getChildCount(CodeSet codeset);

    public abstract int getRecursiveChildCount(CodeSet codeset);

    public abstract int getIndexOfChild(CodeSet codeset, CodeSet codeset2);

    public abstract boolean isLeaf(CodeSet codeset);
    
    public abstract List<CodeSet> topLevelCodeSets();
    
	public abstract CodeSet getParent(CodeSet codeset) throws CodeSetNotFoundException;

	public abstract CodeSet getChild(CodeSet codeset, int i) throws CodeSetNotFoundException;

	public abstract List<CodeSet> children(CodeSet codeset);
	
	public abstract List<CodeSet> recursiveChildren(CodeSet codeset);	
}
