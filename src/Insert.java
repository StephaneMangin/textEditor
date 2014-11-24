/**
 * @(#) Copy.java
 */

public class Insert extends Command
{

	public Insert(Core core) {
		super(core);
	}

	public void execute( Selection position )
	{
		core.insert(position);
	}
	
}