import java.util.Observer;
import java.util.logging.*;

/**
 * @(#) User.java
 */

public class User {
	private static Copy copy;
	private static Paste paste;
	private static Cut cut;
	private static Insert insert;
	private static Delete delete;
	private static Replace replace;
	private static Gui gui;
	private static Core core;
	protected static Logger logger;
	
	public Core getCore() {
		return core;
	}

	public Gui getGui() {
		return gui;
	}
	
	public static void main(String[] args) {
		core = new Core();
		copy = new Copy(core);
		paste = new Paste(core);
		cut = new Cut(core);
		insert = new Insert(core);
		delete = new Delete(core);
		replace = new Replace(core);
		gui = new Gui("Basic Text Editor", copy, paste, cut, insert, delete, replace);
		core.addObserver((Observer) gui);
	}
	
}