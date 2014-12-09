package com.textEditor.command;

/**
 * @(#) Copy.java
 */

public class Copy extends Command {

	public void execute() {
		core.copy(position);
	}
}
