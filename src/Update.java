/**
 * @(#) Copy.java
 */

public class Update extends Command
{

	public Update(User user) {
		super(user);
	}

	public Selection execute( Selection position )
	{
		return user.getCore().update(position);
	}
	
}
