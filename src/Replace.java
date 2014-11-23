/**
 * @(#) Copy.java
 */

public class Replace extends Command
{

	public Replace(User user) {
		super(user);
	}

	public void execute( Selection position )
	{
		user.getCore().replace(position);
	}
	
}
