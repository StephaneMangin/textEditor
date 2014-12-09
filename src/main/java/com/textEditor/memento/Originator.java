package com.textEditor.memento;

import com.textEditor.log.Log;

import net.sf.json.JSONObject;

public class Originator {
	
    protected JSONObject state = new JSONObject();
	private Log logger;
 
    public Originator() {
    	logger = new Log(this);
    }
    
    public void set(JSONObject state) {
		logger.fine("Originator: etat affecte a: " + state.toString());
        this.state = state;
    }
 
    public Memento saveToMemento() {
    	logger.fine("Originator: sauvegarde dans le memento =>" + state.toString());
        return new Memento(state);
    }
    
    public void restoreFromMemento(Object m) {
        if (m instanceof Memento) {
            Memento memento = (Memento)m;
            set(memento.getSavedState());
            logger.fine("Originator: Etat après restauration: " + memento.getSavedState().toString());
        }
    }
 }