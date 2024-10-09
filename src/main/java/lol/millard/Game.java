package lol.millard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.awt.event.*;
import java.util.ArrayList;


public class Game  extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	private BufferedImage back; 
	private int key, x, y;
	private ArrayList <Character> charList;
	private GameStates state = GameStates.START;
	private long deltaTime;




	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		key =-1; 
		x=0;
		y=0;
		charList = setCharList();
		
	
	}

	
	
	public void run()
	   {
	   	try
	   	{
			long lastTime = System.nanoTime();
			while (true) {
				long currentTime = System.nanoTime();
				deltaTime = currentTime - lastTime;
				lastTime = currentTime;

				// Convert deltaTime to seconds for more readable measurement
				double deltaTimeInSeconds = deltaTime / 1e9;

				// Output deltaTimeInSeconds to see the time elapsed between each frame
				System.out.println("Delta time: " + deltaTimeInSeconds + " seconds");

				repaint();
			}
        }
	   		catch(Exception e)
	      {
			  e.printStackTrace();
	      }
	  	}
	

	
	
	
	public void paint(Graphics g){
		
		Graphics2D twoDgraph = (Graphics2D) g; 
		if( back ==null)
			back=(BufferedImage)( (createImage(getWidth(), getHeight()))); 

		Graphics g2d = back.createGraphics();
		g2d.clearRect(0,0,getSize().width, getSize().height);
		// CODE BELOW

		switch(state) {
			case GameStates.START:

			case GameStates.PLAY:
			case GameStates.PAUSE:
			case GameStates.END:
			default:
		}



		
		// CODE ABOVE
		twoDgraph.drawImage(back, null, 0, 0);

	}

	public ArrayList<Character> setCharList() {

		ArrayList<Character> tempList = new ArrayList<>();
		tempList.add(new Mage());
		tempList.add(new Crusader());
		tempList.add(new Thief());
		tempList.add(new Monk());
		return tempList;
	}



	//DO NOT DELETE
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}




//DO NOT DELETE
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		key= e.getKeyCode();
		System.out.println(key);
		
		
		
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		
		
		
		
	}



	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		x=arg0.getX();
		y=arg0.getY();
	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("entered");
	}



	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("exited");
	}



	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		System.out.println("you clicked at"+ arg0.getY());
		x=arg0.getX();
		y=arg0.getY();
		
	}



	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
}
