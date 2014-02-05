package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskNode extends Task implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Task> children;

	public TaskNode(Task parent) {
		super(parent);
		this.children = new ArrayList<Task>();

	}

	public TaskNode(TaskHead head) {
		super(head);
		this.children = new ArrayList<Task>();
		head.setTreeHead(this);

	}

	public static TaskNode fromLeaf(TaskLeaf from) {
		TaskNode tn = new TaskNode(from.getParent());
		tn.setName(from.getName());
		tn.setDescription(from.getDescription());
		return tn;
	}

	@Override
	public int subTaskCount() {
		int out = numChildren();

		for (Task child : children) {
			out += child.subTaskCount();
		}

		return out;
	}

	@Override
	public int completion() {

		int count = 0;
		int sum = 0;

		for (int i = 0; i < this.numChildren(); i++) {
			sum += getChild(i).completion();
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

	public Task getChild(int i) {

		if (children.get(i) instanceof TaskNode) {
			TaskNode tn = (TaskNode) children.get(i);
			if (tn.numChildren() < 1) {
				TaskLeaf tl = TaskLeaf.fromNode(tn);
				children.set(i, tl);
			}
		}

		return children.get(i);
	}

	public ArrayList<Task> getChildren() {
		return children;
	}

	public void setChild(int i, Task t) {
		children.set(i, t);
	}

	public void deleteChild(int i) {
		children.remove(i);
	}

	public void deleteChild(Task t) {
		children.remove(t);
	}

	public void addSubTask(Task t) {
		children.add(t);
	}

	public void replaceChild(int pos, Task t) {
		children.set(pos, t);
	}
	
	public void moveChild(int from, int to) {
		Task t = children.get(from);
		children.remove(from);
		children.add(to, t);
	}

}
