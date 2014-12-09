package com.textEditor.command;

/**
 * @(#) Cut.java
 */

public class Undo extends Command {

	public void execute() {
		core.undo();
	}
}
