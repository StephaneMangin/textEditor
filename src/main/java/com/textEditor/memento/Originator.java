package com.textEditor.memento;

import net.sf.json.JSONObject;

public class Originator {
	
    private JSONObject state;
 
    public void set(JSONObject state) {
        System.out.println("Originator: etat affecte a: " + state.toString());
        this.state = state;
    }
 
    public Object saveToMemento() {
        System.out.println("Originator: sauvegarde dans le memento.");
        return new Memento(state);
    }
    
    public void restoreFromMemento(Object m) {
        if (m instanceof Memento) {
            Memento memento = (Memento)m;
            set(memento.getSavedState());
            System.out.println("Originator: Etat après restauration: " + state.toString());
        }
    }
 
    private static class Memento {
    	
        private JSONObject state;
 
        public Memento(JSONObject stateToSave) {
        	state = stateToSave;
        }
        
        public JSONObject getSavedState() {
        	return state;
        }
    }
 }