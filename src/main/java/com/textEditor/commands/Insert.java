package com.textEditor.commands;

import com.textEditor.core.Command;
import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Copy.java
 */

public class Insert extends Command {

	public Insert(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.insert(position);
	}

}
