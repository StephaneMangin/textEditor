/**
 * @(#) Copy.java
 */

public class Write extends Command
{

	public Write(User user) {
		super(user);
	}

	public void execute( Selection position )
	{
		user.getCore().write(position);
	}
	
}
