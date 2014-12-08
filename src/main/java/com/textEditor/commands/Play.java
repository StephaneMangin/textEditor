package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) Cut.java
 */

public class Play extends Command {

	public Play(Core core) {
		super(core);
	}

	public void execute(Selection position) {
		core.play();
	}
}
