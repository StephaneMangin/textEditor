package com.textEditor.core;

/**
 * Main class that defines all needed operations for a class implementation
 * @(#) CoreInterface.java
 */

public interface CoreInterface {

	/**
	 * Return the content of the buffer
	 * @return
	 */
	public String getBuffer();
	/**
	 * Return the content of the clipboard
	 * @return
	 */
	public String getClipboard();
	/**
	 * Return true if the redo operation is allowed
	 * @return
	 */
	public boolean isRedo();
	/**
	 * Return true if the undo operation is allowed
	 * @return
	 */
	public boolean isUndo();
	/**
	 * Return true if the record operation is pending
	 * @return
	 */
	public boolean isRecording();
	/**
	 * Return true if the play operation is pending
	 * @return
	 */
	public boolean isPlaying();
	/**
	 * Insert a string into the buffer
	 * @param position
	 */
	public void insert(Selection position);
	/**
	 * Delete a string into the buffer
	 * @param position
	 */
	public void delete(Selection position);
	/**
	 * Replace a string into the buffer
	 * @param position
	 */
	public void replace(Selection position);
	/**
	 * Cut a string into the clipboard and delete from the buffer
	 * @param position
	 */
	public void cut(Selection position);
	/**
	 * Copy a string into the buffer
	 * @param position
	 */
	public void copy(Selection position);
	/**
	 * Paste a string into the buffer from the clipboard
	 * Keep the clipboard intact
	 * @param position
	 */
	public void paste(Selection position);
	/**
	 * Set the record state and execute
	 */
	public void record();
	/**
	 * Set the play state and execute
	 */
	public void play();
	/**
	 * Stop the record state and finalize
	 */
	public void stop();
	/**
	 * Apply the previous operation
	 * Concording to usUndo() return state
	 */
	public void undo();
	/**
	 * Apply the next operation
	 * Concording to usRedo() return state
	 */
	public void redo();
	/**
	 * Reset the class to its initial default values
	 */
	public void reset();
	
	public String toString();
}
