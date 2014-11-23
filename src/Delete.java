/**
 * @(#) Copy.java
 */

public class Delete extends Command
{

	public Delete(User user) {
		super(user);
	}

	public void execute( Selection position )
	{
		user.getCore().delete(position);
	}
	
}
