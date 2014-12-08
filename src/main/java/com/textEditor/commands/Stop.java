package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Stop extends Command {

	public Stop(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.stop();
	}
}
