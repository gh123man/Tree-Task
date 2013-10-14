package com.ghsoft.treetask;

import java.io.Serializable;

public abstract class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int maxNameLen = 3000;
	public static final int maxDescriptionLen = 50000;
	private String name, description;
	private Task parent;
	private TaskHead head;

	public Task(Task parent) {
		this.parent = parent;
		this.head = parent.getHead();
	}

	public Task(TaskHead head) {
		this.parent = null;
		this.head = head;
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
