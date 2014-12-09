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
	private boolean playing = false;
	private int currentPosition = 0;
	
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
	public boolean isRecording() {
		return recording;
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}
	
	@Override
	public void play() {
		playing = true;
		logger.info("playing...");
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
		playing = false;
	}

	@Override
	public void record() {
		this.recording = true;
		recordCareTaker = new CareTaker();
		logger.info("recording ...");
	}

	@Override
	public void stop() {
		this.recording = false;
		logger.info("record stopped ...");
	}

	@Override
	public boolean isUndo() {
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
			logger.info(reference.getL() + "::" + position.toJson().toString());
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
			logger.info(reference.getL() + "::" + position.toJson().toString());
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
			logger.fine("in index " + undoRedoPosition);
		}
	}

	@Override
	public void insert(Selection position) {
		saveUndoRedo("insert", position);
		buffer.insert(position.getStart(), position.getContent());
		logger.info(position.toJson().toString());
		setCurrentPosition(position.getStart() + position.getContent().length());
	}

	@Override
	public void delete(Selection position) {
		saveUndoRedo("delete", position);
		if (recording) {
			recordCareTaker.addMemento("delete", position.saveToMemento());
		}
		buffer.delete(position.getStart(), position.getEnd());
		logger.info(position.toJson().toString());
		setCurrentPosition(position.getStart());
	}

	@Override
	public void replace(Selection position) {
		saveUndoRedo("replace", position);
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		logger.info(position.toJson().toString());
		buffer.replace(position.getStart(), position.getEnd(), position.getContent());
		setCurrentPosition(position.getStart() + position.getContent().length());
	}

	@Override
	public void cut(Selection position) {
		saveUndoRedo("copy", position);
		logger.info(position.toJson().toString());
		clipboard = position.getContent();
		buffer.delete(position.getStart(), position.getEnd());
		setCurrentPosition(position.getStart());
	}

	@Override
	public void copy(Selection position) {
		saveUndoRedo("copy", position);
		if (recording) {
			recordCareTaker.addMemento("copy", position.saveToMemento());
			undoRedoPosition++;
		}
		buffer.insert(position.getStart(), position.getContent());
		logger.info(position.toJson().toString());
		clipboard = position.getContent();
		setCurrentPosition(position.getStart() + position.getContent().length());
	}

	@Override
	public void paste(Selection position) {
		logger.fine(position.toString());
		if (clipboard == null) {
			logger.warning("Clipboard is not set !");
		}
		buffer.insert(position.getStart(), position.getContent());
		position.setContent(clipboard);
		logger.info(position.toJson().toString());
		setCurrentPosition(position.getStart() + position.getContent().length());
	}

	private void setCurrentPosition(int pos) {
		currentPosition = pos;		
		setChanged();
		notifyObservers();
	}

	@Override
	public void reset() {
		undoRedoCareTaker = new CareTaker();
		undoRedoPosition = -1;
		buffer.setLength(0);
		setCurrentPosition(0);
		logger.info("done !");
	}
	
	public String toString() {
		return buffer.toString();
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

}
