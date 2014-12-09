package com.textEditor.command;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;


/**
 * 
 * @(#) Command.java
 */

public class Command implements CommandInterface {

	protected Core core;
	protected Selection position;

	/* (non-Javadoc)
	 * @see com.textEditor.command.CommandInterface#setCore(com.textEditor.core.Core)
	 */
	@Override
	public void setCore(Core core) {
		this.core = core;
	}

	/* (non-Javadoc)
	 * @see com.textEditor.command.CommandInterface#setPosition(com.textEditor.core.Selection)
	 */
	@Override
	public void setPosition(Selection position) {
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see com.textEditor.command.CommandInterface#execute()
	 */
	@Override
	public void execute() {
		return;
	}
}