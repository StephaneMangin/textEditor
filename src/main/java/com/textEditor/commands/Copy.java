package com.textEditor.commands;

/**
 * @(#) Copy.java
 */

public class Copy extends Command {

	public void execute() {
		core.copy(position);
	}
}
