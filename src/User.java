/**
 * @(#) User.java
 */

public class User {
	public Copy copy;
	public Paste paste;
	public Cut cut;
	private static Gui gui;
	private Core core;

	public Core getCore() {
		return core;
	}

	public Gui getGui() {
		return gui;
	}

	public User() {
		core = new Core();
		copy = new Copy(this);
		paste = new Paste(this);
		cut = new Cut(this);
	}

	public static void main(String[] args) {
		User user = new User();
		gui = new Gui("Basic Text Editor", user);
	}
	
}