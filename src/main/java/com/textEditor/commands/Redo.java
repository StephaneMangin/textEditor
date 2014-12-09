package com.textEditor.commands;

/**
 * @(#) Cut.java
 */

public class Redo extends Command {

	public void execute() {
		core.redo();
	}
}
