package com.textEditor.memento;

import net.sf.json.JSONObject;


public class Memento {
	
    private JSONObject state;

    public Memento(JSONObject stateToSave) {
    	state = stateToSave;
    }
    
    public JSONObject getSavedState() {
    	return state;
    }
}