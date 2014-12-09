package com.textEditor.core;

/**
 * @(#) CoreInterface.java
 */

public interface CoreInterface {

	public String getBuffer();
	public String getClipboard();
	public boolean isRedo();
	public boolean isUndo();
	public boolean isRecording();
	public boolean isPlaying();
	public void insert(Selection position);
	public void delete(Selection position);
	public void replace(Selection position);
	public void cut(Selection position);
	public void copy(Selection position);
	public void paste(Selection position);
	public void record();
	public void play();
	public void stop();
	public void undo();
	public void redo();
	public void reset();
	
	public String toString();
}
