package com.textEditor.core;

import com.textEditor.memento.CareTaker;
import com.textEditor.memento.Originator;

/**
 * @(#) Selection.java
 */

public class Selection extends Originator {

	private Integer start;
	private Integer end;
	private String str;

	public Selection(Integer start, Integer end, String str) {
		setStart(start);
		setEnd(end);
		setContent(str);
	}

	public String getContent() {
		return str;
	}

	public void setContent(String content) {
		this.str = content;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public void jump() {
		setStart(start + str.length());
		reset();
	}

	public void reset() {
		setEnd(0);
		setContent("");
	}
	
	public String toString() {
		return start + "->" + end + "='" + str + "'";

	}
}
