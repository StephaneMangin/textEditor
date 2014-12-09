package com.textEditor.command;

/**
 * @(#) Cut.java
 */

public class Stop extends Command {

	public void execute() {
		core.stop();
	}
}
