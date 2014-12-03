package com.textEditor.commands;

import com.textEditor.core.Core;

/**
 * @(#) Cut.java
 */

public class Record extends Command {

	public Record(Core core) {
		super(core);
	}

	public void execute() {
		core.record();
	}
}
