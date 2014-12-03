package com.textEditor.commands;

import net.sf.json.JSONObject;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;
import com.textEditor.memento.Originator;


/**
 * @(#) Command.java
 */

public class Command extends Originator implements CommandInterface {

	protected Core core;
	protected String command;
	private Selection position = new Selection(0, 0, "");

	public Command(Core core) {
		this.core = core;
		command = this.getClass().getSimpleName();
		state.accumulate("command", command);
	}
	
    public void set(JSONObject state) {
    	this.command = state.getString("command");
    	position.set(state.getJSONObject("position"));
    }
    
    public Selection getSelection() {
    	return position;
    }
    
	public void execute(Selection position) {
		return;
	}

	public void setPosition(Selection position) {
		this.position = position;
		state.accumulate("position", this.position.toJson());
	}	
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.accumulate("command", this.getClass().getSimpleName());
		json.accumulate("position", position.toJson());
		return json;
	}
}