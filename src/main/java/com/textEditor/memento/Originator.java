package com.textEditor.memento;

import com.textEditor.log.Log;

import net.sf.json.JSONObject;

public class Originator {
	
    protected JSONObject state = new JSONObject();
	protected Log logger;
 
    public Originator() {
    	logger = new Log(this);
    }
    
    /**
     * Set the state
     * @param state
     */
    public void set(JSONObject state) {
		logger.info("Originator: etat affecte");
        this.state = state;
    }
 
    /**
     * Return a memento to be process by a CareTaker
     * @return
     */
    public Memento saveToMemento() {
    	logger.info("Originator: sauvegarde dans le memento =>" + state.toString());
        return new Memento(state);
    }
    
    /**
     * Restore the state with a memento
     * @param m
     */
    public void restoreFromMemento(Object m) {
        if (m instanceof Memento) {
            Memento memento = (Memento)m;
            set(memento.getSavedState());
            logger.info("Originator: Etat après restauration => " + state.toString());
        }
    }
 }