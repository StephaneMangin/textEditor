package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;


/**
 * @(#) Command.java
 */

public class Command implements CommandInterface {

	protected Core core;

	public Command(Core core) {
		this.core = core;
	}

	public void execute(Selection position) {
		return;
	}
}
