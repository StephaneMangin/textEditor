package com.textEditor.commands;

/**
 * @(#) Copy.java
 */

public class Insert extends Command {

	public void execute() {
		core.insert(position);
	}
}
