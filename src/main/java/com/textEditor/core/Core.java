package com.textEditor.core;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.textEditor.log.Log;
import com.textEditor.memento.CareTaker;
import com.textEditor.memento.Memento;
import com.textEditor.memento.Pair;

/**
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface {

	private StringBuffer buffer = new StringBuffer();
	private String clipboard = new String();
	private Log logger;
	

	private boolean undoRedo = false;
	private int undoRedoPosition = -1;
	private CareTaker undoRedoCareTaker = new CareTaker();
	private boolean recording = false;
	private CareTaker recordCareTaker = new CareTaker();
	
	public class InternalGui extends JFrame implements Observer {

		private static final long serialVersionUID = 1L;
		private JTextArea jta;
		private JTextField jtf;
		private JScrollPane jscroll;

		public InternalGui(Core core) {
			setLayout(new BorderLayout());
			jta = new JTextArea();
			jtf = new JTextField();
			jta.setFont(new Font("Arial", Font.PLAIN, 16));
			jscroll = new JScrollPane(jta);
			add(jtf, BorderLayout.NORTH);
			add(jscroll, BorderLayout.CENTER);
			setSize(500, 300);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

		public void update(Observable observable, Object obj) {
			if (((Core) observable).getClipboard() != jtf.getText()) {
				jtf.setText(clipboard);
			}
			if (((Core) observable).getBuffer() != jta.getText()) {
				jta.setText(((Core) observable).getBuffer());
			}
		}
	}
	
	public Core() {
		logger = new Log(this);
		InternalGui gui = new InternalGui(this);
		addObserver(gui);
	}

	@Override
	public String getBuffer() {
		return buffer.toString();
	}

	@Override
	public String getClipboard() {
		return clipboard.toString();
	}

	@Override
	public void play() {
		for (int i=0;i<recordCareTaker.size();i++) {

			Selection position = new Selection();
			Pair<String, Memento> reference = recordCareTaker.getMemento(i);
			position.restoreFromMemento(reference.getR());
			switch (reference.getL()) {
            case "insert":
            	this.insert(position);
                break;
            case "delete":
            	this.delete(position);
                break;
            case "replace":
            	this.replace(position);
                break;
            case "cut":
            	this.cut(position);
                break;
            case "copy":
            	this.copy(position);
                break;
            case "paste":
            	this.paste(position);
                break;
			}
		}
	}

	@Override
	public void record() {
		this.recording = true;
		recordCareTaker = new CareTaker();
	}

	@Override
	public void stop() {
		this.recording = false;
	}

	@Override
	public boolean isUndo() {
		System.out.println(undoRedoPosition >= 0);
		System.out.println(undoRedoPosition < 0);
		return undoRedoPosition >= 0;
	}

	@Override
	public boolean isRedo() {
		return undoRedoPosition < undoRedoCareTaker.size() - 1;
	}
	
	@Override
	public void undo() {
		undoRedo = true;
		if (isUndo()) {
			Selection position = new Selection();
			Pair<String, Memento> reference = undoRedoCareTaker.getMemento(undoRedoPosition);
			position.restoreFromMemento(reference.getR());
			switch (reference.getL()) {
		        case "insert":
		    		position.previous();
		        	this.delete(position);
		            break;
		        case "delete":
		    		position.previous();
		        	this.insert(position);
		            break;
		        case "replace":
		    		position.previous();
		        	this.replace(position);
		            break;
		        case "cut":
		    		position.previous();
		        	this.insert(position);
		            break;
		        case "copy":
		    		position.previous();
		        	this.copy(position);
		            break;
		        case "paste":
		    		position.previous();
		        	this.replace(position);
		            break;
			}
			undoRedoPosition--;
			setChanged();
			notifyObservers();
		}
		undoRedo = false;
	}

	@Override
	public void redo() {
		undoRedo = true;
		if (isRedo()) {
			undoRedoPosition++;
			Selection position = new Selection();
			Pair<String, Memento> reference = undoRedoCareTaker.getMemento(undoRedoPosition);
			position.restoreFromMemento(reference.getR());
			switch (reference.getL()) {
	        case "insert":
	        	this.insert(position);
	            break;
	        case "delete":
	        	this.delete(position);
	            break;
	        case "replace":
	        	this.replace(position);
	            break;
	        case "cut":
	        	this.cut(position);
	            break;
	        case "copy":
	        	this.copy(position);
	            break;
	        case "paste":
	        	this.paste(position);
	            break;
			}
			setChanged();
			notifyObservers();
		}
		undoRedo = false;
	}
	
	private void saveUndoRedo(String methodName, Selection position) {
		if (!undoRedo) {
			if (isRedo()) {
				undoRedoCareTaker.setSize(undoRedoPosition + 1);
			}
			undoRedoCareTaker.addMemento(methodName, position.saveToMemento());
			undoRedoPosition++;
		}
	}

	@Override
	public void insert(Selection position) {
		saveUndoRedo("insert", position);
		buffer.insert(position.getStart(), position.getContent());
		logger.fine(position.toString());
		setChanged();
		notifyObservers();
	}

	@Override
	public void delete(Selection position) {
		saveUndoRedo("delete", position);
		if (recording) {
			recordCareTaker.addMemento("delete", position.saveToMemento());
		}
		buffer.delete(position.getStart(), position.getEnd());
		logger.fine(position.toString());
		setChanged();
		notifyObservers();
	}

	@Override
	public void replace(Selection position) {
		saveUndoRedo("replace", position);
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		logger.fine(position.toString());
		delete(position);
		insert(position);
	}

	@Override
	public void cut(Selection position) {
		logger.fine(position.toString());
		clipboard = position.getContent();
		delete(position);
	}

	@Override
	public void copy(Selection position) {
		saveUndoRedo("copy", position);
		if (recording) {
			recordCareTaker.addMemento("copy", position.saveToMemento());
			undoRedoPosition++;
		}
		logger.fine(position.toString());
		clipboard = position.getContent();
	}

	@Override
	public void paste(Selection position) {
		logger.fine(position.toString());
		if (clipboard == null) {
			logger.warning("Clipboard is not set !");
		}
		position.setContent(clipboard);
		insert(position);
	}

	@Override
	public void reset(Selection position) {
		undoRedoCareTaker = new CareTaker();
		undoRedoPosition = -1;
		buffer.setLength(0);
	}
	
	public String toString() {
		return buffer.toString();
	}

}
