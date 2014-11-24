import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;


interface GuiInterface extends ActionListener, KeyListener, Observer {

	interface TextArea {
		static final long serialVersionUID = 1L;
	}
	public void update(Observable observable, Object objectConcerne);
	public void exit();
	public void open(); 
	public void save();
	public void write();

}
