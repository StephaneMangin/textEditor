package com.textEditor.core;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.textEditor.commands.*;
import com.textEditor.log.Log;
import com.textEditor.memento.CareTaker;
import com.textEditor.memento.Memento;

/**
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface {

	private CareTaker careTaker = new CareTaker();
	private CareTaker careTakerUndoRedo = new CareTaker();
	private StringBuffer buffer = new StringBuffer();
	private String clipboard = new String();
	private Log logger;
	private boolean recording = false;
	
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
			core.addObserver(this);
		}

		public void update(Observable core, Object obj) {
			jta.setText(obj.toString());
		}
	}
	
	public Core() {
		logger = new Log(this);
		new InternalGui(this);
	}

	public boolean isRecording() {
		return recording;
	}

	public void play() {
		for (int i=0;i<careTaker.size();i++) {
			Memento memento = careTaker.getMemento(i);
			String className = memento.getSavedState().getString("command");
			System.out.println(className);
			System.out.println(memento.getSavedState().toString());
			Command command = null;
			switch (className) {
            case "Insert":
            	command = new Insert(this);
                break;
            case "Delete":
            	command = new Delete(this);
                break;
            case "Replace":
            	command = new Replace(this);
                break;
            case "Cut":
            	command = new Cut(this);
                break;
            case "Copy":
            	command = new Copy(this);
                break;
            case "Paste":
            	command = new Paste(this);
                break;
			}
			command.restoreFromMemento(memento);
			command.execute(command.getSelection());
		}
		stop();
	}

	public void record() {
		this.recording = true;
		careTaker = new CareTaker();
	}

	public void stop() {
		this.recording = false;
	}
	
	public void insert(Selection position) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		Insert command = new Insert(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		if (buffer.length() >= position.getEnd()) {
			buffer.insert(position.getStart(), position.getContent());
			logger.fine(position.toString());
			// Mise à jour de la position
			position.jump();
			setChanged();
			notifyObservers(buffer);
		}
	}

	public void delete(Selection position) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		Delete command = new Delete(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		// buffer end argument is the last index of the string, not the length.
		if (buffer.length() >= position.getEnd()) {
			buffer.delete(position.getStart(), position.getStart() + position.getEnd());
			logger.fine(position.toString());
			// Mise à jour de la position
			position.reset();
			setChanged();
			notifyObservers(buffer);
		}
	}

	public void replace(Selection position) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		Replace command = new Replace(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		if (buffer.length() >= position.getEnd()) {
			logger.fine(position.toString());
			delete(position);
			insert(position);
		}
	}

	public void cut(Selection position) {
		Cut command = new Cut(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		if (buffer.length() >= position.getEnd()) {
			assert position.getStart() <= buffer.length();
			assert position.getEnd() <= buffer.length();
			logger.fine(position.toString());
			clipboard = position.getContent();
			delete(position);
		}
	}

	public void copy(Selection position) {
		Copy command = new Copy(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		if (buffer.length() >= position.getEnd()) {
			assert position.getStart() <= buffer.length();
			assert position.getEnd() <= buffer.length();
			logger.fine(position.toString());
			clipboard = position.getContent();
		}
	}
	
	public void paste(Selection position) {
		Paste command = new Paste(this);
		command.setPosition(position);
		careTakerUndoRedo.addMemento(command.saveToMemento());
		if (isRecording()) {
			careTaker.addMemento(command.saveToMemento());
		}
		if (buffer.length() >= position.getEnd()) {
			logger.fine(position.toString());
			if (clipboard == null) {
				logger.severe("Clipboard is not set !");
			}
			position.setContent(clipboard);
			insert(position);
		}
	}

	public String toString() {
		return buffer.toString();
	}
}
