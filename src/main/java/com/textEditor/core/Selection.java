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
	private String prevContent = "";
	
	public Selection() {
		state = toJson();
	}
	
	public Selection(Integer start, Integer end, String content, String prevContent) {
		setStart(start);
		setEnd(end);
		setContent(content);
		setPrevContent(prevContent);
		state = toJson();
	}
    
	public void set(JSONObject json) {
		state = json;
		setStart(json.getInt("start"));
		setEnd(json.getInt("end"));
		setContent(json.getString("content"));
		setPrevContent(json.getString("prevContent"));
	}
	
	public void setPrevContent(String prevContent) {
		this.prevContent = prevContent;
	}

	public String getContent() {
		return content;
	}
	
	public String getPrevContent() {
		return prevContent;
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

	public void previous() {
		setEnd(start + content.length());
		setContent(prevContent);
		setPrevContent("");
	}
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.accumulate("start", start);
		json.accumulate("end", end);
		json.accumulate("content", content);
		json.accumulate("prevContent", prevContent);
		return json;
	}
	
	public String toString() {
		return "<Selection (" + start + ">" + end + ")>";

	}
}
