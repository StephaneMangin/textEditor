package com.textEditor.commands;

import com.textEditor.core.Core;

/**
 * @(#) Cut.java
 */

public class Play extends Command {

	public Play(Core core) {
		super(core);
	}

	public void execute() {
		core.play();
	}
}
