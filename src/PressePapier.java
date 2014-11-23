/**
 * @(#) PressePapier.java
 */

public class PressePapier
{
	private String content;
	
	public String pull( )
	{
		System.out.println("PULL > " + content);
		return content;
	}
	
	public void put(String content)
	{
		System.out.println("PUT > " + content);
		this.content = content;
	}
	
}
