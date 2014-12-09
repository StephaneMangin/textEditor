package com.textEditor.command;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * Used to call a command class with core abstraction
 * @author blacknight
 *
 */
public class CommandInvoker {

	private Core core;

	public CommandInvoker(Core core) {
		this.core = core;
	}
	
	/**
	 * Invoke the command and call the execute
	 * @param cmd
	 */
	public void invoke(Command cmd) {
		cmd.setCore(core);
		cmd.execute();
	}
	
	/**
	 * Invoke the command and call the execute with parameters
	 * @param cmd
	 * @param position
	 */
	public void invoke(Command cmd, Selection position) {
		cmd.setCore(core);
		cmd.setPosition(position);
		cmd.execute();
	}
}
