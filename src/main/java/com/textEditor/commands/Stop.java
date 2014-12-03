package com.textEditor.commands;

import com.textEditor.core.Core;

/**
 * @(#) Cut.java
 */

public class Stop extends Command {

	public Stop(Core core) {
		super(core);
	}

	public void execute() {
		core.stop();
	}
}
