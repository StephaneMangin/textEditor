/**
 * @(#) Core.java
 */

public class Core implements CoreInterface
{
	private Buffer buffer = new Buffer();
	private PressePapier prpa = new PressePapier();
	
	public void write(Selection position)
	{
		buffer.write(position.getStart(), position.getContent());
		// Mise à jour de la position
		position.jump();
	}
	
	public void delete(Selection position)
	{
		buffer.delete(position.getStart(), position.getLength());
		// Mise à jour de la position
		position.jump();
	}
	
	public void replace(Selection position)
	{
		delete(position);
		write(position);
	}
	
	public void cut( Selection position )
	{
		prpa.put(position.getContent());
		delete(position);
		// Mise à jour de la position
		position.jump();
	}
	
	public void copy( Selection position )
	{
		prpa.put(position.getContent());
	}
	
	public void paste( Selection position )
	{
		position.setContent(prpa.pull());
		write(position);
	}
	
	public String toString(){
		return this.buffer.toString();
	}

}
