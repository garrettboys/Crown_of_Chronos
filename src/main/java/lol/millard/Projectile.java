package lol.millard; 

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Projectile { // for dynamite projectile physics, moves in 360 deg instead of 8 directions
	private Vector position;
	private Vector velocity;
	private BufferedImage sprite;
	// private int frameCount = 1;
	// private int animationCount = 1;
	// private int animationSpeed = 5;
	private int width = 12;
	private int height = 12;
	private Rectangle hitbox;
	
	// static {
	//     try {
	//         for (int i = 1; i <= 7; i++) {
	//             try {
	//                 sprites[i-1] = ImageUtils.resizeImage(ImageIO.read(new File("assets/boss_sprites/dynamite" + i + ".png")), 96, 96);
	//             } catch (IOException e) { // default img
	//                 sprites[i-1] = ImageUtils.resizeImage(ImageIO.read(new File("assets/boss_sprites/dynamite1.png")), 96, 96);
	//             }
	//         }
	//     } catch (Exception e) {
	//         e.printStackTrace();
	//     }
	// }

	
	public Projectile(float startX, float startY, Vector direction, float speed) {
		try {
	    position = new Vector(startX, startY);
	    direction.normalize();
	    direction.scale(speed); 
	    velocity = direction; 
	    hitbox = new Rectangle((int)position.getX()+100, (int)position.getY()+100, width, height);
		sprite = ImageUtils.resizeImage(ImageIO.read(new File("src/main/resources/game/skeleton_archer/arrow.png")
				), 100, 100);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public BufferedImage getCurrentSprite() {
	// 	if (animationCount == animationSpeed) {
	// 		frameCount++;
	// 		animationCount = 1;
	// 	}
		
	// 	animationCount++;
		
	// 	if (frameCount == 7) {
	// 		frameCount = 1;
	// 	}
		
	// 	return sprites[frameCount - 1];
		
	// }
	
    public void draw(Graphics2D g2d) {
        double angle = Math.atan2(velocity.getY(), velocity.getX());
        AffineTransform transform = new AffineTransform();
        transform.translate(position.getX(), position.getY());
        transform.rotate(angle, sprite.getWidth() / 2, sprite.getHeight() / 2);
        g2d.drawImage(sprite, transform, null);
    }

	public Vector getPosition() {
		return position;
	}
	
	
	public void update() {
		position.add(velocity);
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHitbox(Rectangle rectangle) {
		this.hitbox = rectangle;		
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
