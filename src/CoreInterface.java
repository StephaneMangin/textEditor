
/**
 * @(#) CoreInterface.java
 */

public interface CoreInterface {
	
	public void insert( Selection position );
	public void delete( Selection position );
	public void replace( Selection position );
	public void cut( Selection position );
	public void copy( Selection position );
	public void paste( Selection position );
	public String toString();
}
