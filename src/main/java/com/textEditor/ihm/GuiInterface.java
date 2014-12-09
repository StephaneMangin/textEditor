package com.textEditor.ihm;

import com.textEditor.commands.CommandInvoker;

/**
 * Main class that defines all the mandatory operations for a class implementation
 * 
 * @author blacknight
 *
 */
interface GuiInterface {

	/**
	 * Start the recorder
	 */
	public void record();
	/**
	 * Stop the recorder
	 */
	public void stop();
	/**
	 * Play the previous record
	 */
	public void play();
	/**
	 * Exit the application
	 */
	public void exit();
	/**
	 * Open a file
	 */
	public void open();
	/**
	 * Save the current file
	 */
	public void save();
	/**
	 * Write to a file
	 */
	public void write();
	/**
	 * Undo the previous operation
	 */
	public void undo();
	/**
	 * Redo the next operation
	 */
	public void redo();
	/**
	 * Link the class to a command invoket
	 * Concording to command pattern integration
	 * @param invoker
	 */
	public void setCommandInvoker(CommandInvoker invoker);

}
