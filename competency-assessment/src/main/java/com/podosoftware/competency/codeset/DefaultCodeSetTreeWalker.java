package com.podosoftware.competency.codeset;

import java.util.ArrayList;
import java.util.List;

import architecture.common.util.LongTree;
import architecture.ee.util.ApplicationHelper;

public class DefaultCodeSetTreeWalker implements CodeSetTreeWalker {

	
	private int objectType = -1;
	private long objectId = -1L;
	private LongTree tree;
	public static final CodeSet TOP_CODE_SET = new DefaultCodeSet();
	
	
	public DefaultCodeSetTreeWalker(int objectType, long objectId, LongTree tree) {
		super();
		this.objectType = objectType;
		this.objectId = objectId;
		this.tree = tree;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public LongTree getTree() {
		return tree;
	}

	public void setTree(LongTree tree) {
		this.tree = tree;
	}


	protected long[] getCodeSetIds( CodeSet parent ) {
		return tree.getChildren(parent.getCodeSetId());
	}
	
	protected long[] getRecursiveCodeSetIds( CodeSet parent ) {
		return tree.getRecursiveChildren(parent.getCodeSetId());
	}	
	
	public int getCodeSetDepth(CodeSet comment) {
		int depth = tree.getDepth(comment.getCodeSetId());
		if (depth == -1)
			throw new IllegalArgumentException((new StringBuilder())
					.append("CodeSet").append(comment.getCodeSetId())
					.append(" does not belong to this document.").toString());
		else
			return depth - 1;
	}
	
	public int getRecursiveChildCount(CodeSet parent) {
		return getRecursiveChildCount(parent.getCodeSetId());
	}

	
	public int getIndexOfChild(CodeSet parent, CodeSet child) {
		return tree.getIndexOfChild(parent.getCodeSetId(), child.getCodeSetId());
	}

	public boolean isLeaf(CodeSet comment) {
		return tree.isLeaf(comment.getCodeSetId());
	}

	private int getRecursiveChildCount(long parentId) {
		int numChildren = 0;
		int num = tree.getChildCount(parentId);
		numChildren += num;
		for (int i = 0; i < num; i++) {
			long childID = tree.getChild(parentId, i);
			if (childID != -1L)
				numChildren += getRecursiveChildCount(childID);
		}
		return numChildren;
	}
	@Override
	public int getChildCount(CodeSet comment) {
		return tree.getChildCount(comment.getCodeSetId());
	}
	@Override
	public List<CodeSet> topLevelCodeSets() {
		return children(this.TOP_CODE_SET);
	}
	
	@Override
	public CodeSet getParent(CodeSet comment) throws CodeSetNotFoundException {
		long parentId = tree.getParent(comment.getCodeSetId());
		if (parentId == -1L) {
			return null;
		} else {
			CodeSetManager mgr = ApplicationHelper.getComponent(CodeSetManager.class);
			return mgr.getCodeSet(parentId);
		}
	}
	
	@Override
	public CodeSet getChild(CodeSet comment, int index)
			throws CodeSetNotFoundException {
		long childId = tree.getChild(comment.getCodeSetId(), index);
		if (childId == -1L) {
			return null;
		} else {
			CodeSetManager mgr = ApplicationHelper.getComponent(CodeSetManager.class);
			return mgr.getCodeSet(childId);
		}
	}
	@Override
	public List<CodeSet> children(CodeSet comment) {
		long children[] = tree.getChildren(comment.getCodeSetId());
		List<CodeSet> list = new ArrayList<CodeSet>();
		CodeSetManager mgr = ApplicationHelper.getComponent(CodeSetManager.class);
		for( long childId : children )
			try {
				list.add(mgr.getCodeSet(childId));
			} catch (CodeSetNotFoundException e) {
				e.printStackTrace();
			}
		return list;
	}
	
	@Override
	public List<CodeSet> recursiveChildren(CodeSet comment) {
		 long comments[] = tree.getRecursiveChildren(comment.getCodeSetId());
		 List<CodeSet> list = new ArrayList<CodeSet>();
		 CodeSetManager mgr = ApplicationHelper.getComponent(CodeSetManager.class);
			for( long commentId : comments )
				try {
					list.add(mgr.getCodeSet(commentId));
				} catch (CodeSetNotFoundException e) {
					e.printStackTrace();
				}
			return list;
	}

}
