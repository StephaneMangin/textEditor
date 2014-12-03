package com.textEditor.commands;

import com.textEditor.core.Selection;

/**
 * @(#) CommandInterface.java
 */

public interface CommandInterface {

	public void execute(Selection position);

}
