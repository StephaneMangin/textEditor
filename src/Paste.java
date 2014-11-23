/**
 * @(#) Paste.java
 */

public class Paste extends Command
{

	public Paste(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public void execute( Selection position )
	{
		user.getCore().paste(position);
	}
	
}
