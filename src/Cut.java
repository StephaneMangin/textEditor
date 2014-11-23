/**
 * @(#) Cut.java
 */

public class Cut extends Command
{

	public Cut(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public Selection execute( Selection position )
	{
		return user.getCore().cut(position);
	}
	
}
