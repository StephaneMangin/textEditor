package com.textEditor.command;

/**
 * @(#) Cut.java
 */

public class Cut extends Command {

	public void execute() {
		core.cut(position);
	}
}
