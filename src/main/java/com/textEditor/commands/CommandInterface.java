package com.textEditor.commands;

import com.textEditor.core.Core;
import com.textEditor.core.Selection;

/**
 * @(#) CommandInterface.java
 */

public interface CommandInterface {

	public void setCore(Core core);
	public void setPosition(Selection position);
	public void execute();

}
