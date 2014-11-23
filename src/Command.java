/**
 * @(#) Command.java
 */

public class Command implements CommandInterface
{

	protected User user;
	
	public Command(User user){
		this.user = user;
	}
	
	public Selection execute(Selection position) {
		return position;
	}
	
}
