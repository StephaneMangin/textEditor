package com.textEditor.command;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * Manage the mandatory operations for a command class
 * @(#) CommandInterface.java
 */

public interface CommandInterface {

	/**
	 * Link to the core
	 * @param core
	 */
	public void setCore(Core core);
	/**
	 * Link to a position
	 * @param position
	 */
	public void setPosition(Selection position);
	/**
	 * Execute the operation
	 * 
	 */
	public void execute();

}
