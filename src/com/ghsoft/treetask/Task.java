package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Task implements Serializable {
	
	private String name, description;
	private static final int maxNameLen = 80;
	private static final int maxDescriptionLen = 500;
	
	public Task() {
		
	}

	public abstract int completion();
	public abstract boolean hasChildren();
	
	public void setName(String name) {
		if (name.length() <= maxNameLen) {
			this.name = name;
		}
	}
	
	public void setDescription(String description) {
		if (description.length() <= maxDescriptionLen) {
			this.description = description;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	
	

}
