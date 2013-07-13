package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskNode extends Task implements Serializable {
	
	private ArrayList<Task> children;
	
	
	public TaskNode() {
		this.children = new ArrayList<Task>();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public int completion() {
	        
        int count = 0;
        int sum = 0;
        
        for (Task t : children) {
            sum += t.completion();
            count++;
        }
        
        return Math.round(sum / count);
        
	}
	
	public int numChildren() {
		return children.size();
	}
	
	public boolean hasCildren() {
		return numChildren() > 0;
	}
	
	public ArrayList<Task> getChildren() {
		return children;
	}
	
	public void addSubTask(Task t) {
		children.add(t);
	}

}
