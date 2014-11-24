/**
 * @(#) Paste.java
 */

public class Paste extends Command
{

	public Paste(Core core) {
		super(core);
	}

	public void execute( Selection position )
	{
		core.paste(position);
	}
	
}
