package com.textEditor.command;

/**
 * @(#) Cut.java
 */

public class Redo extends Command {

	public void execute() {
		core.redo();
	}
}
