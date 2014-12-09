package com.textEditor.ihm;

import javax.swing.*;

import com.textEditor.commands.*;
import com.textEditor.core.*;
import com.textEditor.log.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 * IHM class
 * 
 * @author blacknight
 *
 */
public class Gui extends JFrame implements ActionListener, GuiInterface, KeyListener, Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fname;
	private Log logger;
	private int selectionStart;
	private int selectionEnd;
	private String selectionText;
	private CommandInvoker invoker;
	protected TextArea jta;
	protected boolean chg;
	protected JScrollPane jscroll;

	private JToolBar jtbar;
	protected JButton bttnopen, bttnnew, bttnsave, bttncut, bttncopy,
			bttnpaste, bttnrec, bttnstop, bttnplay, bttnredo, bttnundo;

	private JMenuBar mbar;
	private JMenu file, edit, macro;
	protected JMenuItem fnew, fexit, fopen, fsave, mrec, mplay, ecut, ecopy,
			epaste, eselall, mrecstop, eundo, eredo;

	private ImageIcon iNew, iOpen, iSave, iCut, iCopy, iPaste,
			iRecord, iPlay, iStop, iRedo, iUndo;
	
	
	/**
	 * Nested class that manage the text and text operations, updates itself when notified
	 * @author blacknight
	 *
	 */
	public class TextArea extends JTextArea implements Observer {

		private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see javax.swing.JTextArea#insert(java.lang.String, int)
		 */
		public void insert(String str, int pos) {
			Selection position = new Selection(pos, pos, str, "");
			invoker.invoke(new Insert(), position);
		}

		/* (non-Javadoc)
		 * @see javax.swing.JTextArea#append(java.lang.String)
		 */
		public void append(String str) {
			insert(str, getText().length());
		}

		/* (non-Javadoc)
		 * @see javax.swing.JTextArea#replaceRange(java.lang.String, int, int)
		 */
		public void replaceRange(String str, int pos, int end) {
			replaceSelection(str);
		}
		
		/**
		 * @param start
		 * @param end
		 */
		public void delete(int start, int end) {
			Selection position = new Selection(start, end, "", selectionText);
			invoker.invoke(new Delete(), position);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.JTextComponent#replaceSelection(java.lang.String)
		 */
		public void replaceSelection(String str) {
			// In all cases, JTextArea call this method whatever the currently
			// operation
			// We have to look at positions to know about the current operation.
			// It can be either a simple char append or a replacement.
			if (getSelectionStart() == getSelectionEnd()) {
				insert(str, getSelectionStart());
			} else if (str == "") {
				delete(getSelectionStart(), getSelectionEnd());
			} else {
				Selection position = new Selection(getSelectionStart(), getSelectionEnd(), str, getSelectedText());
				invoker.invoke(new Replace(), position);
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.JTextComponent#cut()
		 */
		public void cut() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(),  getSelectedText());
			invoker.invoke(new Cut(), position);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.JTextComponent#paste()
		 */
		public void paste() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(), getSelectedText());
			invoker.invoke(new Paste(), position);
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.JTextComponent#copy()
		 */
		public void copy() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(), getSelectedText());
			invoker.invoke(new Copy(), position);
		}
		
		/* (non-Javadoc)
		 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
		 */
		public void update(Observable observable, Object obj) {
			if (((Core) observable).getBuffer() != getText()) {
				setText(((Core) observable).getBuffer());
			}
			setCaretPosition(((Core) observable).getCurrentPosition());
		}
	}
	
	/**
	 *  Returns an ImageIcon, or null if the path was invalid.
	 *  
	 */
	private ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	/**
	 * Instantiate icons
	 */
	private void initIcons() {

		iNew = createImageIcon("/images/new.gif", "New file");
		iOpen = createImageIcon("/images/open.gif", "Open a file");
		iSave = createImageIcon("/images/save.gif", "Save");
		iCut = createImageIcon("/images/cut.gif", "Cut");
		iCopy = createImageIcon("/images/copy.gif", "Copy");
		iPaste = createImageIcon("/images/paste.gif", "Paste");
		iRecord = createImageIcon("/images/record.gif", "Record");
		iStop = createImageIcon("/images/stop.gif", "Stop");
		iPlay = createImageIcon("/images/play.gif", "Play");
		iRedo = createImageIcon("/images/redo.gif", "Redo");
		iUndo = createImageIcon("/images/undo.gif", "Undo");
	}

	/**
	 * Instanciate menus and links menus with icons and keylisteners
	 */
	private void initMenu() {
		mbar = new JMenuBar();

		file = new JMenu("File");
		file.setMnemonic('F');
		edit = new JMenu("Edit");
		edit.setMnemonic('E');
		macro = new JMenu("Macro");
		macro.setMnemonic('M');

		fnew = new JMenuItem("New", iNew);
		fnew.setMnemonic('N');
		
		fopen = new JMenuItem("Open", iOpen);
		fopen.setMnemonic('O');
		
		fsave = new JMenuItem("Save", iSave);
		fsave.setMnemonic('S');
		
		fexit = new JMenuItem("Exit");
		fexit.setMnemonic('E');

		eundo = new JMenuItem("Undo", iUndo);
		eundo.setEnabled(false);
		eundo.setMnemonic('U');
		
		eredo = new JMenuItem("Redo", iRedo);
		eredo.setEnabled(false);
		eredo.setMnemonic('D');
		
		ecut = new JMenuItem("Cut", iCut);
		ecut.setMnemonic('C');
		
		ecopy = new JMenuItem("Copy", iCopy);
		ecopy.setMnemonic('O');
		
		epaste = new JMenuItem("Paste", iPaste);
		epaste.setMnemonic('P');
		
		eselall = new JMenuItem("Selectall");
		eselall.setMnemonic('A');
		
		mrec = new JMenuItem("Record", iRecord);
		mrec.setMnemonic('R');
		mrecstop = new JMenuItem("Stop", iStop);
		mrecstop.setEnabled(false);
		mrecstop.setMnemonic('S');
		mplay = new JMenuItem("Play", iPlay);
		mplay.setMnemonic('P');

		file.add(fnew);
		file.add(fopen);
		file.add(fsave);
		file.addSeparator();
		file.add(fexit);
		
		edit.add(eundo);
		edit.add(eredo);
		edit.addSeparator();
		edit.add(ecut);
		edit.add(ecopy);
		edit.add(epaste);
		edit.addSeparator();
		edit.add(eselall);
		
		macro.add(mrec);
		macro.add(mrecstop);
		macro.add(mplay);
		
		mbar.add(file);
		mbar.add(edit);
		mbar.add(macro);

		fnew.addActionListener(this);
		fopen.addActionListener(this);
		fsave.addActionListener(this);
		fexit.addActionListener(this);
		eundo.addActionListener(this);
		eredo.addActionListener(this);
		ecut.addActionListener(this);
		ecopy.addActionListener(this);
		epaste.addActionListener(this);
		eselall.addActionListener(this);
		mrec.addActionListener(this);
		mrecstop.addActionListener(this);
		mplay.addActionListener(this);

		KeyStroke k;
		k = KeyStroke.getKeyStroke('N', java.awt.Event.CTRL_MASK);
		fnew.setAccelerator(k);
		k = KeyStroke.getKeyStroke('O', java.awt.Event.CTRL_MASK);
		fopen.setAccelerator(k);
		k = KeyStroke.getKeyStroke('S', java.awt.Event.CTRL_MASK);
		fsave.setAccelerator(k);
		k = KeyStroke.getKeyStroke('Z', java.awt.Event.CTRL_MASK);
		eundo.setAccelerator(k);
		k = KeyStroke.getKeyStroke('Z', java.awt.Event.SHIFT_MASK);
		eredo.setAccelerator(k);
		k = KeyStroke.getKeyStroke('X', java.awt.Event.CTRL_MASK);
		ecut.setAccelerator(k);
		k = KeyStroke.getKeyStroke('C', java.awt.Event.CTRL_MASK);
		ecopy.setAccelerator(k);
		k = KeyStroke.getKeyStroke('V', java.awt.Event.CTRL_MASK);
		epaste.setAccelerator(k);
		k = KeyStroke.getKeyStroke('A', java.awt.Event.CTRL_MASK);
		eselall.setAccelerator(k);
		k = KeyStroke.getKeyStroke('R', java.awt.Event.CTRL_MASK);
		mrec.setAccelerator(k);
		k = KeyStroke.getKeyStroke('S', java.awt.Event.CTRL_MASK);
		mrecstop.setAccelerator(k);
		k = KeyStroke.getKeyStroke('P', java.awt.Event.CTRL_MASK);
		mplay.setAccelerator(k);
	}

	/**
	 * Instanciate the toolbar and links with icons and actionlisteners
	 */
	private void initToolbar() {
		jtbar = new JToolBar();
		jtbar.setFloatable(false);

		bttnnew = new JButton(iNew);
		bttnopen = new JButton(iOpen);
		bttnsave = new JButton(iSave);
		bttnundo = new JButton(iUndo);
		bttnredo = new JButton(iRedo);
		bttncut = new JButton(iCut);
		bttncopy = new JButton(iCopy);
		bttnpaste = new JButton(iPaste);
		bttnrec = new JButton(iRecord);
		bttnstop = new JButton(iStop);
		bttnplay = new JButton(iPlay);

		bttnnew.addActionListener(this);
		bttnopen.addActionListener(this);
		bttnsave.addActionListener(this);
		bttnundo.addActionListener(this);
		bttnundo.setEnabled(false);
		bttnredo.addActionListener(this);
		bttnredo.setEnabled(false);
		bttncut.addActionListener(this);
		bttncopy.addActionListener(this);
		bttnpaste.addActionListener(this);
		bttnrec.addActionListener(this);
		bttnstop.addActionListener(this);
		bttnstop.setEnabled(false);
		bttnplay.addActionListener(this);

		jtbar.add(bttnnew);
		jtbar.add(bttnopen);
		jtbar.add(bttnsave);
		jtbar.addSeparator();
		jtbar.add(bttnundo);
		jtbar.add(bttnredo);
		jtbar.addSeparator();
		jtbar.add(bttncut);
		jtbar.add(bttncopy);
		jtbar.add(bttnpaste);
		jtbar.addSeparator();
		jtbar.add(bttnrec);
		jtbar.add(bttnstop);
		jtbar.add(bttnplay);
	}
	
	public Gui(String name) {
		fname = name;
		logger = new Log(this);
		chg = false;
		setLayout(new BorderLayout());

		jta = new TextArea();
		jta.setFont(new Font("Arial", Font.PLAIN, 16));
		jta.addKeyListener(this);

		jscroll = new JScrollPane(jta);
		add(jscroll, BorderLayout.CENTER);

		initIcons();
		initMenu();
		setJMenuBar(mbar);
		initToolbar();
		add(jtbar, BorderLayout.NORTH);

		setSize(800, 600);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		logger.info("Done.");
	}

	/**
	 * Return the text area to allow to be add as an observer
	 * @return
	 */
	public TextArea getTextArea() {
		return jta;
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#setCommandInvoker(com.textEditor.commands.CommandInvoker)
	 */
	public void setCommandInvoker(CommandInvoker invoker) {
		this.invoker = invoker;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		selectionStart = jta.getCaret().getDot();
		selectionEnd = jta.getCaret().getMark();
		// First we keep the positions of the cursor and the selection if any
		if (selectionStart > selectionEnd) {
			int backup = selectionStart;
			selectionStart = selectionEnd;
			selectionEnd = backup;
		}
		selectionText = jta.getText().substring(selectionStart, selectionEnd);
		chg = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// Then we can use the previous saved position for DELETE and BACKSPACE
		// ATTENTION : The position indices are thus after action is called
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_DELETE) {
			if (selectionStart == selectionEnd) {
				selectionEnd++;
			}
			((TextArea) jta).delete(selectionStart, selectionEnd);
		} else if (keyCode == KeyEvent.VK_BACK_SPACE) {
			if (selectionStart == selectionEnd) {
				selectionStart--;
			}
			((TextArea) jta).delete(selectionStart, selectionEnd);
		}
		chg = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
		chg = true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(bttncut) || e.getSource().equals(ecut)) {
			jta.cut();
		} else if (e.getSource().equals(bttncopy) || e.getSource().equals(ecopy)) {
			jta.copy();
		} else if (e.getSource().equals(bttnpaste) || e.getSource().equals(epaste)) {
			jta.paste();
		} else if (e.getSource().equals(bttnrec) || e.getSource().equals(mrec)) {
			record();
		} else if (e.getSource().equals(bttnstop) || e.getSource().equals(mrecstop)) {
			stop();
		} else if (e.getSource().equals(bttnplay) || e.getSource().equals(mplay)) {
			play();
		} else if (e.getSource().equals(eselall)) {
			jta.selectAll();
		} else if (e.getSource().equals(bttnnew) || e.getSource().equals(fnew)) {
			fname = "";
			chg = false;
			Selection position = new Selection(0, jta.getText().length(), "", jta.getText());
			invoker.invoke(new Reset(), position);
		} else if (e.getSource().equals(bttnopen) || e.getSource().equals(fopen)) {
			open();
		} else if (e.getSource().equals(bttnundo) || e.getSource().equals(eundo)) {
			undo();
		} else if (e.getSource().equals(bttnredo) || e.getSource().equals(eredo)) {
			redo();
		} else if (e.getSource().equals(bttnsave) || e.getSource().equals(fsave)) {
			save();
		} else if (e.getSource().equals(fexit)) {
			exit();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#undo()
	 */
	public void undo() {
		invoker.invoke(new Undo());
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#redo()
	 */
	public void redo() {
		invoker.invoke(new Redo());
	}
	
	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#record()
	 */
	public void record() {
		invoker.invoke(new Record());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#stop()
	 */
	public void stop() {
		invoker.invoke(new Stop());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#play()
	 */
	public void play() {
		invoker.invoke(new Play());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#exit()
	 */
	public void exit() {
		if (chg == true) {
			int res;
			res = JOptionPane.showConfirmDialog(this,
					"Do You Want to Save Changes", "File Exit",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				save();
			} else if (res == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		logger.info("Done.");
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#open()
	 */
	public void open() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int res = jfc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			try {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				String data;
				jta.setText("");

				while ((data = br.readLine()) != null) {
					data = data + "n";
					jta.append(data);
				}
				fname = f.getAbsolutePath();
				logger.info(fname.toString());
				br.close();
				fr.close();

			} catch (IOException e) {
				logger.info("File Open Error => " + JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"File Open Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#save()
	 */
	public void save() {
		if (fname.equals("")) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int res = jfc.showSaveDialog(this);
			if (res == JFileChooser.APPROVE_OPTION) {
				File f = jfc.getSelectedFile();
				fname = f.getAbsolutePath();
				write();
			}
		} else
			write();
		logger.info(fname.toString());
	}

	/* (non-Javadoc)
	 * @see com.textEditor.ihm.GuiInterface#write()
	 */
	public void write() {
		try {
			FileWriter fw = new FileWriter(fname);

			fw.write(jta.getText());
			fw.flush();
			fw.close();
			chg = false;
		} catch (IOException e) {
			logger.info("File Save Error => " + JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"File Save Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		bttnundo.setEnabled(((Core) observable).isUndo());
		bttnredo.setEnabled(((Core) observable).isRedo());
		eundo.setEnabled(((Core) observable).isUndo());
		eredo.setEnabled(((Core) observable).isRedo());

		bttnrec.setEnabled(!((Core) observable).isRecording());
		bttnplay.setEnabled(!((Core) observable).isPlaying());
		bttnstop.setEnabled(((Core) observable).isRecording());
		mrec.setEnabled(!((Core) observable).isRecording());
		mplay.setEnabled(!((Core) observable).isPlaying());
		mrecstop.setEnabled(((Core) observable).isRecording());
	}

}
