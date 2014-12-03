package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Copy.java
 */

public class Replace extends Command {

	public Replace(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.replace(position);
	}
}
