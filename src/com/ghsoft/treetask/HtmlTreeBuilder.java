package com.ghsoft.treetask;

import java.util.UUID;

public class HtmlTreeBuilder {

	private Task task;

	public HtmlTreeBuilder(Task task) {
		this.task = task;
	}

	public String getHtml() {

		String out = "";

		out += "<!DOCTYPE html><html><body>";

		out += "<link rel='stylesheet' type='text/css' href='treeView.css'>";

		out += "<div>";
		out += drawNode((TaskNode) task);

		out += "</div>";

		out += "</body></html>";

		return out;
	}

	private String drawNode(TaskNode t) {

		String out = "";

		String rndID = UUID.randomUUID().toString();

		String perc = "20%";

		out += "<div>";

		out += "<div class='node'>";
		out += "<div class='taskName'>";
		out += t.getName();
		out += "</div>";

		out += "<div class='taskDescription'>";
		out += t.getDescription();
		out += "</div>";

		out += "<style type='text/css'>";
		out += "    #" + rndID + " {";
		out += "        width:" + t.completion() + "%";
		out += "    }";
		out += "</style>";

		out += "<div class='progressBackground'>";
		out += "    <div id='" + rndID + "' class='progress'></div>";
		out += "</div>";
		out += "<div class='progText'>";
		out += t.completion() + "%";
		out += " </div>";
		out += " </div>";

		out += triangle;

		out += "<div class='children'>";

		for (int i = 0; i < t.numChildren(); i++) {
			if (t.getChild(i) instanceof TaskNode) {
				out += drawNode((TaskNode) t.getChild(i));
			} else {
				out += drawLeaf((TaskLeaf) t.getChild(i));
			}
		}

		out += " </div>";
		out += " </div>";

		return out;
	}

	private String drawLeaf(TaskLeaf t) {

		String out = "";
		out += "<div>";
		out += "<div class='node'>";
		out += "<div class='taskName'>";
		out += t.getName();
		out += "</div>";

		out += "<div class='taskDescription'>";
		out += t.getDescription();
		out += "</div>";

		out += "</div>";
		out += "</div>";

		return out;
	}

	/*
	 * 
	 * <div>
	 * 
	 * 
	 * <div class="node"> <div class="taskName"> taskname </div> </div>
	 * 
	 * 
	 * <div class="tri"> <div class="triangle"> </div> </div>
	 * 
	 * 
	 * 
	 * <div class="children">
	 * 
	 * 
	 * </div>
	 * 
	 * </div>
	 */

	private static String buildTree(Task tree) {

		String out = "";

		return out;

	}

	private static String triangle = "<div class='tri'><div class='triangle'></div></div>";

}
