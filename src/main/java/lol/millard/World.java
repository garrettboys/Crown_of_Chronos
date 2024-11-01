package lol.millard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
    private BufferedImage[] map; // store the entire tilemap as one image
    private boolean[][] collisionMap; // store the walkable status of each tile

    /*
     * Constructor for the World class
     * Requires the path to the image file that contains the map
     * @param imagePath the path to the image file that contains the map
     */
    public World() {
        loadMap("assets/decorUnderPlayer.png", "assets/decorOverPlayer.png"); // load the map at initialization
        createCollisionMap(); // create the collision map at initialization
    }

    private void loadMap(String pathUnder, String pathOver) {
        try {
        	map = new BufferedImage[2];
            map[0] = ImageIO.read(new File(pathUnder)); // load the map image under the 
            map[1] = ImageIO.read(new File(pathOver)); // load the map image
        } catch (IOException e) {
            e.printStackTrace();
            map = null; 
        }
    }

    
    private void createCollisionMap() { //hardcoded 
    	collisionMap = new boolean[40][21]; // [column][row]];
    	
    	int[][] collisionTiles = { // [column][row] tiles that are collide enabled
            
     
        };

        // Loop over the collisionTiles array and set the corresponding tiles on the collisionMap
        for (int[] tile : collisionTiles) 
            collisionMap[tile[0]][tile[1]] = true;
    }
    
    public boolean isCollision(int row, int column) {
    	try {
    		if (row >= 0 && row < collisionMap.length && column >= 0 && column < collisionMap[0].length)
            return collisionMap[row][column];
    		else
    			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
    }
    
    
    
    public void mapRenderUnder(Graphics g) {
        if (map != null) {
            g.drawImage(map[0], 0, 0, null); // draw the first layer of the map at the top-left corner
        }
    }
    
	public void mapRenderOver(Graphics g) {
		if (map != null) {
			g.drawImage(map[1], 0, 0, null); // draw the second layer of the map at the top-left corner
		}
	}
}
