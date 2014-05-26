package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.Date;

public abstract class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int maxNameLen = 3000;
	public static final int maxDescriptionLen = 50000;
	private String name, description;
	private Date timeStamp;
	private Task parent;
	private TaskHead head;
	private int color;
	private boolean useColor;
	private int weight;
	private Date deadline;

	public Task(Task parent) {
		this.parent = parent;
		this.head = parent.getHead();
	}

	public Task(TaskHead head) {
		this.parent = null;
		this.head = head;
		this.weight = 1;
	}

	public abstract int completion();

	public boolean hasChildren() {
		return subTaskCount() != 0;
	}

	public abstract int subTaskCount();

	public boolean setName(String name) {
		if (name.length() <= maxNameLen) {
			this.name = name;
			return true;
		}
		return false;
	}

	public boolean setDescription(String description) {
		if (description.length() <= maxDescriptionLen) {
			this.description = description;
			return true;
		}
		return false;
	}
	
	public void setColor(int color) {
		this.color = color;
		this.useColor = true;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public void setUseColor(boolean useColor) {
		this.useColor = useColor;
	}
	
	public boolean getUseColor() {
		return useColor;
	}
	
	public Date getDeadline() {
		return deadline;
	}
	
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	public int getWeight() {
		if (this.weight < 1) {
			this.weight = 1;
		}
		return this.weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getName() {
		if (name == null)
			return "";
		else
			return name;
	}

	public String getDescription() {
		if (description == null)
			return "";
		else
			return description;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Task getParent() {
		return parent;
	}

	public void setParent(Task p) {
		this.parent = p;
	}

	public boolean isHead() {
		return parent == null;
	}

	public String getPath() {
		if (!isHead()) {
			return parent.getPath() + " > " + this.name;
		}
		return this.name;
	}

	public TaskHead getHead() {
		return head;
	}

}
