package com.textEditor.memento;

import java.util.Stack;

public class CareTaker extends Stack<Pair<String, Memento>> {

	private static final long serialVersionUID = 1L;

	public void addMemento(String string, Memento m) {
		push(new Pair<String, Memento>(string, m));
	}
	
	public Pair<String, Memento> getMemento(int index) {
		return elementAt(index);
	}
}