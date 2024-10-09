package lol.millard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.awt.event.*;
import java.util.ArrayList;


public class Game  extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	private BufferedImage back; 
	private int key, x, y;
	private GameStates state = GameStates.START;
	private long deltaTime;
	private String selectedCharacter;



	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		selectedCharacter = "";
		key =-1; 
		x=0;
		y=0;
	
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
		g2d.setFont(getFont().deriveFont(30.0f));
		g2d.setColor(Color.WHITE);
		// CODE BELOW

		gameLogic(g2d);


		// CODE ABOVE
		twoDgraph.drawImage(back, null, 0, 0);

	}

	public void gameLogic(Graphics g2d) {
		switch(state) {
			case GameStates.START:
				g2d.drawString("Click on your desired character:", 700, 100);
				int xOffset = 140; // Starting x position for the first character
				int yOffset = 350; // y position for all characters
				int spacing = 400; // Space between each character portrait
			
				// List of character classes
				Class<?>[] characterClasses = {Crusader.class, Ranger.class, Mage.class, Monk.class};
			
				for (int i = 0; i < characterClasses.length; i++) {
					try {
						BufferedImage portrait = (BufferedImage) characterClasses[i].getMethod("getPortrait").invoke(null);
						g2d.drawImage(portrait, xOffset + i * spacing, yOffset, null);
						g2d.drawString(characterClasses[i].getSimpleName(), xOffset + 100 + i * spacing, yOffset + 400);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case GameStates.SELECTED:
				g2d.drawString("You selected " + selectedCharacter, 700, 100);
				BufferedImage selectedPortrait;
				try {
						switch(selectedCharacter) {

							case "Crusader":
								selectedPortrait = (BufferedImage) Crusader.getPortrait();
								g2d.drawImage(selectedPortrait, 700, 200, null);
								break;
							case "Ranger":
								selectedPortrait = (BufferedImage) Ranger.getPortrait();
								g2d.drawImage(selectedPortrait, 700, 200, null);
								break;
							case "Mage":
								selectedPortrait = (BufferedImage) Mage.getPortrait();
								g2d.drawImage(selectedPortrait, 700, 200, null);
								break;
							case "Monk":
								selectedPortrait = (BufferedImage) Monk.getPortrait();
								g2d.drawImage(selectedPortrait, 700, 200, null);
								break;
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case GameStates.PLAY:
				break;
			case GameStates.PAUSE:
				break;
			case GameStates.END:
				break;
			default:
		}
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
	public void mouseClicked(MouseEvent e) {
		if (state == GameStates.START) {
			selectedCharacter = selectCharacter(e);
			if (!selectedCharacter.equals("")) {
				state = GameStates.SELECTED;
			}
		}
		
	}

	public String selectCharacter(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int xOffset = 140; // Starting x position for the first character
		int yOffset = 350;
		int spacing = 400;
		int portraitWidth = 315;
		int portraitHeight = 315; 

		Class<?>[] characterClasses = {Crusader.class, Ranger.class, Mage.class, Monk.class};

		for (int i = 0; i < characterClasses.length; i++) {
			int xStart = xOffset + i * spacing;
			int xEnd = xStart + portraitWidth;
			int yStart = yOffset;
			int yEnd = yStart + portraitHeight;

			if (x >= xStart && x <= xEnd && y >= yStart && y <= yEnd) {
				System.out.println(characterClasses[i].getSimpleName());
				return characterClasses[i].getSimpleName();
			}
		

		}
		return "";
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
