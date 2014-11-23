import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Gui extends JFrame implements ActionListener , KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
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

	private class textArea extends JTextArea {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
				
		public void cut() {
			Selection position = new Selection(jta.getSelectionStart(), jta.getSelectionEnd(), "");
			System.out.println("Cut !");
			user.cut.execute(position);
			super.cut();
		}
		
		public void paste() {
			Selection position = new Selection(jta.getSelectionStart(), jta.getSelectionEnd(), jta.getSelectedText());
			System.out.println("Paste !");
			user.paste.execute(position);
			super.paste();
		}
		
		public void copy() {
			Selection position = new Selection(jta.getSelectionStart(), jta.getSelectionEnd(), jta.getSelectedText());
			System.out.println("Copy !");
			user.copy.execute(position);
			super.copy();
		}

	}
	
	public Gui(String name, User user)
	{
		this.user = user;
		fname = "";
		chg = false;
		setLayout(new BorderLayout());

		jta = new textArea();
		jta.setFont(new Font("Comic Sans MS", Font.PLAIN , 24) );
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

	private void initIcons()
	{
		iNew  = new ImageIcon("images/new.gif");
		iOpen  = new ImageIcon("images/open.gif");
		iSave  = new ImageIcon("images/save.gif");
		iCut  = new ImageIcon("images/cut.gif");
		iCopy  = new ImageIcon("images/copy.gif");
		iPaste  = new ImageIcon("images/paste.gif");
	}

	private void initMenu()
	{
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

		KeyStroke k ;

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

	private void initToolbar()
	{
		bttnnew = new JButton(iNew);
		bttnopen = new JButton(iOpen);
		bttnsave = new JButton(iSave);
		bttncut  = new JButton(iCut);
		bttncopy = new JButton(iCopy);
		bttnpaste = new JButton(iPaste);

		bttnnew.addActionListener(this);
		bttnopen.addActionListener(this);
		bttnsave.addActionListener(this);
		bttncut.addActionListener(this);
		bttncopy.addActionListener(this);
		bttnpaste.addActionListener(this);

		jtbar = new JToolBar();
		jtbar.add(bttnnew);
		jtbar.add(bttnopen);
		jtbar.add(bttnsave);
		jtbar.addSeparator();
		jtbar.add(bttncut);
		jtbar.add(bttncopy);
		jtbar.add(bttnpaste);
	}
	
	public void keyPressed(KeyEvent e) {
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
		int code = e.getKeyCode();
		String keytext = KeyEvent.getKeyText(code);
		System.out.println("Key typed > " + code);
		
		Selection position = new Selection(jta.getSelectionStart(), jta.getSelectionEnd(), Character.toString(e.getKeyChar()));
					
		if (keytext == "Supprimer") {
			position.setContent("");
			position.setLength(position.getLength() + 1);
			user.getCore().delete(position);
		}
		else if (keytext == "Retour arrière") {
			position.setContent("");
			position.setLength(position.getLength() + 1);
			user.getCore().delete(position);
		}
		else if (keytext == "Tab") {
			position.setContent("\t");
			user.getCore().write(position);
		}
		else if (keytext == "Entrée") {
			position.setContent("\n");
		}
		user.getCore().write(position);
		chg = true;
	}
	
	public void exit(){
		System.exit(0);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(bttncut) || e.getSource().equals(ecut) )
		{
			jta.cut();
		}
		else if(e.getSource().equals(bttncopy) || e.getSource().equals(ecopy))
		{
			jta.copy();
		}
		else if(e.getSource().equals(bttnpaste) || e.getSource().equals(epaste))
		{
			jta.paste();
		}
		else if(e.getSource().equals(eselall))
		{
			jta.selectAll();
		}
		else if(e.getSource().equals(fnew))
		{
			fname = "";
			chg = false;
			jta.setText("");
		}
		else if(e.getSource().equals(fopen))
		{
			fileopen();
		}
		else if(e.getSource().equals(fsave))
		{
			filesave();
		}
		else if(e.getSource().equals(fexit))
		{
			fileexit();
		}
	}

	private void fileexit()
	{
		if(chg == true)
		{
			int res;
			res = JOptionPane.showConfirmDialog(this, "Do You Want to Save Changes", "File Exit", JOptionPane.YES_NO_CANCEL_OPTION );
			if(res == JOptionPane.YES_OPTION)
			{
				filesave();
			}
			else if(res == JOptionPane.CANCEL_OPTION)
			{
				return;
			}
		}
		System.exit(0);
	}

	private void fileopen()
	{

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int res = jfc.showOpenDialog(this);
		if(res == JFileChooser.APPROVE_OPTION)
		{
			File f = jfc.getSelectedFile();
			try
			{
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);

				String data;
				jta.setText("");

				while( (data =br.readLine()) != null)
				{
					data = data + "n";
					jta.append(data);
				}
				fname = f.getAbsolutePath();
				br.close();
				fr.close();

			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog
				(
						this , e.getMessage() , "File Open Error",
						JOptionPane.ERROR_MESSAGE
						);
			}
		}
	}

	private void filesave()
	{
		if(fname.equals(""))
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int res = jfc.showSaveDialog(this);
			if(res == JFileChooser.APPROVE_OPTION)
			{
				File f = jfc.getSelectedFile();
				fname = f.getAbsolutePath();
				filewrite();
			}
		}
		else
			filewrite();
	}

	private void filewrite()
	{
		try
		{
			FileWriter fw = new FileWriter(fname);

			fw.write(jta.getText());
			fw.flush();
			fw.close();
			chg = false;
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog
			(
					this , e.getMessage() , "File Save Error",
					JOptionPane.ERROR_MESSAGE
					);
		}

	}

}
