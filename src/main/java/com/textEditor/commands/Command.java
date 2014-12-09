package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;


/**
 * @(#) Command.java
 */

public class Command implements CommandInterface {

	protected Core core;
	protected Selection position;

	@Override
	public void setCore(Core core) {
		this.core = core;
	}

	@Override
	public void setPosition(Selection position) {
		this.position = position;
	}

	@Override
	public void execute() {
		return;
	}
}