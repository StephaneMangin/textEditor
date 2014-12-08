package com.textEditor.core;

import com.textEditor.memento.Originator;

import net.sf.json.JSONObject;

/**
 * @(#) Selection.java
 */

public class Selection extends Originator {

	private Integer start = 0;
	private Integer end = 0;
	private String content = "";
	
	public Selection(Integer start, Integer end, String content) {
		setStart(start);
		setEnd(end);
		setContent(content);
		state = toJson();
	}
    
	public void set(JSONObject json) {
		state = json;
		System.out.println(json.toString());
		setStart(json.getInt("start"));
		setEnd(json.getInt("end"));
		setContent(json.getString("content"));
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		setStart(start + content.length());
		reset();
	}

	public void reset() {
		setEnd(0);
		setContent("");
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.accumulate("start", start);
		json.accumulate("end", end);
		json.accumulate("content", content);
		return json;
	}
	
	public String toString() {
		return start + "->" + end + "='" + content + "'";

	}
}
