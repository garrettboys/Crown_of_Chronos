package lol.millard;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class Boss { // screw inheritance
    private float x;
    private float y;
    private int width;
    private int height;
    private int health;
    private int dx, dy;
    private Boolean lastMoveRight = true;
    private Map<AnimationStates, BufferedImage[]> sprites = new HashMap<>();
    
    private int idleRightFrameCount = 1;
    private int idleRightAnimationCount = 0;
    private int idleLeftFrameCount = 1;
    private int idleLeftAnimationCount = 0;
    private int runRightFrameCount = 1;
    private int runRightAnimationCount = 0;
    private int runLeftFrameCount = 1;
    private int runLeftAnimationCount = 0;
    private int throwingRightFrameCount = 1;
    private int throwingRightAnimationCount = 0;
    private int throwingLeftFrameCount = 1;
    private int throwingLeftAnimationCount = 0;


	private int animationSpeed = 5; // 5 frames per animation frame
    private AnimationStates animState = AnimationStates.IDLE_RIGHT; // default state

	// below for boss ai handling
    // private AIStates aiState = AIStates.IDLING; // default ai state

    // note: for simplicity's sake, all below AI logic removed
	
    // private double distanceToPlayer;

    // private AttackStates attackState = AttackStates.IDLE;
    
    private long lastAttackTime = 0; 
    private long attackDuration = 0;
    private static final long ATTACK_COOLDOWN = 2000; //cd in milliseconds 
    
    private boolean isFlashingRed = false;
    private int redFlashFramesRemaining = 0;
    
    private Rectangle hitbox;
    
    private attackInitListener attackInitListener;
    
	// private List<Point> patrolPath = new ArrayList<>(); // the patrol path
	// private int currentWaypoint = 0; // the current waypoint the boss is moving towards
	// private static final double SPEED = 1.5;
	// private static final double THRESHOLD = 1.0;
	// private long waypointReachedTime;
    
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
    
	// public enum AIStates {
	// 	IDLING, CHASING, THROWING, RETREATING
	// }

	
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

		// createPatrolPath();
	}

	// public void createPatrolPath() {
	// 	patrolPath.add(new Point(0, 0));

	// }

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
			
			// BufferedImage[] idleLeft = new BufferedImage[7];
			// for (int i = 1; i <= 7; i++) {
			// 	idleLeft[i - 1] = ImageUtils.flipImageHorizontally(idleRight[i - 1]);
			// }
			// sprites.put(AnimationStates.IDLE_LEFT, idleLeft);
			
			// BufferedImage[] throwingRight = new BufferedImage[7];
			// for (int i = 1; i <= 7; i++) {
			// 	throwingRight[i - 1] = ImageUtils
			// 			.resizeImage(ImageIO.read(new File("assets/boss_sprites/throw" + i + ".png")), 96, 96);
			// }
			// sprites.put(AnimationStates.THROWING_RIGHT, throwingRight);
			
			// BufferedImage[] throwingLeft = new BufferedImage[7];
			// for (int i = 1; i <= 7; i++) {
			// 	throwingLeft[i - 1] = ImageUtils.flipImageHorizontally(throwingRight[i - 1]);
			// }
			// sprites.put(AnimationStates.THROWING_LEFT, throwingLeft);
			
			// BufferedImage[] runRight = new BufferedImage[6];
			// for (int i = 1; i <= 6; i++) {
			// 	runRight[i - 1] = ImageUtils.resizeImage(ImageIO.read(new File("assets/boss_sprites/run" + i + ".png")),
			// 			96, 96);
			// }
			// sprites.put(AnimationStates.RUN_RIGHT, runRight);
			
			// BufferedImage[] runLeft = new BufferedImage[6];
			// for (int i = 1; i <= 6; i++) {
			// 	runLeft[i - 1] = ImageUtils.flipImageHorizontally(runRight[i - 1]);
			// }
			// sprites.put(AnimationStates.RUN_LEFT, runLeft);
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getCurrentSprite() { // attack logic contained in here
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

	    // case IDLE_LEFT: 
	    //     if (++idleLeftAnimationCount == animationSpeed) {
	    //         idleLeftAnimationCount = 0;
	    //         idleLeftFrameCount = (idleLeftFrameCount % 7) + 1;
	    //     }
	    //     return getIdleLeftSprite(idleLeftFrameCount - 1);
	        
	    // case RUN_RIGHT:
	    //     if (++runRightAnimationCount == animationSpeed) {
	    //         runRightAnimationCount = 0;
	    //         runRightFrameCount = (runRightFrameCount % 6) + 1;
	    //     }
	    //     return getRunRightSprite(runRightFrameCount - 1);
	        
	    // case RUN_LEFT:
	    //     if (++runLeftAnimationCount == animationSpeed) {
	    //         runLeftAnimationCount = 0;
	    //         runLeftFrameCount = (runLeftFrameCount % 6) + 1;
	    //     }
	    //     return getRunLeftSprite(runLeftFrameCount - 1);
	        
	    // case THROWING_RIGHT:
	    //     if (++throwingRightAnimationCount == animationSpeed) {
	    //         throwingRightAnimationCount = 0;
	    //         throwingRightFrameCount = (throwingRightFrameCount % 7) + 1; 
	    //         if (throwingRightFrameCount == 1) {
	    //             this.setState(AnimationStates.IDLE_RIGHT);
	    //         }
	    //     }
		// 	if (throwingRightFrameCount == 5 && canAttack()) {
		// 		startAttack();
		// 	}
	    //     return getThrowingRightSprite(throwingRightFrameCount - 1);
	        
	    // case THROWING_LEFT:
	    //     if (++throwingLeftAnimationCount == animationSpeed) {
	    //         throwingLeftAnimationCount = 0;
	    //         throwingLeftFrameCount = (throwingLeftFrameCount % 7) + 1; 
	    //         if (throwingLeftFrameCount == 1) {
	    //             this.setState(AnimationStates.IDLE_LEFT);
	    //         }
	    //     }
	    //     if (throwingLeftFrameCount == 5 && canAttack()) {
        //         startAttack();
	    //     }
	    //     return getThrowingLeftSprite(throwingLeftFrameCount - 1);

	    default:
	        return getIdleRightSprite(1);
	    }
	}
	
	// public void patrol() {
	// 	Point target = patrolPath.get(currentWaypoint);

	// 	if (this.x == target.x && this.y == target.y && System.currentTimeMillis() - waypointReachedTime >= 1000) {
	// 		currentWaypoint = (currentWaypoint + 1) % patrolPath.size();
	// 		waypointReachedTime = System.currentTimeMillis(); 
	// 	} else if (this.x != target.x || this.y != target.y) { 
	// 		moveTowards(target.x, target.y);
	// 	}
	// }
	

	// public void moveTowards(int targetX, int targetY) {
	// 	double dx = targetX - this.x;
	// 	double dy = targetY - this.y;
	
	// 	double distance = Math.sqrt(dx * dx + dy * dy);

	// 	if (distance <= THRESHOLD) {
	// 		this.x = targetX;
	// 		this.y = targetY;
	// 	} else {
	// 		double directionX = dx / distance;
	// 		double directionY = dy / distance;
		
	
	// 	this.x += directionX * SPEED;
	// 	this.y += directionY * SPEED;
	// 	}
	// 	if (this.x == targetX && this.y == targetY) {
	// 		waypointReachedTime = System.currentTimeMillis();
	// 	}

	// }

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
		
		else  {
			redFlashFramesRemaining--;
			return ImageUtils.tintRed(sprites.get(AnimationStates.IDLE_LEFT)[frameCt]);
		}
	}

	// public BufferedImage getRunRightSprite(int frameCt) {
	// 	if (!isFlashingRed)
	//     return sprites.get(AnimationStates.RUN_RIGHT)[frameCt];
		
	// 	else {
	// 		redFlashFramesRemaining--;
	// 		return ImageUtils.tintRed(sprites.get(AnimationStates.RUN_RIGHT)[frameCt]);
	// 	}
	// }

	// public BufferedImage getRunLeftSprite(int frameCt) {
	// 	if (!isFlashingRed)
	//     return sprites.get(AnimationStates.RUN_LEFT)[frameCt];
		
	// 	else {
	// 		redFlashFramesRemaining--;
	// 		return ImageUtils.tintRed(sprites.get(AnimationStates.RUN_LEFT)[frameCt]);
	// 	}
	// }

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

    
    public int getAnimationSpeed() {
		return animationSpeed;
	}

	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	// public AIStates getAiState() {
	// 	return aiState;
	// }

	// public void setAiState(AIStates aiState) {
	// 	this.aiState = aiState;
	// }

	// public double getDistanceToPlayer() {
	// 	return distanceToPlayer;
	// }

	// public void setDistanceToPlayer(double distanceToPlayer) {
	// 	this.distanceToPlayer = distanceToPlayer;
	// }

	
	public int getX() {
		return (int)(x+.5);
	}

	public void setX(float x) {
		this.x = x;
	}

	public int getY() {
		return (int)(y+.5);
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

    
    // public AttackStates getAttackState() {
	// 	return attackState;
	// }

	// public void setAttackState(AttackStates attackState) {
	// 	this.attackState = attackState;
	// }

	public boolean isCooldown() {
		if (System.currentTimeMillis() - lastAttackTime < ATTACK_COOLDOWN)
			return true;
		else
			return false;
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
}
