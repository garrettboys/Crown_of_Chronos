package lol.millard;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Boss {
    private float x;
    private float y;
    private int width;
    private int height;
    private int health;
    private int dx, dy;
    private Boolean lastMoveRight = true;
    private Boolean attackPending = false;
    private Map<AnimationStates, BufferedImage[]> sprites = new HashMap<>();
    
    private int idleRightFrameCount = 1;
    private int idleRightAnimationCount = 0;
    private int idleLeftFrameCount = 1;
    private int idleLeftAnimationCount = 0;
    // private int runRightFrameCount = 1;
    // private int runRightAnimationCount = 0;
    // private int runLeftFrameCount = 1;
    // private int runLeftAnimationCount = 0;
    private int attackRightFrameCount = 1;
    private int attackRightAnimationCount = 0;
    private int attackLeftFrameCount = 1;
    private int attackLeftAnimationCount = 0;

    private int animationSpeed = 5; // 5 frames per animation frame
    private AnimationStates animState = AnimationStates.IDLE_RIGHT; // default state

    private long lastAttackTime = System.currentTimeMillis(); 
    private long attackDuration = 0;
    private static final long ATTACK_COOLDOWN = 2000; // cooldown in milliseconds 
    
    private boolean isFlashingRed = false;
    private int redFlashFramesRemaining = 0;
    private Rectangle hitbox;
    
    private attackInitListener attackInitListener;
    
    public interface attackInitListener {
        void attack();
    }
    
    public void setAttackInitListener(attackInitListener listener) {
        this.attackInitListener = listener;
    }
    
    public void startAttack() {
        if (attackInitListener != null) {
            attackInitListener.attack();
        }
    }
    
    public enum AnimationStates {
        IDLE_RIGHT, IDLE_LEFT, ATTACK_RIGHT, ATTACK_LEFT, DIE_LEFT, DIE_RIGHT
    }
    
    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        dx = 0; 
        dy = 0;
        width = 96;
        height = 96;
        health = 500;
        setSprites(sprites);
        setHitbox(new Rectangle((int)x, (int)y, width, height));
    }

    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            lastAttackTime = currentTime; // reset
            return true;
        }
        return false;
    }

    public void setSprites(Map<AnimationStates, BufferedImage[]> sprites) {
        try {
            BufferedImage[] idleRight = new BufferedImage[6];
            for (int i = 1; i <= 6; i++) {
                idleRight[i-1] = ImageUtils
                        .resizeImage(ImageIO.read(new File("src/main/resources/game/skeleton_archer/idle/"+i+".png")), 500, 500);
            }
            sprites.put(AnimationStates.IDLE_RIGHT, idleRight);
            
            BufferedImage[] idleLeft = new BufferedImage[6];
			for (int i = 1; i <= 6; i++) {
				idleRight[i-1] = ImageUtils.flipImageHorizontally(ImageUtils
						.resizeImage(ImageIO.read(new File("src/main/resources/game/skeleton_archer/idle/"+i+".png")), 500, 500));
			}
			sprites.put(AnimationStates.IDLE_LEFT, idleLeft);

			BufferedImage[] attackRight = new BufferedImage[9];
			for (int i = 1; i <= 9; i++) {
				attackRight[i-1] = ImageUtils
						.resizeImage(ImageIO.read(new File("src/main/resources/game/skeleton_archer/atk/"+i+".png")), 500, 500);
			}
			sprites.put(AnimationStates.ATTACK_RIGHT, attackRight);

			BufferedImage[] attackLeft = new BufferedImage[9];
			for (int i = 1; i <= 9; i++) {
				attackLeft[i-1] = ImageUtils.flipImageHorizontally(ImageUtils
						.resizeImage(ImageIO.read(new File("src/main/resources/game/skeleton_archer/atk/"+i+".png")), 500, 500));
			}
			sprites.put(AnimationStates.ATTACK_LEFT, attackLeft);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCurrentSprite() {
        if (isFlashingRed && redFlashFramesRemaining == 0) 
            isFlashingRed = false;
        switch (animState) {
            case IDLE_RIGHT:   
                idleRightAnimationCount++;
                if (idleRightAnimationCount >= animationSpeed) {
                    idleRightFrameCount++;
                    idleRightAnimationCount = 0; 
                }
                if (idleRightFrameCount >= 6) { // assuming 6 frames
                    idleRightFrameCount = 0;
                }
                return getIdleRightSprite(idleRightFrameCount);
			case IDLE_LEFT:
			idleLeftAnimationCount++;
			if (idleLeftAnimationCount >= animationSpeed) {
				idleLeftFrameCount++;
				idleLeftAnimationCount = 0;
			}
			if (idleLeftFrameCount >= 6) { // assuming 6 frames
				idleLeftFrameCount = 0;
			}
			return getIdleLeftSprite(idleLeftFrameCount);
		    case ATTACK_RIGHT:
				attackRightAnimationCount++;
				if (attackRightAnimationCount >= animationSpeed) {
					attackRightFrameCount++;
					attackRightAnimationCount = 0;
				}
				if (attackRightFrameCount == 9) {
					animState = AnimationStates.IDLE_RIGHT;
                    attackPending = true;
                    attackRightFrameCount = 0;
				}
				return getAttackRightSprite(attackRightFrameCount);
			case ATTACK_LEFT:
				attackLeftAnimationCount++;
				if (attackLeftAnimationCount >= animationSpeed) {
					attackLeftFrameCount++;
					attackLeftAnimationCount = 0;
				}
				if (attackLeftFrameCount == 9) { 
					animState = AnimationStates.IDLE_LEFT;
                    attackPending = true;
                    attackLeftFrameCount = 0;
                    
				}
				return getAttackLeftSprite(attackLeftFrameCount);

            default:
                return getIdleRightSprite(1);
        }
    }

    public BufferedImage getIdleRightSprite(int frameCt) {
        if (!isFlashingRed) 
            return sprites.get(AnimationStates.IDLE_RIGHT)[frameCt];
        else {
            redFlashFramesRemaining--;
            return ImageUtils.tintRed(sprites.get(AnimationStates.IDLE_RIGHT)[frameCt]);
        }
    }

	public BufferedImage getIdleLeftSprite(int frameCt) {
		if (!isFlashingRed) 
			return sprites.get(AnimationStates.IDLE_LEFT)[frameCt];
		else {
			redFlashFramesRemaining--;
			return ImageUtils.tintRed(sprites.get(AnimationStates.IDLE_LEFT)[frameCt]);
		}
	}

	public BufferedImage getAttackRightSprite(int frameCt) {
		if (!isFlashingRed) 
			return sprites.get(AnimationStates.ATTACK_RIGHT)[frameCt];
		else {
			redFlashFramesRemaining--;
			return ImageUtils.tintRed(sprites.get(AnimationStates.ATTACK_RIGHT)[frameCt]);
		}
	}

	public BufferedImage getAttackLeftSprite(int frameCt) {
		if (!isFlashingRed) 
			return sprites.get(AnimationStates.ATTACK_LEFT)[frameCt];
		else {
			redFlashFramesRemaining--;
			return ImageUtils.tintRed(sprites.get(AnimationStates.ATTACK_LEFT)[frameCt]);
		}
	}

    


    public boolean isAttacking() {
        return animState == AnimationStates.ATTACK_RIGHT || animState == AnimationStates.ATTACK_LEFT;
    }

	public boolean isAttackPending() {
        if (attackPending) {
            attackPending = false;
            return true;
        }
        return false;
	}

    public int getX() {
        return (int)(x + 0.5);
    }

    public void setX(float x) {
        this.x = x;
    }

    public int getY() {
        return (int)(y + 0.5);
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public AnimationStates getState() {
        return animState;
    }

    public void setState(AnimationStates state) {
        this.animState = state;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Boolean getLastMoveRight() {
        return lastMoveRight;
    }

    public void setLastMoveRight(Boolean lastMoveRight) {
        this.lastMoveRight = lastMoveRight;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isFlashingRed() {
        return isFlashingRed;
    }

    public void setFlashingRed(boolean isFlashingRed) {
        this.isFlashingRed = isFlashingRed;
    }

    public int getRedFlashFramesRemaining() {
        return redFlashFramesRemaining;
    }

    public void setRedFlashFramesRemaining(int redFlashFramesRemaining) {
        this.redFlashFramesRemaining = redFlashFramesRemaining;
    }

    public Vector getPosition() {    
        return new Vector(x, y);
    }

    public boolean isCooldown() {
        return System.currentTimeMillis() - lastAttackTime < ATTACK_COOLDOWN;
    }

    public long getAttackDuration() {
        return attackDuration;
    }

    public void setAttackDuration(long attackDuration) {
        this.attackDuration = attackDuration;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

	public long getLastAttackTime() {
		return lastAttackTime;
	}
}