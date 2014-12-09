

import java.util.Observer;

import com.textEditor.commands.*;
import com.textEditor.core.*;
import com.textEditor.ihm.Gui;

/**
 * This is the User class which initiate the application
 * 
 * @(#) User.java
 */

public class User {

	private static Gui gui = new Gui("Basic Text Editor");
	private static Core core = new Core();
	
	public User() {
		core.addObserver((Observer) gui);
		core.addObserver((Observer) gui.getTextArea());
		CommandInvoker invoker =  new CommandInvoker(core);
		gui.setCommandInvoker(invoker);
	}

	public static void main(String[] args) {
		new User();
	}

}