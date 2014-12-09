package com.textEditor.memento;

import net.sf.json.JSONObject;


public class Memento {
	
    private JSONObject state;

    public Memento(JSONObject stateToSave) {
    	state = stateToSave;
    }
    
    /**
     * Return the state of the memento
     * @return
     */
    public JSONObject getSavedState() {
    	return state;
    }
}