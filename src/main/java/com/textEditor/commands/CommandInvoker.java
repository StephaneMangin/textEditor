package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

public class CommandInvoker {

	private Core core;

	public CommandInvoker(Core core) {
		this.core = core;
	}
	
	public void invoke(Command cmd) {
		cmd.setCore(core);
		cmd.execute();
	}
	
	public void invoke(Command cmd, Selection position) {
		cmd.setCore(core);
		cmd.setPosition(position);
		cmd.execute();
	}
}
