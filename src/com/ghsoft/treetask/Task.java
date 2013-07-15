package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int maxNameLen = 30;
	public static final int maxDescriptionLen = 500;
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

	public abstract boolean hasChildren();

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
		return name;
	}

	public String getDescription() {
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
