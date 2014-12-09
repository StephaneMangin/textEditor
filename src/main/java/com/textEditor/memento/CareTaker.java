package com.textEditor.memento;

import java.util.Stack;

/**
 * @author blacknight
 *
 */
public class CareTaker extends Stack<Pair<String, Memento>> {
	   
	private static final long serialVersionUID = 1L;

	/**
	 * Return a memento to be process by a CareTaker instance
	 * @param string
	 * @param memento
	 */
	public void addMemento(String string, Memento memento) {
		push(new Pair<String, Memento>(string, memento));
	}
	
	/**
	 * Return a memento to be process by a CareTaker instance
	 * @param index
	 * @return
	 */
	public Pair<String, Memento> getMemento(int index) {
		return elementAt(index);
	}
}