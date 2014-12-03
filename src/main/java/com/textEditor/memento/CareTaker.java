package com.textEditor.memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CareTaker {
	
	private Map<Object, List<Object>> savedStates;
	
	public void addMemento(Object m) {
		String className = m.getClass().getName();
		if (!savedStates.containsKey(className)) {
			savedStates.put(className, new ArrayList<Object>());
		}
		List<Object> list = savedStates.get(className);
		list.add(m);
	}
	
	public Object getMemento(Object m, int index) {
		String className = m.getClass().getName();
		return savedStates.get(className).get(index);
	}
	
	public int length(Object m) {
		String className = m.getClass().getName();
		return savedStates.get(className).size();
	}
}