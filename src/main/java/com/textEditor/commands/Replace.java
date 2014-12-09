package com.textEditor.commands;

/**
 * @(#) Copy.java
 */

public class Replace extends Command {

	public void execute() {
		core.replace(position);
	}
}
