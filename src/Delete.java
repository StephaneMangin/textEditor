/**
 * @(#) Copy.java
 */

public class Delete extends Command
{

	public Delete(Core core) {
		super(core);
	}

	public void execute( Selection position )
	{
		core.delete(position);
	}
	
}
