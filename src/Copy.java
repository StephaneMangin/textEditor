/**
 * @(#) Copy.java
 */

public class Copy extends Command
{

	public Copy(Core core) {
		super(core);
	}

	public void execute( Selection position )
	{
		core.copy(position);
	}
	
}
