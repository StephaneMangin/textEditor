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
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#record()
	 */
	@Override
	public void record() {
		this.recording = true;
		recordCareTaker = new CareTaker();
		logger.info("recording ...");
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#stop()
	 */
	@Override
	public void stop() {
		this.recording = false;
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
			Selection position = new Selection();
			Pair<String, Memento> reference = undoRedoCareTaker.getMemento(undoRedoPosition);
			position.restoreFromMemento(reference.getMemento());
			switch (reference.getReference()) {
		        case "insert":
		    		position.previous();
		    		System.out.println(position.toJson().toString());
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
		        	this.delete(position);
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
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#redo()
	 */
	@Override
	public void redo() {
		undoRedo = true;
		if (isRedo()) {
			undoRedoPosition++;
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
	        	this.cut(position);
	            break;
	        case "copy":
	        	this.copy(position);
	            break;
	        case "paste":
	        	this.paste(position);
	            break;
			}
			logger.info(reference.getReference() + "::" + position.toJson().toString());
		}
		undoRedo = false;
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
			if (isRedo()) {
				undoRedoCareTaker.setSize(undoRedoPosition + 1);
			}
			undoRedoCareTaker.addMemento(methodName, position.saveToMemento());
			undoRedoPosition++;
			logger.fine("in index " + undoRedoPosition);
		}
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#insert(com.textEditor.core.Selection)
	 */
	@Override
	public void insert(Selection position) {
		saveUndoRedo("insert", position);
		buffer.insert(position.getStart(), position.getContent());
		logger.info(position.toJson().toString());
		setCurrentPosition(position.getStart() + position.getContent().length());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#delete(com.textEditor.core.Selection)
	 */
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

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#replace(com.textEditor.core.Selection)
	 */
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

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#cut(com.textEditor.core.Selection)
	 */
	@Override
	public void cut(Selection position) {
		saveUndoRedo("cut", position);
		logger.info(position.toJson().toString());
		clipboard = buffer.substring(position.getStart(), position.getEnd());
		buffer.delete(position.getStart(), position.getEnd());
		setCurrentPosition(position.getStart() + clipboard.length());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#copy(com.textEditor.core.Selection)
	 */
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

	/* (non-Javadoc)
	 * @see com.textEditor.core.CoreInterface#paste(com.textEditor.core.Selection)
	 */
	@Override
	public void paste(Selection position) {
		saveUndoRedo("paste", position);
		logger.fine(position.toString());
		if (clipboard == null) {
			logger.warning("Clipboard is not set !");
		}
		buffer.insert(position.getStart(), clipboard);
		position.setPrevContent(position.getContent());
		position.setContent(clipboard);
		logger.info(position.toJson().toString());
		setCurrentPosition(position.getStart() + position.getContent().length());
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
