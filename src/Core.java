import java.util.Observable;
import java.util.Stack;

/**
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface
{
	private StringBuffer buffer = new StringBuffer();
	private String clipboard = new String();
	
	public void insert(Selection position)
	{
		buffer.insert(position.getStart(), position.getContent());
		// Mise à jour de la position
		position.jump();
		setChanged();
		notifyObservers();
	}
	
	public void delete(Selection position)
	{
		buffer.delete(position.getStart(), position.getLength());
		// Mise à jour de la position
		position.jump();
		setChanged();
		notifyObservers();
	}
	
	public void replace(Selection position)
	{
		delete(position);
		insert(position);
	}
	
	public void cut( Selection position )
	{
		clipboard = position.getContent();
		delete(position);
		// Mise à jour de la position
	}
	
	public void copy( Selection position )
	{
		clipboard = position.getContent();
	}
	
	public void paste( Selection position )
	{
		position.setContent(clipboard);
		insert(position);
	}
	
	public String toString(){
		return buffer.toString();
	}

}
