import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @(#) Core.java
 */

public class Core extends Observable implements CoreInterface {
	
	public class InternalGui extends JFrame implements Observer {

		private static final long serialVersionUID = 1L;
		private JTextArea jta;
		private JTextField jtf;
		private JScrollPane jscroll;

		public InternalGui(Core core) {
			setLayout(new BorderLayout());

			jta = new JTextArea();
			jtf = new JTextField();
			jta.setFont(new Font("Arial", Font.PLAIN , 16) );

			jscroll = new JScrollPane(jta);
			add(jtf, BorderLayout.NORTH);
			add(jscroll, BorderLayout.CENTER);
			setSize(500,300);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			core.addObserver(this);
		}

		public void update(Observable core, Object obj) {
			jta.setText(obj.toString());
		}
	}
	
	private StringBuffer buffer = new StringBuffer();
	private String clipboard = new String();
	private Log logger;
	private InternalGui gui;
	
	Core () {
		logger = new Log(this);
		gui = new InternalGui(this);
	}
	
	public void insert(Selection position) {
		buffer.insert(position.getStart(), position.getContent());
		logger.fine("insert = " + position.toString());
		// Mise à jour de la position
		position.jump();
		setChanged();
		notifyObservers(buffer);
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
		notifyObservers(buffer);
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
