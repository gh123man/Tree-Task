package com.ghsoft.treetask;


public class TextTreeBuilder {

	private Task task;

	public TextTreeBuilder(Task task) {
		this.task = task;
	}

	public String getText() {

		String out = "";

		out += drawNode("1", (TaskNode) task);

		return out;
	}

	private String drawNode(String uri, TaskNode t) {

		int index = 0;
		String out = "";

		
		out += uri + ": " + t.getName() + " - " + t.completion() + "%";
		
		if (t.getDescription().length() > 0) {
			out += "\n";
			out += t.getDescription();
		}
		
		out += "\n";
		out += "\n";
		
		for (int i = 0; i < t.numChildren(); i++) {
			index++;
			
			if (t.getChild(i) instanceof TaskNode) {
				out += drawNode(uri + "." + index, (TaskNode) t.getChild(i));
			} else {
				out += drawLeaf(uri + "." + index, (TaskLeaf) t.getChild(i));
			}
			
		}


		return out;
	}

	private String drawLeaf(String uri, TaskLeaf t) {

		String out = "";
		
		out += uri + ": " + t.getName() + " - " + t.completion() + "%";
		if (t.getDescription().length() > 0) {
			out += "\n";
			out += t.getDescription();
		}
		out += "\n";
		out += "\n";
		
		return out;
	}


}
