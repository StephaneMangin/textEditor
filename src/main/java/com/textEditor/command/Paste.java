package com.textEditor.command;

/**
 * @(#) Paste.java
 */

public class Paste extends Command {

	public void execute() {
		core.paste(position);
	}
}
