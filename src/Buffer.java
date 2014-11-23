/**
 * @(#) Buffer.java
 */

public class Buffer
{
	private StringBuffer content = new StringBuffer();
	
	public String read(Integer start, Integer end)
	{
		System.out.println("READ =>" + this.toString().substring(start, end));
		return content.substring(start, end);
	}
	
	public void write(Integer start, String string)
	{
		System.out.println("WRITE =>" + string);
		content.insert(start, string);
	}

	public void delete(Integer start, Integer end){
		System.out.println("DELETE => " + start + ">" + end);
		content.delete(start, end);
	}
	
	public String toString() {
		return content.toString();
	}
	
	public Integer length(){
		return content.length();
	}
	
}
