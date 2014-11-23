/**
 * @(#) Copy.java
 */

public class Copy extends Command
{

	public Copy(User user) {
		super(user);
	}

	public Selection execute( Selection position )
	{
		return user.getCore().update(position);
	}
	
}
