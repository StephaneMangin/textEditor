package com.textEditor.ihm;

import javax.swing.*;
import javax.swing.undo.UndoManager;

import com.textEditor.commands.*;
import com.textEditor.core.*;
import com.textEditor.log.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class Gui extends JFrame implements GuiInterface, Observer, ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Cut cut;
	private Copy copy;
	private Paste paste;
	private Insert insert;
	private Delete delete;
	private Replace replace;
	private Record record;
	private Play play;
	private Stop stop;
	private Undo undo;
	private Redo redo;

	private JTextArea jta;
	protected UndoManager undoManager = new UndoManager();
	private JScrollPane jscroll;

	private JToolBar jtbar;
	private JButton bttnopen, bttnnew, bttnsave, bttncut, bttncopy, bttnpaste,
			bttnrec, bttnstop, bttnplay, bttnredo, bttnundo;

	private JMenuBar mbar;
	private JMenu file, edit, macro;
	private JMenuItem fnew, fexit, fopen, fsave, mrec, mplay, ecut, ecopy,
			epaste, eselall, mrecstop, eundo, eredo;

	private ImageIcon iNew, iOpen, iSave, iCut, iCopy, iPaste, iRecord, iPlay, iStop, iRedo, iUndo;

	private String fname;
	private boolean chg;
	private Log logger;
	private int selectionStart;
	private int selectionEnd;

	@SuppressWarnings("serial")
	private class TextArea extends JTextArea {

		@Override
		public void insert(String str, int pos) {
			Selection position = new Selection(pos, pos, str, "");
			Gui.this.insert.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}

		@Override
		public void append(String str) {
			int pos = getText().length() - 1;
			Selection position = new Selection(pos, pos, str, "");
			Gui.this.insert.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}

		@Override
		public void replaceRange(String str, int pos, int end) {
			if (str == "") {
				delete(pos, end);
			} else {
				replaceSelection(str);
			}
		}
		
		public void delete(int start, int end) {
			Selection position = new Selection(start, end, "", getSelectedText());
			Gui.this.delete.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}

		@Override
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
				Gui.this.replace.execute(position);
				bttnundo.setEnabled(true);
				eundo.setEnabled(true);
			}
		}

		@Override
		public void cut() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(),  getSelectedText());
			Gui.this.cut.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}

		@Override
		public void paste() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(), getSelectedText());
			Gui.this.paste.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}

		@Override
		public void copy() {
			Selection position = new Selection(getSelectionStart(), getSelectionEnd(), getSelectedText(), getSelectedText());
			Gui.this.copy.execute(position);
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		}
	}

	public Gui(String name) {
		logger = new Log(this);
		fname = "";
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
		add(jtbar, BorderLayout.WEST);

		setSize(800, 600);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		logger.info("Done.");
	}

	public void setCommands(
			Copy copy,
			Paste paste,
			Cut cut,
			Insert insert,
			Delete delete,
			Replace replace,
			Record record,
			Play play,
			Stop stop,
			Undo undo,
			Redo redo) {
		this.copy = copy;
		this.paste = paste;
		this.cut = cut;
		this.insert = insert;
		this.delete = delete;
		this.replace = replace;
		this.record = record;
		this.play = play;
		this.stop = stop;
		this.undo = undo;
		this.redo = redo;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	private ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
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
		logger.info("Done.");
	}

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
		logger.info("Done.");
	}

	private void initToolbar() {
		jtbar = new JToolBar(1);

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
		logger.info("Done.");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// First we keep the positions of the cursor and the selection if any
		selectionStart = jta.getCaret().getDot();
		selectionEnd = jta.getCaret().getMark();
		// Then we can use the previous saved position for DELETE and BACKSPACE
		// ATTENTION : The position indices are thus after action is called
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_DELETE) {
			((TextArea) jta).delete(selectionStart, selectionEnd + 1);
			// Bug while using delete. Caret return at the end of the text
			jta.setCaretPosition(selectionStart);
		}
		chg = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_BACK_SPACE) {
			((TextArea) jta).delete(selectionStart - 1, selectionEnd);
			// Bug while using delete. Caret return at the end of the text
			jta.setCaretPosition(selectionStart - 1);
		}
		chg = true;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		chg = true;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(bttncut) || e.getSource().equals(ecut)) {
			jta.cut();
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		} else if (e.getSource().equals(bttncopy) || e.getSource().equals(ecopy)) {
			jta.copy();
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		} else if (e.getSource().equals(bttnpaste) || e.getSource().equals(epaste)) {
			jta.paste();
			bttnundo.setEnabled(true);
			eundo.setEnabled(true);
		} else if (e.getSource().equals(bttnrec) || e.getSource().equals(mrec)) {
			record();
			bttnrec.setEnabled(false);
			mrec.setEnabled(false);
			bttnstop.setEnabled(true);
			mrecstop.setEnabled(true);
		} else if (e.getSource().equals(bttnstop) || e.getSource().equals(mrecstop)) {
			bttnrec.setEnabled(true);
			mrec.setEnabled(true);
			bttnstop.setEnabled(false);
			mrecstop.setEnabled(false);
			stop();
		} else if (e.getSource().equals(bttnplay) || e.getSource().equals(mplay)) {
			play();
		} else if (e.getSource().equals(eselall)) {
			jta.selectAll();
		} else if (e.getSource().equals(bttnnew) || e.getSource().equals(fnew)) {
			fname = "";
			chg = false;
			Selection position = new Selection(0, jta.getText().length(), "", jta.getText());
			Gui.this.delete.execute(position);
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
	
	public void undo() {
		bttnredo.setEnabled(true);
		eredo.setEnabled(true);
		Selection position = new Selection();
		Gui.this.undo.execute(position);
	}
	
	public void redo() {
		bttnundo.setEnabled(true);
		eundo.setEnabled(true);
		Selection position = new Selection();
		Gui.this.redo.execute(position);
	}
	
	public void record() {
		this.record.execute(new Selection());
		logger.info("Macro recording...");
	}

	public void stop() {
		this.stop.execute(new Selection());
		logger.info("Macro stopped.");
	}

	public void play() {
		this.play.execute(new Selection());
		logger.info("Macro playing...");
	}

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

	public void update(Observable observable, Object obj) {
		if (((Core) observable).getBuffer() != jta.getText()) {
			jta.setText(observable.toString());
		}
		bttnundo.setEnabled(((Core) observable).isUndo());
		bttnredo.setEnabled(((Core) observable).isRedo());
		eundo.setEnabled(((Core) observable).isUndo());
		eredo.setEnabled(((Core) observable).isRedo());
	}
}
