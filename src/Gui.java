import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;


public class Gui extends JFrame implements Observer, ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Cut cut;
	private Copy copy;
	private Paste paste;
	private Insert insert;
	private Delete delete;
	private Replace replace;

	private JTextArea jta;
	private JScrollPane jscroll;

	private JToolBar jtbar;
	private JButton bttnopen, bttnnew, bttnsave,bttncut, bttncopy,bttnpaste;

	private JMenuBar mbar;
	private JMenu file,edit;
	private JMenuItem fnew , fexit, fopen, fsave;
	private JMenuItem ecut , ecopy, epaste, eselall;

	private ImageIcon iNew, iOpen, iSave, iCut, iCopy, iPaste;

	private String fname;
	private boolean chg;
	private Log logger;
	
	private class TextArea extends JTextArea {

		private static final long serialVersionUID = 1L;
		
		public void insert(String str, int pos) {
			Selection position = new Selection(pos, 0, str);
			Gui.this.insert.execute(position);
		}

		public void delete(int start, int end) {
			Selection position = new Selection(start, end, "");
			Gui.this.delete.execute(position);
		}
		public void append(String str) {
			Selection position = new Selection(getCaretPosition(), getCaretPosition(), str);
			Gui.this.insert.execute(position);
		}

		public void replaceRange(String str, int start, int end) {
			Selection position = new Selection(start, end, str);
			Gui.this.replace.execute(position);
		}

		public void replaceSelection(String str) {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), str);
			// In all cases, JTextArea call this method whatever the currently operation
			// We have to look at positions to know about the current operation.
			// It can be either a simple char append or a replacement.
			if (position.getStart() == position.getEnd()) {
				Gui.this.insert.execute(position);
			} else {
				Gui.this.replace.execute(position);
			}
		}

		public void cut() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText());
			Gui.this.cut.execute(position);
		}

		public void paste() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText());
			Gui.this.paste.execute(position);
		}

		public void copy() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText());
			Gui.this.copy.execute(position);
		}
	}
	
	public Gui(String name, Copy copy, Paste paste, Cut cut, Insert insert, Delete delete, Replace replace) {
		logger = new Log(this);
		this.copy = copy;
		this.paste = paste;
		this.cut = cut;
		this.insert = insert;
		this.delete = delete;
		this.replace = replace;

		fname = "";
		chg = false;
		setLayout(new BorderLayout());

		jta = new TextArea();
		jta.setFont(new Font("Arial", Font.PLAIN , 16) );
		jta.addKeyListener(this);

		jscroll = new JScrollPane(jta);
		add(jscroll, BorderLayout.CENTER );

		initIcons();

		initMenu();
		setJMenuBar(mbar);

		initToolbar();
		add(jtbar, BorderLayout.NORTH);

		setSize(800,600);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initIcons() {
		iNew  = new ImageIcon("images/new.gif");
		iOpen  = new ImageIcon("images/open.gif");
		iSave  = new ImageIcon("images/save.gif");
		iCut  = new ImageIcon("images/cut.gif");
		iCopy  = new ImageIcon("images/copy.gif");
		iPaste  = new ImageIcon("images/paste.gif");
	}

	private void initMenu() {
		mbar = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");

		file.setMnemonic('F');
		edit.setMnemonic('E');

		fnew = new JMenuItem("New", iNew);
		fopen = new JMenuItem("Open", iOpen);
		fsave = new JMenuItem("Save",iSave);
		fexit = new JMenuItem("Exit");
		ecut=new JMenuItem("cut", iCut);
		ecopy=new JMenuItem("copy", iCopy);
		epaste=new JMenuItem("Paste", iPaste);
		eselall=new JMenuItem("Selectall");

		file.add(fnew);
		file.add(fopen);
		file.add(fsave);
		file.addSeparator();
		file.add(fexit);
		edit.add(ecut);
		edit.add(ecopy);
		edit.add(epaste);
		edit.addSeparator();
		edit.add(eselall);
		mbar.add(file);
		mbar.add(edit);

		fnew.addActionListener(this);
		fopen.addActionListener(this);
		fsave.addActionListener(this);
		fexit.addActionListener(this);
		ecut.addActionListener(this);
		ecopy.addActionListener(this);
		epaste.addActionListener(this);
		eselall.addActionListener(this);

		KeyStroke k;
		k = KeyStroke.getKeyStroke('N', java.awt.Event.CTRL_MASK);
		fnew.setAccelerator(k);
		k = KeyStroke.getKeyStroke('O', java.awt.Event.CTRL_MASK);
		fopen.setAccelerator(k);
		k = KeyStroke.getKeyStroke('S', java.awt.Event.CTRL_MASK);
		fsave.setAccelerator(k);
		k = KeyStroke.getKeyStroke('X', java.awt.Event.CTRL_MASK);
		ecut.setAccelerator(k);
		k = KeyStroke.getKeyStroke('C', java.awt.Event.CTRL_MASK);
		ecopy.setAccelerator(k);
		k = KeyStroke.getKeyStroke('V', java.awt.Event.CTRL_MASK);
		epaste.setAccelerator(k);
		k = KeyStroke.getKeyStroke('A', java.awt.Event.CTRL_MASK);
		eselall.setAccelerator(k);
	}

	private void initToolbar() {
		bttnnew = new JButton(iNew);
		bttnopen = new JButton(iOpen);
		bttnsave = new JButton(iSave);
		bttncut  = new JButton(iCut);
		bttncopy = new JButton(iCopy);
		bttnpaste = new JButton(iPaste);
		jtbar = new JToolBar();

		bttnnew.addActionListener(this);
		bttnopen.addActionListener(this);
		bttnsave.addActionListener(this);
		bttncut.addActionListener(this);
		bttncopy.addActionListener(this);
		bttnpaste.addActionListener(this);

		jtbar.add(bttnnew);
		jtbar.add(bttnopen);
		jtbar.add(bttnsave);
		jtbar.add(bttncut);
		jtbar.add(bttncopy);
		jtbar.add(bttnpaste);
		jtbar.addSeparator();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_DELETE && jta.getCaretPosition() != jta.getText().length()) {
			// The position indices are thus before action is called
			// We have to add -1 to be in the current position
			// So no need for delete via DELETE, because the right position is thus before action
			((TextArea) jta).delete(jta.getCaretPosition(), 1);
		}
		chg = true;
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_BACK_SPACE && jta.getCaretPosition() != 1) {
			((TextArea) jta).delete(jta.getCaretPosition(), 1);
		}
		chg = true;
	}
	
	public void keyTyped(KeyEvent e) {
		chg = true;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(bttncut) || e.getSource().equals(ecut) ) {
			jta.cut();
		}
		else if(e.getSource().equals(bttncopy) || e.getSource().equals(ecopy)) {
			jta.copy();
		}
		else if(e.getSource().equals(bttnpaste) || e.getSource().equals(epaste)) {
			jta.paste();
		}
		else if(e.getSource().equals(eselall)) {
			jta.selectAll();
		}
		else if(e.getSource().equals(fnew)) {
			fname = "";
			chg = false;
			jta.setText("");
		}
		else if(e.getSource().equals(fopen)) {
			open();
		}
		else if(e.getSource().equals(fsave)) {
			save();
		}
		else if(e.getSource().equals(fexit)) {
			exit();
		}
	}

	public void exit() {
		if(chg == true) {
			int res;
			res = JOptionPane.showConfirmDialog(this, "Do You Want to Save Changes", "File Exit", JOptionPane.YES_NO_CANCEL_OPTION );
			if(res == JOptionPane.YES_OPTION) {
				save();
			}
			else if(res == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.exit(0);
	}

	public void open() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int res = jfc.showOpenDialog(this);
		if(res == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				String data;
				jta.setText("");

				while( (data =br.readLine()) != null) {
					data = data + "n";
					jta.append(data);
				}
				fname = f.getAbsolutePath();
				br.close();
				fr.close();

			} catch(IOException e) {
				JOptionPane.showMessageDialog(
					this , e.getMessage() , "File Open Error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void save() {
		if(fname.equals("")) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int res = jfc.showSaveDialog(this);
			if(res == JFileChooser.APPROVE_OPTION) {
				File f = jfc.getSelectedFile();
				fname = f.getAbsolutePath();
				write();
			}
		} else write();
	}

	public void write() {
		try {
			FileWriter fw = new FileWriter(fname);

			fw.write(jta.getText());
			fw.flush();
			fw.close();
			chg = false;
		} catch(IOException e) {
			JOptionPane.showMessageDialog(
					this , e.getMessage() , "File Save Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void update(Observable observable, Object objectConcerne) {
		if (observable.toString() != jta.getText()) {
			jta.setText(observable.toString());
		}
	}
}
