/**
 * @(#) Cut.java
 */

public class Cut extends Command
{

	public Cut(Core core) {
		super(core);
	}

	public void execute( Selection position )
	{
		core.cut(position);
	}
	
}
