package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Redo extends Command {

	public Redo(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.redo();
	}
}
