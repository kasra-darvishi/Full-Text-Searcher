/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsmidtermproject;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 *
 * @author kasra
 */
public class GUI extends JFrame{
    
    String folderAdress = null;
    int kindOfTree = 1;
    static Trie trie = null;
    static TST tst = null;
    static BST bst = null;
    static ArrayList<String> stopWords;
    static ArrayList<String> filePaths;
    static ArrayList<String> recentTasks;
    static int positionInRecentTasks;
    static boolean firstArrowUpPress = true;
    JTextArea ta2;

    public static void main(String[] args) throws IOException {
	
	stopWords = new StopWords().getStopWords();
	filePaths = new ArrayList<String>();
	recentTasks = new ArrayList<String>();
	
	//related to GUI
	try {
	    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
	PipedInputStream outPipe = new PipedInputStream();
	System.setOut(new PrintStream(new PipedOutputStream(outPipe), true));
	GUI gui = new GUI(outPipe);
	gui.setVisible(true);
	gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//

    }

    public GUI(PipedInputStream outPipe) throws HeadlessException {
	
	super("Inverted Index");
	setLayout(null);
	JPanel panel = new JPanel(null);
	
	Font font1 = new Font( "", Font.PLAIN, 35);
	JLabel label1 = new JLabel( "Enter folder adress here " );
	label1.setFont(font1);
	label1.setBounds( 20, 0, 450, 80);
	panel.add(label1);
	
	JTextArea ta = new JTextArea(1, 100);
	font1 = new Font( "", Font.PLAIN, 40);
	ta.setFont(font1);
	ta.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	ta.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
	    }
	    @Override
	    public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
		    folderAdress = ta.getText();
		    setFilesFromFolder();
		}
	    }
	    @Override
	    public void keyReleased(KeyEvent e) {
	    }
	});
	ta.setBounds(0, 100, 750, 80);
	panel.add(ta);
	
	JButton btn = new JButton(new AbstractAction() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		folderAdress = ta.getText();
		setFilesFromFolder();
	    }
	});
	btn.setSize(100, 90);
	btn.setText("Set foldeR");
	font1 = new Font( "", Font.PLAIN, 35);
	btn.setFont(font1);
	btn.setBounds(800, 100, 200, 80);
	panel.add(btn);
	
	Font f = new Font("", Font.PLAIN, 33);
	JTextArea text = console(outPipe);
	text.setFont(f);
	text.setWrapStyleWord(true);
	text.setLineWrap(true);
	text.setEditable(false);
	JScrollPane scrollPane = new JScrollPane(text);
	scrollPane.setBounds(0, 210, 1000, 650);
	panel.add(scrollPane);
	
	font1 = new Font( "", Font.PLAIN, 35);
	JLabel label2 = new JLabel( "Enter your command here " );
	label2.setFont(font1);
	label2.setBounds( 20, 870, 460, 80);
	panel.add(label2);
	
	JPanel btnPanel = new JPanel(new FlowLayout());
	JRadioButton btn1 = new JRadioButton("BST", true);
	JRadioButton btn2 = new JRadioButton("TST");
	JRadioButton btn3 = new JRadioButton("Trie");
	btn1.setSize(100, 100);
	btn2.setSize(100, 100);
	btn3.setSize(100, 100);
	btn1.setFont(f);
	btn2.setFont(f);
	btn3.setFont(f);
	btn1.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
		    kindOfTree = 1;
		}
	    }
	});
	btn2.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
		    kindOfTree = 2;
		}
	    }
	});
	btn3.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
		    kindOfTree = 3;
		}
	    }
	});
	ButtonGroup btnG = new ButtonGroup();
	btnG.add(btn1);
	btnG.add(btn2);
	btnG.add(btn3);
	btnPanel.add(btn1);
	btnPanel.add(btn2);
	btnPanel.add(btn3);
	btnPanel.setBounds(550, 885, 400, 50);
	panel.add(btnPanel);
	
	ta2 = new JTextArea(1, 100);
	font1 = new Font( "", Font.PLAIN, 40);
	ta2.setFont(font1);
	ta2.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	ta2.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
		
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
		    executeTask();
		    firstArrowUpPress = true;
		}
		//down arrow key
		if(e.getKeyCode() == 40) {
		    if (!firstArrowUpPress) {
			if(positionInRecentTasks < recentTasks.size() - 1){
			    positionInRecentTasks ++;
			    ta2.setText("");
			    ta2.setText(recentTasks.get(positionInRecentTasks));
			}
		    }
		}
		//up arrow key
		if(e.getKeyCode() == 38) {
		    
		    if(firstArrowUpPress){
			positionInRecentTasks = recentTasks.size();
			firstArrowUpPress = false;
		    }
		    if(positionInRecentTasks > 0){
			positionInRecentTasks --;
			ta2.setText("");
			ta2.setText(recentTasks.get(positionInRecentTasks));
		    }
		    
		}
		
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }
	});
	ta2.setBounds(0, 950, 1000, 80);
	panel.add(ta2);
	
	JPanel btnPanel2 = new JPanel(new GridLayout(1, 4, 60, 0));
	JButton btn31 = new JButton("Execute");
	JButton btn4 = new JButton("Build");
	JButton btn5 = new JButton("Reset");
	JButton btn6 = new JButton("Exit");
	font1 = new Font( "", Font.PLAIN, 35);
	btn31.setFont(font1);
	btn4.setFont(font1);
	btn5.setFont(font1);
	btn6.setFont(font1);
	btn31.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) throws HeadlessException{
		executeTask();
	    }
	});
	btn4.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		
		if(kindOfTree == 1){
		    
		    bst = new BST(filePaths, stopWords);
		    if(folderAdress != null)
			bst.setFolder(folderAdress);
		    else
			System.out.println("enter folder adress");
		    
		}else if(kindOfTree == 2){
		    
		    tst = new TST(filePaths, stopWords);
		    if(folderAdress != null)
			tst.setFolder(folderAdress);
		    else
			System.out.println("enter folder adress");
		    
		}else if(kindOfTree == 3){
		    
		    trie = new Trie(filePaths, stopWords);
		    if(folderAdress != null)
			trie.setFolder(folderAdress);
		    else
			System.out.println("enter folder adress");
		    
		}
		
	    }
	});
	btn5.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		
		bst = null;
		tst = null;
		trie = null;
		text.setText("");
		
	    }
	});
	btn6.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Container frame = btn6.getParent();
		do 
		    frame = frame.getParent(); 
		while (!(frame instanceof JFrame));                                      
		((JFrame) frame).dispose();
	    }
	});
	btnPanel2.add(btn31);
	btnPanel2.add(btn4);
	btnPanel2.add(btn5);
	btnPanel2.add(btn6);
	btnPanel2.setBounds(0, 1050, 1000, 80);
	panel.add(btnPanel2);
	
	panel.setBounds( 30, 30, 1440, 1235);
	add(panel);
	
	setBackground(Color.LIGHT_GRAY);
	setSize(1100, 1280);
	
    }
    
    public static JTextArea console(final InputStream out) {
	final JTextArea area = new JTextArea();

	// handle "System.out"
	new SwingWorker<Void, String>() {
	    @Override protected Void doInBackground() throws Exception {
		Scanner s = new Scanner(out);
		while (s.hasNextLine()) publish(s.nextLine() + "\n");
		return null;
	    }
	    @Override protected void process(List<String> chunks) {
		for (String line : chunks) area.append(line);
	    }
	}.execute();

	return area;
    }
    
    private void setFilesFromFolder() {
	
	File folder = new File(folderAdress);
	File[] listOfFiles = folder.listFiles();
	filePaths = new ArrayList<String>();
	
	for (int i = 0; i < listOfFiles.length; i++)
	    if (listOfFiles[i].isFile()) 
	      filePaths.add(folderAdress + "\\" + listOfFiles[i].getName());
	
    }
    
    private void executeTask() {
	
	try {
	    
	    Scanner scanner = new Scanner(ta2.getText());
	    String lastTask = scanner.nextLine();
	    recentTasks.add(lastTask);
	    scanner = new Scanner(lastTask);

	    if(!scanner.hasNext())
		return;
	    switch(scanner.next()){

		    case "add" :

			if(kindOfTree == 1)
			    bst.addFileWithName(scanner.next());
			else if(kindOfTree == 2)
			    tst.addFileWithName(scanner.next());
			else if(kindOfTree == 3)
			    trie.addFileWithName(scanner.next());
			break;

		    case "del" :

			if(kindOfTree == 1)
			    bst.deleteFile(scanner.next());
			else if(kindOfTree == 2)
			    tst.deleteFile(scanner.next());
			else if(kindOfTree == 3)
			    trie.deleteFile(scanner.next());
			break;

		    case "update" :

			if(kindOfTree == 1)
			    bst.updateFile(scanner.next());
			else if(kindOfTree == 2)
			    tst.updateFile(scanner.next());
			else if(kindOfTree == 3)
			    trie.updateFile(scanner.next());
			break;

		    case "list" :

			String s = scanner.next();
			if(s.equals("-w")){

			    if(kindOfTree == 1)
				bst.listOfWords();
			    else if(kindOfTree == 2)
				tst.listOfWords();
			    else if(kindOfTree == 3)
				trie.listOfWords();

			}
			else if(s.equals("-l")){

			    if(kindOfTree == 1)
				bst.listOfFiles();
			    else if(kindOfTree == 2)
				tst.listOfFiles();
			    else if(kindOfTree == 3)
				trie.listOfFiles();

			}
			else if(s.equals("-f")){

			    for (int i = 0; i < filePaths.size(); i++) {
				String get = filePaths.get(i);
				System.out.print(findFileNameFromPath(get) + ", ");
			    }
			    System.out.println("\n" + "number of all docs : " + filePaths.size());

			}
			break;

		    case "search" :

			s = scanner.next();
			if(s.equals("-w")){

			    if(kindOfTree == 1)
				bst.search(scanner.next());
			    else if(kindOfTree == 2)
				tst.search(scanner.next());
			    else if(kindOfTree == 3)
				trie.search(scanner.next());

			}
			else if(s.equals("-s")){

			    if(kindOfTree == 1)
				bst.searchScentence(scanner.nextLine());
			    else if(kindOfTree == 2)
				tst.searchScentence(scanner.nextLine());
			    else if(kindOfTree == 3)
				trie.searchScentence(scanner.nextLine());

			}

			break;

		    default:
			System.out.println("command not found!");

	    }
	    
	} catch (Exception e) {
	}
	
	ta2.setText("");
	
    }
    
        private String findFileNameFromPath(String filePath) {
	
	int i = filePath.length() - 4;
	int j = i;
	while (true) {	    
	    
	    if (filePath.charAt(i) == '\\')
		break;
	    else
		i--;
	    
	}
	i++;
	
	char[] chars = new char[j-i];
	
	int k = 0;
	for ( ; i < j; i++) {
	    chars[k] = filePath.charAt(i);
	    k++;
	}
	
	String fileName = new String(chars);
	
	return fileName;
    }
    
}
