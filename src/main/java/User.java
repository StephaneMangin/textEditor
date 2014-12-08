

import java.util.Observer;

import com.textEditor.commands.*;
import com.textEditor.core.*;
import com.textEditor.ihm.Gui;

/**
 * @(#) User.java
 */

public class User {

	private static Gui gui = new Gui("Basic Text Editor");;
	private static Core core = new Core();

	public User() {
		core.addObserver((Observer) gui);
		gui.setCommands(
				new Copy(core),
				new Paste(core),
				new Cut(core),
				new Insert(core),
				new Delete(core),
				new Replace(core),
				new Record(core),
				new Play(core),
				new Stop(core));
	}

	public static void main(String[] args) {
		new User();
	}

}