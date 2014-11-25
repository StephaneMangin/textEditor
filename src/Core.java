import java.util.Observable;

/**
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface {
	
	private StringBuffer buffer = new StringBuffer();
	private String clipboard = new String();
	private Log logger;
	
	Core () {
		logger = new Log(this);
	}
	
	public void insert(Selection position) {
		buffer.insert(position.getStart(), position.getContent());
		logger.fine("insert = " + position.toString());
		// Mise à jour de la position
		position.jump();
		setChanged();
		notifyObservers();
	}
	
	public void delete(Selection position) {
		assert position.getStart() <= buffer.length();
		assert position.getStart() + position.getEnd() <= buffer.length();
		// buffer end argument is the last index of the string, not the length.
		buffer.delete(position.getStart(), position.getStart() + position.getEnd());
		logger.fine("delete = " + position.toString());
		// Mise à jour de la position
		position.reset();
		setChanged();
		notifyObservers();
	}
	
	public void replace(Selection position) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		logger.fine("replace = " + position.toString());
		delete(position);
		insert(position);
	}
	
	public void cut( Selection position ) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		logger.fine("cut = " + position.toString());
		clipboard = position.getContent();
		delete(position);
	}
	
	public void copy( Selection position ) {
		assert position.getStart() <= buffer.length();
		assert position.getEnd() <= buffer.length();
		logger.fine("copy = " + position.toString());
		clipboard = position.getContent();
	}
	
	public void paste( Selection position ) {
		logger.fine("paste = " + position.toString());
		if (clipboard == null) {
			logger.severe("Clipboard is not set !");
		}
		position.setContent(clipboard);
		insert(position);
	}
	
	public String toString(){
		return buffer.toString();
	}
}
