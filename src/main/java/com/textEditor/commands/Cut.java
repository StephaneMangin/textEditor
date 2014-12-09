package com.textEditor.commands;

/**
 * @(#) Cut.java
 */

public class Cut extends Command {

	public void execute() {
		core.cut(position);
	}
}
