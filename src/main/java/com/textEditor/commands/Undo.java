package com.textEditor.commands;

/**
 * @(#) Cut.java
 */

public class Undo extends Command {

	public void execute() {
		core.undo();
	}
}
