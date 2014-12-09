package com.textEditor.memento;

@SuppressWarnings("hiding")
public class Pair<String,Memento> {
	
	protected String reference;
    public Memento memento;
    
    public Pair(String reference, Memento memento){
        this.reference = reference;
        this.memento = memento;
    }
    public String getReference(){ return reference; }
    public Memento getMemento(){ return memento; }
    public void setReference(String l){ this.reference = l; }
    public void setMemento(Memento r){ this.memento = r; }
}
