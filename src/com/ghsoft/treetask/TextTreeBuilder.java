package com.ghsoft.treetask;


public class TextTreeBuilder {

	private Task task;
	private boolean useTabs, useHead, useNums, useDescription, useProg;

	public TextTreeBuilder(Task task) {
		this.task = task;
		this.useTabs = false;
		this.useHead = false;
		this.useNums = true;
		this.useDescription = true;
		this.useProg = true;
	}
	
	public void setTask (Task t) {
		this.task = t;
	}
	
	public void setTabs(boolean tabs) {
		this.useTabs = tabs;
	}

	public void setHead(boolean useHead) {
		this.useHead = useHead;
	}
	
	public void setNums(boolean nums) {
		this.useNums = nums;
	}
	
	public void setUseDescription(boolean des) {
		this.useDescription = des;
	}
	
	public void setUseProgress(boolean prog) {
		this.useProg = prog;
	}
	
	public String getText() {
		String out = "";
		String tabs = "";
		String index = useNums ? "1" : "";

		if (useHead)
			out += drawNode(index, tabs, (TaskNode) task.getHead().getTask());
		else
			out += drawNode(index, tabs, (TaskNode) task);

		return out;
	}

	private String drawNode(String uri, String tabs, TaskNode t) {

		int index = 0;
		String out = "";

		
		out += tabs + (uri != "" ? uri + ": " : "") + t.getName() + (useProg ? " - " + t.completion() + "%" : "");
		
		if (t.getDescription().length() > 0 && useDescription) {
			out += "\n";
			out += tabs + t.getDescription();
		}
		
		out += "\n";
		out += "\n";
		String nextURI = "";
		tabs += useTabs ? "\t" : "";
		
		for (int i = 0; i < t.numChildren(); i++) {
			index++;
			nextURI = useNums ? uri + "." + index : "";
			if (t.getChild(i) instanceof TaskNode) {
				out += drawNode(nextURI, tabs, (TaskNode) t.getChild(i));
			} else {
				out += drawLeaf(nextURI, tabs, (TaskLeaf) t.getChild(i));
			}
			
		}


		return out;
	}

	private String drawLeaf(String uri, String tabs, TaskLeaf t) {

		String out = "";
		
		out += tabs + (uri != "" ? uri + ": " : "") + t.getName() + (useProg ? " - " + t.completion() + "%" : "");
		if (t.getDescription().length() > 0 && useDescription) {
			out += "\n";
			out += tabs + t.getDescription();
		}
		out += "\n";
		out += "\n";
		
		return out;
	}


}
