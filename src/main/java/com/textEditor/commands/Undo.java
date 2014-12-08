package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Undo extends Command {

	public Undo(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.undo();
	}
}
