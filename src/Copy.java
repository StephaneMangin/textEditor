/**
 * @(#) Copy.java
 */

public class Copy extends Command
{

	public Copy(User user) {
		super(user);
	}

	public void execute( Selection position )
	{
		user.getCore().copy(position);
	}
	
}
