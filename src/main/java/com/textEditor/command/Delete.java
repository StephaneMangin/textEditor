package com.textEditor.command;

/**
 * @(#) Copy.java
 */

public class Delete extends Command {

	public void execute() {
		core.delete(position);
	}
}
