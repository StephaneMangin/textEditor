package com.textEditor.commands;

import com.textEditor.core.Command;
import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Cut extends Command {

	public Cut(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.cut(position);
	}

}
