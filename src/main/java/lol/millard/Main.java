package lol.millard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame{
	public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int HEIGHT= Toolkit.getDefaultToolkit().getScreenSize().height;
	
	public Main () {
		super("KeyListener Demo");
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		Game play = new Game();
		((Component) play).setFocusable(true);
		
		Color freakyBlue = new Color(65,48,146);
		
		
		setBackground(freakyBlue);
		
		
		getContentPane().add(play);
		
		setVisible(true);
		addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				//play.createFile();
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	

	public static void main(String[] args) {
		Main run = new Main();
	}


}
