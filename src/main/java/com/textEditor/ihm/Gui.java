package com.textEditor.ihm;

import javax.swing.*;

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

	private JTextArea jta;
	private JScrollPane jscroll;

	private JToolBar jtbar;
	private JButton bttnopen, bttnnew, bttnsave, bttncut, bttncopy, bttnpaste,
			bttnrec, bttnstop, bttnplay;

	private JMenuBar mbar;
	private JMenu file, edit, macro;
	private JMenuItem fnew, fexit, fopen, fsave, mrec, mplay, ecut, ecopy,
			epaste, eselall, mrecstop;

	private ImageIcon iNew, iOpen, iSave, iCut, iCopy, iPaste, iRecord, iPlay,
			iStop;

	private String fname;
	private boolean chg;
	private Log logger;

	private class TextArea extends JTextArea {

		private static final long serialVersionUID = 1L;

		public void insert(String str, int pos) {
			Selection position = new Selection(pos, pos, str);
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
			// In all cases, JTextArea call this method whatever the currently
			// operation
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
			Stop stop) {
		this.copy = copy;
		this.paste = paste;
		this.cut = cut;
		this.insert = insert;
		this.delete = delete;
		this.replace = replace;
		this.record = record;
		this.play = play;
		this.stop = stop;
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
		mrecstop.setMnemonic('R');
		mplay = new JMenuItem("Play", iPlay);
		mplay.setMnemonic('P');

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
		bttncut = new JButton(iCut);
		bttncopy = new JButton(iCopy);
		bttnpaste = new JButton(iPaste);
		bttnrec = new JButton(iRecord);
		bttnstop = new JButton(iStop);
		bttnplay = new JButton(iPlay);

		bttnnew.addActionListener(this);
		bttnopen.addActionListener(this);
		bttnsave.addActionListener(this);
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
		jtbar.add(bttncut);
		jtbar.add(bttncopy);
		jtbar.add(bttnpaste);
		jtbar.addSeparator();
		jtbar.add(bttnrec);
		jtbar.add(bttnstop);
		jtbar.add(bttnplay);
		logger.info("Done.");
	}

	public void keyPressed(KeyEvent e) {
		chg = true;
	}

	public void keyReleased(KeyEvent e) {
		// ATTENTION : The position indices are thus after action is called
		int keyCode = e.getKeyCode();
		int start = jta.getCaretPosition();
		int end = start + 1;
		if (jta.getSelectedText() != null) {
			start = jta.getSelectionStart();
			end = jta.getSelectionEnd();
		}
		if (keyCode == KeyEvent.VK_BACK_SPACE) {
			((TextArea) jta).delete(start, end);
		}
		if (keyCode == KeyEvent.VK_DELETE) {
			((TextArea) jta).delete(start, end);
			// Bug while using delete. Caret return at the end of the text
			// ???
			jta.setCaretPosition(start);
		}
		chg = true;

	}

	public void keyTyped(KeyEvent e) {
		chg = true;
	}

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
			Selection position = new Selection(0, jta.getText().length(), "");
			Gui.this.delete.execute(position);
		} else if (e.getSource().equals(bttnopen) || e.getSource().equals(fopen)) {
			open();
		} else if (e.getSource().equals(bttnsave) || e.getSource().equals(fsave)) {
			save();
		} else if (e.getSource().equals(fexit)) {
			exit();
		}
	}

	public void record() {
		bttnrec.setEnabled(false);
		mrec.setEnabled(false);
		bttnstop.setEnabled(true);
		mrecstop.setEnabled(true);
		this.record.execute();
		logger.info("Macro recording...");
	}

	public void stop() {
		bttnrec.setEnabled(true);
		mrec.setEnabled(true);
		bttnstop.setEnabled(false);
		mrecstop.setEnabled(false);
		this.stop.execute();
		logger.info("Macro stopped.");
	}

	public void play() {
		this.play.execute();
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
		if (obj.toString() != jta.getText()) {
			jta.setText(observable.toString());
		}
	}
}
