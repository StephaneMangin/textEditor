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
 * Core class
 * Manage all the operation execution and notify observers when states have changed
 * 
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface {

	private Log logger;
	/**
	 * The text saver
	 */
	private StringBuffer buffer = new StringBuffer();
	/**
	 * The clipboard saver
	 * Only one value at a time
	 */
	private String clipboard = new String();
	/**
	 * Return true if in undo/redo mode
	 * Usefull to prevent operation saves twice
	 */
	private boolean undoRedo = false;
	/**
	 *  Manage the position of the undo/redo curso
	 */
	private int undoRedoPosition = -1;
	/**
	 * Saves the undo/redo states
	 */
	private CareTaker undoRedoCareTaker = new CareTaker();
	/**
	 * Return true if in recording mode
	 */
	private boolean recording = false;
	/**
	 * Saves recording states
	 */
	private CareTaker recordCareTaker = new CareTaker();
	/**
	 * Return true if in playing mode
	 */
	private boolean playing = false;
	/**
	 * Saves the current cursor position received by the Selection object at all time
	 */
	private int currentPosition = 0;
	
	/**
	 * Nested class to allow show buffer and clipboard content while using the interface
	 * For test purpose only
	 *
	 */
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
		//InternalGui gui = new InternalGui(this);
		//addObserver(gui);
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#getBuffer()
	 */
	@Override
	public String getBuffer() {
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#getClipboard()
	 */
	@Override
	public String getClipboard() {
		return clipboard.toString();
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#isRecording()
	 */
	@Override
	public boolean isRecording() {
		return recording;
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#isPlaying()
	 */
	@Override
	public boolean isPlaying() {
		return playing;
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#play()
	 */
	@Override
	public void play() {
		playing = true;
		logger.info("playing...");
		for (int i=0;i<recordCareTaker.size();i++) {

			Selection position = new Selection();
			Pair<String, Memento> reference = recordCareTaker.getMemento(i);
			position.restoreFromMemento(reference.getMemento());
			switch (reference.getReference()) {
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
		setChanged();
		notifyObservers();
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#record()
	 */
	@Override
	public void record() {
		this.recording = true;
		setChanged();
		notifyObservers();
		recordCareTaker = new CareTaker();
		logger.info("recording ...");
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#stop()
	 */
	@Override
	public void stop() {
		this.recording = false;
		setChanged();
		notifyObservers();
		logger.info("record stopped ...");
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#isUndo()
	 */
	@Override
	public boolean isUndo() {
		return undoRedoPosition >= 0;
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#isRedo()
	 */
	@Override
	public boolean isRedo() {
		return undoRedoPosition < undoRedoCareTaker.size() - 1;
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#undo()
	 */
	@Override
	public void undo() {
		undoRedo = true;
		if (isUndo()) {
			logger.info("in index " + undoRedoPosition);
			Selection position = new Selection();
			Pair<String, Memento> reference = undoRedoCareTaker.getMemento(undoRedoPosition);
			position.restoreFromMemento(reference.getMemento());
			switch (reference.getReference()) {
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
		    		clipboard = position.getContent();
		            break;
		        case "paste":
		    		position.previous();
		        	this.replace(position);
		            break;
			}
			undoRedoPosition--;
			logger.info(reference.getReference() + "::" + position.toJson().toString());
		}
		undoRedo = false;
		setChanged();
		notifyObservers();
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#redo()
	 */
	@Override
	public void redo() {
		undoRedo = true;
		if (isRedo()) {
			undoRedoPosition++;
			logger.info("in index " + undoRedoPosition);
			Selection position = new Selection();
			Pair<String, Memento> reference = undoRedoCareTaker.getMemento(undoRedoPosition);
			position.restoreFromMemento(reference.getMemento());
			switch (reference.getReference()) {
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
	        	this.delete(position);
	            break;
	        case "copy":
	            break;
	        case "paste":
	        	this.replace(position);
	            break;
			}
			logger.info(reference.getReference() + "::" + position.toJson().toString());
		}
		undoRedo = false;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Centralize the undo/redo changes
	 * If an operation is done while the redo state is true, all succeding states will be lost
	 * 
	 * @param methodName
	 * @param position
	 */
	private void saveUndoRedo(String methodName, Selection position) {
		if (!undoRedo) {
			// On réinitialise le compteur si des redos restent alors qu'une opération est effectuée
			if (isRedo()) {
				undoRedoCareTaker.setSize(undoRedoPosition + 1);
			}
			undoRedoCareTaker.addMemento(methodName, position.saveToMemento());
			undoRedoPosition++;
			logger.info("in index " + undoRedoPosition);
			setChanged();
			notifyObservers();
		}
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#insert(com.textEditor.core.Selection)
	 */
	@Override
	public void insert(Selection position) {
		// First save metadatas to Selection
		position.setPrevContent("");
		if (recording) {
			recordCareTaker.addMemento("insert", position.saveToMemento());
		}
		// Apply the operation
		buffer.insert(position.getStart(), position.getContent());
		saveUndoRedo("insert", position);
		setCurrentPosition(position.getStart() + position.getContent().length());
		logger.info(position.toJson().toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#delete(com.textEditor.core.Selection)
	 */
	@Override
	public void delete(Selection position) {
		// First save metadatas to Selection
		position.setContent("");
		position.setPrevContent(buffer.substring(position.getStart(), position.getEnd()));
		if (recording) {
			recordCareTaker.addMemento("delete", position.saveToMemento());
		}
		// Apply the operation
		buffer.delete(position.getStart(), position.getEnd());
		// Update the cursor
		setCurrentPosition(position.getStart());
		// Undo/redo updating
		saveUndoRedo("delete", position);
		logger.info(position.toJson().toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#replace(com.textEditor.core.Selection)
	 */
	@Override
	public void replace(Selection position) {
		// First save metadatas to Selection
		position.setPrevContent(buffer.substring(position.getStart(), position.getEnd()));
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		// Apply the operation
		buffer.replace(position.getStart(), position.getEnd(), position.getContent());
		// Update the cursor
		setCurrentPosition(position.getStart() + position.getContent().length());
		// Undo/redo updating
		saveUndoRedo("replace", position);
		logger.info(position.toJson().toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#cut(com.textEditor.core.Selection)
	 */
	@Override
	public void cut(Selection position) {
		// First save metadatas to Selection
		clipboard = buffer.substring(position.getStart(), position.getEnd());
		position.setPrevContent(clipboard);
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		// Apply the operation
		buffer.delete(position.getStart(), position.getEnd());
		// Update the cursor
		setCurrentPosition(position.getStart());
		// Undo/redo updating
		saveUndoRedo("cut", position);
		logger.info(position.toJson().toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#copy(com.textEditor.core.Selection)
	 */
	@Override
	public void copy(Selection position) {
		if (recording) {
			recordCareTaker.addMemento("copy", position.saveToMemento());
			undoRedoPosition++;
		}
		// First save metadatas
		position.setPrevContent(clipboard);
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		// Apply the operation
		clipboard = buffer.substring(position.getStart(), position.getEnd());
		// Update the cursor
		setCurrentPosition(position.getStart());
		// Undo/redo updating
		saveUndoRedo("copy", position);
		logger.info(position.toJson().toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#paste(com.textEditor.core.Selection)
	 */
	@Override
	public void paste(Selection position) {
		if (clipboard == null) {
			logger.warning("Clipboard is not set !");
		}
		// First save metadatas to Selection
		position.setContent(clipboard);
		position.setPrevContent(buffer.substring(position.getStart(), position.getEnd()));
		if (recording) {
			recordCareTaker.addMemento("replace", position.saveToMemento());
		}
		// Apply the operation
		buffer.replace(position.getStart(), position.getEnd(), clipboard);
		setCurrentPosition(position.getStart() + clipboard.length());
		// Undo/redo updating
		saveUndoRedo("paste", position);
		logger.info(position.toJson().toString());
	}

	/**
	 * Return the current position of the cursor after or before an operation
	 * @return
	 */
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * Set the position of the cursor after an operation
	 * Also send the state's change signal to all observers
	 * @param pos
	 */
	private void setCurrentPosition(int pos) {
		currentPosition = pos;
		setChanged();
		notifyObservers();
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#reset()
	 */
	@Override
	public void reset() {
		undoRedoCareTaker = new CareTaker();
		recordCareTaker = new CareTaker();
		undoRedoPosition = -1;
		clipboard = "";
		buffer.setLength(0);
		setCurrentPosition(0);
		logger.info("done !");
	}
	
	public String toString() {
		return buffer.toString();
	}


}
