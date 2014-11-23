/**
 * @(#) Paste.java
 */

public class Paste extends Command
{

	public Paste(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public Selection execute( Selection position )
	{
		return user.getCore().paste(position);
	}
	
}
