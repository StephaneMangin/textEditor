package com.textEditor.commands;

import com.textEditor.core.Command;
import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Paste.java
 */

public class Paste extends Command {

	public Paste(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.paste(position);
	}

}
