package com.textEditor.memento;

import net.sf.json.JSONObject;

public class Originator {
	
    protected JSONObject state = new JSONObject();
 
    public void set(JSONObject state) {
        System.out.println("Originator: etat affecte a: " + state.toString());
        this.state = state;
    }
 
    public Memento saveToMemento() {
        System.out.println("Originator: sauvegarde dans le memento =>" + state.toString());
        return new Memento(state);
    }
    
    public void restoreFromMemento(Object m) {
        if (m instanceof Memento) {
            Memento memento = (Memento)m;
            set(memento.getSavedState());
            System.out.println("Originator: Etat après restauration: " + memento.getSavedState().toString());
        }
    }
 }