package com.textEditor.memento;

import java.util.Stack;

public class CareTaker extends Stack<Memento> {
			
	private static final long serialVersionUID = -5441904950627315914L;

	public void addMemento(Memento m) {
		push(m);
	}
	
	public Memento getMemento(int index) {
		return elementAt(index);
	}
}