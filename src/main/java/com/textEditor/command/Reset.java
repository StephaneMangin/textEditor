package com.textEditor.command;

/**
 * @(#) Cut.java
 */

public class Reset extends Command {

	public void execute() {
		core.reset();
	}
}
