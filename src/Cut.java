/**
 * @(#) Cut.java
 */

public class Cut extends Command
{

	public Cut(User user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

	public void execute( Selection position )
	{
		user.getCore().cut(position);
	}
	
}
