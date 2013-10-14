package com.ghsoft.treetask;

import java.math.BigInteger;
import java.security.SecureRandom;

public class HtmlTreeBuilder {

	private Task task;
	private SecureRandom random;

	public HtmlTreeBuilder(Task task) {
		this.task = task;
		this.random = new SecureRandom();
	}

	private String getUrl(Task t) {
		String out = "";

		if (!t.isHead()) {
			TaskNode tn = (TaskNode) t.getParent();
			out += getUrl(tn);
			int index = tn.getChildren().indexOf(t);
			out += "," + String.valueOf(index);
			return out;
		}
		return "";
	}

	private String getFullUrl(Task t) {
		return "root" + getUrl(t);
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

		String rndID = new BigInteger(130, random).toString(32);
		rndID = rndID.replaceAll("[0-9]", "");

		out += "<div class='wrapper'>";

		out += "<a href='" + getFullUrl(t) + "'>";
		out += "<div class='node'>";
		if (t.completion() == 100)
			out += "<div class='taskNameFinished'>";
		else
			out += "<div class='taskName'>";
		out += t.getName();
		out += "<br>";
		out += "</div>";

		if (t.completion() == 100)
			out += "<div class='taskDescriptionFinished'>";
		else
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
		out += "</div>";
		out += "</div>";
		out += "</a>";

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
		out += "<a href='" + getFullUrl(t.getParent()) + "'>";
		out += "<div class='node'>";

		if (t.completion() == 100)
			out += "<div class='taskNameFinished'>";
		else
			out += "<div class='taskName'>";

		out += t.getName();
		out += "<br>";
		out += "</div>";

		if (t.completion() == 100)
			out += "<div class='taskDescriptionFinished'>";
		else
			out += "<div class='taskDescription'>";
		out += t.getDescription();
		out += "</div>";

		out += "</div>";
		out += "</a>";
		out += "</div>";

		return out;
	}

	private static String triangle = "<div class='tri'><div class='triangle'></div></div>";

}
