package lol.millard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Game  extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	private BufferedImage back; 
	private int key, x, y;
	public static GameStates state = GameStates.START;
	private double deltaTime;
	private String selectedCharacter;
	private ImageIcon menuBackground;
	private Player player;
	private boolean isAttacking;
	private HashSet<Integer> pressedKeys;
	private long lastTime;
	private Queue<Boss> enemies;
	private ArrayList<Projectile> projectiles;

	private static final double NANO_TO_MILLI = 1000000.0;
	private static final double TARGET_FRAME_TIME = 1000.0/60.0;


	
	public Game() {
		new Thread(this).start();	
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		menuBackground = new ImageIcon("src/main/resources/menu_bg.jpeg");
		selectedCharacter = "";
		player = new Player(400, 400);
		pressedKeys = new HashSet<Integer>();
		isAttacking = false;
		lastTime = System.nanoTime();
		key =-1; 
		x=0;
		y=0;
		enemies = new LinkedList<>();
		fillEnemies();
		projectiles = new ArrayList<Projectile>();

	}

	
	
	public void run()
	   {
	   	try
	   	{
	   		while(true)
	   		{ // designed to maintain a standard 60 fps for rendering
	   		    long now = System.nanoTime();
	   		    deltaTime = (now - lastTime) / NANO_TO_MILLI;  
	   		    lastTime = now;

	   		    repaint(); 
	   		    // calculate how much time to sleep to maintain 60 FPS
	   		    double timeTaken = (System.nanoTime() - now) / NANO_TO_MILLI;
	   		    double timeToSleep = TARGET_FRAME_TIME - timeTaken;  // time remaining to reach 16.67 milliseconds (1000 ms / 60 fps)

	   		    if (timeToSleep > 0) 
	   		            Thread.sleep((long)timeToSleep);  // sleep to maintain the frame rate
	         }
	      }
	   		catch(Exception e)
	      {
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

				g2d.setFont(boldFont);
				g2d.drawString("Press Space to Start", 640, 1000);
				g2d.setFont(originalFont);


				break;
			case GameStates.PLAY:
				player.update(deltaTime);
				Boss boss = enemies.peek();
				
				g2d.drawImage(player.getCurrentSprite(), player.getX(), player.getY(), null);
				player.setHitbox(new Rectangle(player.getX()+210, player.getY()+200, 100, 100));
				//g2d.drawRect((int)player.getHitbox().getX(), (int)player.getHitbox().getY(), player.getHitbox().width, player.getHitbox().height);
				g2d.drawImage(boss.getCurrentSprite(), boss.getX(), boss.getY(), null); 
				boss.setHitbox(new Rectangle(boss.getX()+215, boss.getY()+200, 100, 100));
				//g2d.drawRect((int)boss.getHitbox().getX(), (int)boss.getHitbox().getY(), boss.getHitbox().width, boss.getHitbox().height);

				if (boss.getState() == Boss.AnimationStates.IDLE_LEFT || boss.getState() == Boss.AnimationStates.IDLE_RIGHT) {
					if (!boss.isAttacking()) {
						long attackInterval = (long) (Math.random() * 4000) + 1000; // Random interval between 1-5 seconds
						if (System.currentTimeMillis() - boss.getLastAttackTime() >= attackInterval) {
							boss.setState(Boss.AnimationStates.ATTACK_RIGHT);
							boss.setLastAttackTime(System.currentTimeMillis());
						}
						if (boss.isAttackPending()) {
							Vector direction = new Vector(player.getX() - boss.getX(), player.getY() - boss.getY());
							direction.normalize();
							Projectile projectile = new Projectile(boss.getX(), boss.getY(), direction, 300);
							projectiles.add(projectile);
						}
					}
				}

				projectileRender(g2d);
                

				break;
			case GameStates.PAUSE:
				break;
			case GameStates.END:
				break;
			default:
		}
	}


    private void fillEnemies() {
        Random rand = new Random();
        int screenWidth = 1920;
        int screenHeight = 1080;
        int borderOffset = 100;

        for (int i = 0; i < 9; i++) {
            int x = rand.nextInt(screenWidth - 2 * borderOffset) + borderOffset;
            int y = rand.nextInt(screenHeight - 2 * borderOffset) + borderOffset;
            enemies.add(new Boss(x, y));
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

	//DO NOT DELETE
	@Override
	public void keyTyped(KeyEvent e) {
		
	}




//DO NOT DELETE
	@Override
	public void keyPressed(KeyEvent e) {
		
		key= e.getKeyCode();

	    pressedKeys.add(e.getKeyCode());
		updatePlayerState();
		if (key == KeyEvent.VK_ESCAPE && state == GameStates.SELECTED) {
			state = GameStates.START;
		}

		if (key == KeyEvent.VK_SPACE && state == GameStates.SELECTED) {
			state = GameStates.PLAY;
		}
		
		
		
	
	}


	//DO NOT DELETE
	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
		updatePlayerState();
		
		
	}



	@Override
	public void mouseDragged(MouseEvent arg0) {
	}



	@Override
	public void mouseMoved(MouseEvent arg0) {
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

	private void updatePlayerState() {
	    // if currently attacking, ignore all other input until the attack is complete
	    if (isAttacking) return;

	    boolean hasHorizontalInput = pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_D);
	    boolean hasVerticalInput = pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_S);

	    // reset movement velocities
	    player.setDx(0);
	    player.setDy(0);

		if (pressedKeys.contains(KeyEvent.VK_A)) 
			player.setDx(-player.getSpeed());
		

		if (pressedKeys.contains(KeyEvent.VK_D)) 
			player.setDx(player.getSpeed());

		if (pressedKeys.contains(KeyEvent.VK_W))
			player.setDy(-player.getSpeed());
		
		if (pressedKeys.contains(KeyEvent.VK_S))
			player.setDy(player.getSpeed());
  
		if (hasHorizontalInput || hasVerticalInput)
			player.setPlayerState(Player.States.RUN_RIGHT);
		else	
			player.setPlayerState(Player.States.IDLE_RIGHT);
			

	    //     player.setLastDirectionMoved(Player.Directions.WEST);
	        
	    // } else if (pressedKeys.contains(KeyEvent.VK_D)) {
	    //     player.setState(Player.States.RUN_RIGHT);
	    //     player.setDx(player.getSpeed());
	    //     player.setLastDirectionMoved(Player.Directions.EAST);
	    // }

	    // if (pressedKeys.contains(KeyEvent.VK_W)) {
	    //     player.setDy(-player.getSpeed());
	    //     player.setLastDirectionMoved(Player.Directions.NORTH);
	        
	    // } else if (pressedKeys.contains(KeyEvent.VK_S)) {
	    //     player.setDy(player.getSpeed());
	    //     player.setLastDirectionMoved(Player.Directions.SOUTH);
	    // }
	    
	    // if (!hasHorizontalInput && hasVerticalInput) {
	    //     player.setState(player.getLastDirectionMoved() == Player.Directions.WEST ? Player.States.RUN_LEFT : Player.States.RUN_RIGHT);
	    // }

	    // if (pressedKeys.contains(KeyEvent.VK_F) && canAttack()) {
	    //     player.setState(player.getAttackDirection());
		// 	player.attackCheck(boss);
	    //     isAttacking = true;
	    // }

	    // // reset to appropriate idle state based on last direction moved if no movement keys are pressed
	    // if (!hasHorizontalInput && !hasVerticalInput && !isAttacking) {
	    //     if (player.getLastDirectionMoved() == Player.Directions.WEST) {
	    //         player.setState(Player.States.IDLE_LEFT);
	    //     } else {
	    //         player.setState(Player.States.IDLE_RIGHT);
	    //     }
	    // }
	    
	}

	private boolean canAttack() {
	    return !player.getPlayerState().name().startsWith("ATK");
	}

	public void projectileRender(Graphics g) {
		for (Projectile projectile : projectiles) {
			projectile.draw((Graphics2D) g);
		}
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
 