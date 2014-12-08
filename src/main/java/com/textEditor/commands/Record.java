package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Record extends Command {

	public Record(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.record();
	}
}
