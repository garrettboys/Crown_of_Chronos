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
	private ImageIcon menuBackground;



	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		menuBackground = new ImageIcon("src/main/resources/menu_bg.jpeg");
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
		Font originalFont = g2d.getFont();
		Font boldFont = originalFont.deriveFont(Font.BOLD, 60f);
		switch(state) {
			case GameStates.START:
				g2d.drawImage(menuBackground.getImage(), 0, 0, null);

				g2d.setFont(boldFont);
				g2d.drawString("Crown of Chronos", 650, 200);
				g2d.setFont(originalFont);	
				g2d.drawString("Click on your desired character:", 700, 300);
				
				int xOffset = 180; // Starting x position for the first character
				int yOffset = 350; // y position for all characters
				int spacing = 400; // Space between each character portrait
			
				// List of character classes
				Class<?>[] characterClasses = {Crusader.class, Ranger.class, Mage.class, Monk.class};
			
				for (int i = 0; i < characterClasses.length; i++) {
					try {

						BufferedImage portrait = (BufferedImage) characterClasses[i].getMethod("getPortrait").invoke(null);
						g2d.setColor(Color.BLACK);
						g2d.fillRect(xOffset + i * spacing + -10, yOffset -10, portrait.getWidth()+20, portrait.getHeight()+20);
						g2d.setColor(Color.WHITE);
						g2d.drawImage(portrait, xOffset + i * spacing, yOffset, null);

						
						g2d.drawString(characterClasses[i].getSimpleName(), xOffset + 100 + i * spacing, yOffset + 400);


					} catch (Exception e) {
						e.printStackTrace();
					}
				} 
				break;
			case GameStates.SELECTED:
				g2d.drawImage(menuBackground.getImage(), 0, 0, null);
				g2d.drawString("Press Escape to go back", 760, 75);

				g2d.setFont(boldFont);
				g2d.drawString("You selected " + selectedCharacter, 660, 150);
				g2d.setFont(originalFont);
				BufferedImage selectedPortrait = null;
				ImageIcon selectedGif = null;

				String description = "";
				try {
						switch(selectedCharacter) {
							case "Crusader":
								selectedPortrait = (BufferedImage) Crusader.getPortrait();
								selectedGif = new ImageIcon("src/main/resources/menu/gifs/Crusader.gif");
								description = "The Crusader hoists a greatsword to defend his realm and his faith. He smites his foes with strong, slow strikes, and can take a beating before going down.";
								break;
							case "Ranger":
								selectedPortrait = (BufferedImage) Ranger.getPortrait();
								selectedGif = new ImageIcon("src/main/resources/menu/gifs/Ranger.gif");
								description = "The Ranger is a master of the bow. He is swift and can strike single targets from range, but failing to avoid damage will be costly.";
								break;
							case "Mage":
								selectedPortrait = (BufferedImage) Mage.getPortrait();
								selectedGif = new ImageIcon("src/main/resources/menu/gifs/Mage.gif");
								description = "The Mage fights for herself with potent spells. She does massive damage to groups of enemies with her magic fire, but is not particularly agile, and will not survive a direct hit.";
								break;
							case "Monk":
								selectedPortrait = (BufferedImage) Monk.getPortrait();
								selectedGif = new ImageIcon("src/main/resources/menu/gifs/Monk.gif");
								description = "The Monk has studied forbidden techniques to harness the power of the wild. Channeling nature's fury, he transforms into a werebear that fights with his bare claws. He shrugs off attacks, attacking swiftly and dodging even faster.";
								break;	
						}
				} catch (Exception e) {
					e.printStackTrace();
				}

				g2d.setColor(Color.BLACK);
				g2d.fillRect(755, 190, selectedPortrait.getWidth()+20, selectedPortrait.getHeight()+20);
				g2d.drawImage(selectedPortrait, 765, 200, null);
				g2d.setColor(Color.WHITE);
				String[] words = description.split(" ");
				StringBuilder line = new StringBuilder();
				int lineHeight = g2d.getFontMetrics().getHeight();
				int yPosition = 700;
			
				for (String word : words) {
					if (g2d.getFontMetrics().stringWidth(line + word) < 700) {
						line.append(word).append(" ");
					} else {
						g2d.drawString(line.toString(), 300, yPosition);
						yPosition += lineHeight;
						line = new StringBuilder(word).append(" ");
					}
				}

				if (line.length() > 0) {
					g2d.drawString(line.toString(), 300, yPosition);
				}
			
				g2d.drawImage(selectedGif.getImage(), 1000, 400, null);

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
		if (key == KeyEvent.VK_ESCAPE && state == GameStates.SELECTED) {
			state = GameStates.START;
		}
		
		
		
	
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

	}



	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	
}
