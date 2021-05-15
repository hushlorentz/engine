package greenpixel.game.world.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import greenpixel.gut.data.EntityData;
import greenpixel.gut.data.EntityAnimationData;
import greenpixel.gut.data.InventoryData;
import greenpixel.game.data.StatusEffect;
import greenpixel.game.sprite.Sprite2D;

import greenpixel.math.Collisions;
import greenpixel.math.FloatRect;
import greenpixel.math.Vector2D;

import java.util.ArrayList;

public class Entity extends Sprite2D 
{
	public static final int DIR_UP = 0;
	public static final int DIR_DOWN = 1;
	public static final int DIR_LEFT = 2;
	public static final int DIR_RIGHT = 3;

	public static final int ENTITY_TYPE_NONE = 0;
	public static final int ENTITY_TYPE_BREAKABLE = 1;
	public static final int ENTITY_TYPE_LOOT = 2;
	public static final int ENTITY_TYPE_CONTAINER = 3;
	public static final int ENTITY_TYPE_DOOR = 4;
	public static final int ENTITY_TYPE_TRIGGER = 5;
	public static final int ENTITY_TYPE_BARRIER = 6;
	public static final int ENTITY_TYPE_PROJECTILE = 7;

	public static final int STATE_SETUP = -1;
	public static final int AI_NONE = -1;

	public long id;
	public int direction;
	public int directionsMovedLastFrame;
	public int directionsToMove;
	public int damageMoveDirection;
	public int type;
	public int collisionDirection;
	public int userType;
	public int xTile;
	public int yTile;
	public int masterArrayIndex;
	public int aiType;
	public int counter;
	public float speed;
	public float xSpeed;
	public float ySpeed;
	public float attack;
	public float defense;
	public float health;
	public float maxHealth;
	public float range;
	public float damageDistanceRemaining;
	public boolean isMoveable;
	public boolean isCollideable;
	public boolean isHittable;
	public boolean isDamaged;
	public boolean isFriendly;
	public boolean hasArrivedAtPathDest;
	public boolean hasEntityCollision;
	public boolean hasWallCollision;
	public boolean hasNonFriendlyCollision;
	public Entity collisionEntity;
	public String displayName;
	public Vector2D heading;
	public Vector2D dest;
	public FloatRect attackBox;
	public ArrayList<InventoryData> inventory;
	public ArrayList<StatusEffect> statusEffects;

	protected ArrayList<EntityAnimationData> animations;
	protected int currentAnimSet;
	protected int directionForAnim;
	protected boolean useAnimSet;
	protected FloatRect hitBox;

	private static long idCounter = 0;

	private final int DAMAGE_FLASH_COUNTER_MAX = 16;
	private final int DAMAGE_FLASH_COUNTER_OFF = 8;
	private int actions;
	private int state;
	private int damageFlashCounter;
	private int damageCounter;
	private int damageCooldown;
	private float initialPosX;
	private float initialPosY;

	public Entity()
	{
		super();

		xSpeed = 0;
		ySpeed = 0;
		speed = 0;
		direction = DIR_DOWN;
		collisionBox = new FloatRect(0, 0, 0, 0);
		heading = new Vector2D(0, 0);
		dest = new Vector2D(0, 0);
		directionsMovedLastFrame = 0;
		directionsToMove = 0;
		type = 0;
		userType = 0;
		actions = 0;
		isMoveable = false;
		isCollideable = true;
		isHittable = true;
		attack = 0;
		defense = 0;
		health = 0;
		maxHealth = 0;
		useAnimSet = false;
		attackBox = new FloatRect(0, 0, 0, 0);
		hitBox = new FloatRect(0, 0, 0, 0);
		inventory = new ArrayList<InventoryData>();
		statusEffects = new ArrayList<StatusEffect>();
		currentAnimSet = -1;
		counter = 0;
		isFriendly = true;
		damageCooldown = 45;

		useAnimSet = false;
		animations = new ArrayList<EntityAnimationData>();

		aiType = AI_NONE;
		setState(STATE_SETUP);

		id = idCounter;
		idCounter++;
	}

	public void init(EntityData data)
	{
		collisionBox = data.collisionBox;
		attack = data.attack;
		health = data.health;
		speed = data.speed;
		range = data.range;
		displayName = data.displayName;
		isMoveable = data.isMoveable;
		isCollideable = data.isCollideable;
		isFriendly = data.isFriendly;
		isHittable = data.isHittable;
		direction = data.initialDirection;
		animations = data.animations;
		aiType = data.aiType;
		type = ENTITY_TYPE_NONE;
		hitBox.init(data.hitBox);
		zPos = data.zPos;

		maxHealth = health;
	}

	public void init(float xPos, float yPos, FloatRect bounds, float spd, boolean moveable, int entityType)
	{
		collisionBox = bounds;
		speed = spd;
		isMoveable = moveable;
		userType = entityType;
		type = ENTITY_TYPE_NONE;

		initialPosX = xPos;
		initialPosY = yPos;
		setPos(xPos, yPos);
		counter = 0;
	}

	public void resetPos()
	{
		setPos(initialPosX, initialPosY);
	}

	public void setMapLocation(int x, int y)
	{
		xTile = x;
		yTile = y;
	}

	public void setState(int newState)
	{
		state = newState;
	}

	public int getState()
	{
		return state;
	}

	public void resetAnimSet()
	{
		currentFrame = 0;
		frameCounter = 0;

		setDrawData();

		isAnimFinished = false;
		imgLoaded = true;
		isVisible = true;
	}

	public void setAnimSet(int animSet)
	{
		if (currentAnimSet == animSet)
		{
			return;
		}

		currentAnimSet = animSet;
		selectAnimation();
		useAnimSet = true;
	}

	public void setAnimSet(String animName)
	{
		for (int i = 0; i < animations.size(); i++)
		{
			EntityAnimationData aData = animations.get(i);	

			if (aData.name.equals(animName))
			{
				setAnimSet(i);
				return;
			}
		}

		System.out.println("Animation: \"" + animName + "\" on Entity \"" + displayName + "\" doesn't exist");
		System.exit(0);
	}

	public void setAnim(int animBundle, int animIndex)
	{
		super.setAnim(animBundle, animIndex);
		useAnimSet = false;
	}

	public void resetAnim()
	{
		super.resetAnim();
		useAnimSet = false;
	}

	private void selectAnimation()
	{
		int animBundle = 0;
		int animIndex = 0;
		EntityAnimationData aData = animations.get(currentAnimSet);

		switch (direction)
		{
			case DIR_UP:
				animBundle = aData.upAnimGroup;
				animIndex = aData.upAnimIndex;
				break;
			case DIR_DOWN:
				animBundle = aData.downAnimGroup;
				animIndex = aData.downAnimIndex;
				break;
			case DIR_LEFT:
				animBundle = aData.leftAnimGroup;
				animIndex = aData.leftAnimIndex;
				break;
			case DIR_RIGHT:
				animBundle = aData.rightAnimGroup;
				animIndex = aData.rightAnimIndex;
				break;
		}

		currentAnimBundle = animBundle;
		currentAnimIndex = animIndex;

		currentFrame = 0;
		frameCounter = 0;

		setDrawData();

		isAnimFinished = false;
		imgLoaded = true;
		isVisible = true;
		directionForAnim = direction;
	}

	public void setDest(float xPos, float yPos)
	{
		dest = new Vector2D(xPos, yPos);
		heading = new Vector2D(xPos - x, yPos - y);
		heading.normalize();
	}

	public boolean isNearDest()
	{
		Vector2D dist = new Vector2D(x - dest.x, y - dest.y);

		return dist.getLength() < speed;
	}

	public void clampToDest()
	{
		x = dest.x;
		y = dest.y;
	}

	public void setPathDest(float xPos, float yPos)
	{
		heading = new Vector2D(xPos - x, yPos - y);
		heading.normalize();

		dest = new Vector2D(xPos, yPos);

		hasArrivedAtPathDest = false;
	}

	public void incrementPathMove()
	{
		Vector2D dist = new Vector2D(dest.x - x, dest.y - y);

		if (dist.getLength() <= speed)
		{
			x = dest.x;
			y = dest.y;
			hasArrivedAtPathDest = true;
		}
		else
		{
			x += heading.x * speed;
			y += heading.y * speed;
		}
	}

	public void addDirectionToMove(int direction)
	{
		directionsToMove |= (1 << direction);
	}

	public boolean hasMoveDirection(int direction)
	{
		return (directionsToMove & (1 << direction)) == (1 << direction);
	}

	public void move(int direction)
	{
		directionsMovedLastFrame |= (1 << direction);
	}

	public boolean hasMoved(int direction)
	{
		return (directionsMovedLastFrame & (1 << direction)) == (1 << direction);
	}

	public void clearMoveDirections()
	{
		directionsToMove = 0;
	}

	public void clearActions()
	{
		actions = 0;
	}

	public void clearAction(int action)
	{
		actions &= ~action;
	}

	public void setAction(int action)
	{
		actions |= action;
	}

	public boolean doingAction(int action)
	{
		return (actions & action) == action;
	}

	public boolean collidesWith(Entity entity, float moveX, float moveY)
	{
		FloatRect entityRect = entity.getCollisionRect();
		FloatRect thisRect = getCollisionRect();

		thisRect.x += moveX;
		thisRect.y += moveY;

		return Collisions.rectInRect(entityRect, thisRect);
	}

	public void setCollisionData(Entity entity, int dir)
	{
		collisionEntity = entity;
		collisionDirection = dir;
		hasEntityCollision = true;
	}

	public boolean attackCollidesWith(Entity entity)
	{
		FloatRect entityRect = entity.getCollisionRect();
		FloatRect attackRect = new FloatRect(attackBox.x, attackBox.y, attackBox.width, attackBox.height);

		attackRect.x += x; //translate to the character's coordinates
		attackRect.y += y;

		return Collisions.rectInRect(entityRect, attackRect);
	}

	public void damage(float amount)
	{
		health -= amount;

		if (health > 0)
		{
			isDamaged = true;
			damageCounter = damageCooldown;
			damageFlashCounter = DAMAGE_FLASH_COUNTER_MAX;
		}
	}

	public void addItem(InventoryData data)
	{
		addItem(data.itemType, data.amount);
	}

	public void addItem(int item, int amount)
	{
		for (int i = 0; i < inventory.size(); i++)
		{
			InventoryData iData = inventory.get(i);

			if (iData.itemType == item)
			{
				iData.amount += amount;
				return;
			}
		}

		inventory.add(new InventoryData(item, amount));
	}

	public boolean hasItem(int item)
	{
		for (int i = 0; i < inventory.size(); i++)
		{
			InventoryData iData = inventory.get(i);

			if (iData.itemType == item && iData.amount > 0)
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasItemAmount(int item, int amount)
	{
		for (int i = 0; i < inventory.size(); i++)
		{
			InventoryData iData = inventory.get(i);

			if (iData.itemType == item && iData.amount >= amount)
			{
				return true;
			}
		}

		return false;
	}

	public void removeItemAmount(int item, int amount)
	{
		for (int i = 0; i < inventory.size(); i++)
		{
			InventoryData iData = inventory.get(i);

			if (iData.itemType == item && iData.amount >= amount)
			{
				iData.amount -= amount;
			}
		}
	}

	public void addStatusEffect(int type, int cooldown)
	{
		for (int i = 0; i < statusEffects.size(); i++)
		{
			StatusEffect se = statusEffects.get(i);

			if (se.type == type)
			{
				se.cooldown = cooldown;
				return;
			}
		}

		StatusEffect newEffect = new StatusEffect();
		newEffect.init(type, cooldown);

		statusEffects.add(newEffect);
	}

	public void drawToBuffer(Graphics g, int xPos, int yPos)
	{
		if (isDamaged)
		{
			if (damageFlashCounter < DAMAGE_FLASH_COUNTER_OFF)
			{
				damageFlashCounter--;

				if (damageFlashCounter == 0)
				{
					damageFlashCounter = DAMAGE_FLASH_COUNTER_MAX;
				}
				return;
			}
			else
			{
				damageFlashCounter--;
			}
		}

		super.drawToBuffer(g, xPos, yPos);

		if (debug && isVisible)
		{
			g.setColor(Color.red);
			g.fillRect(xPos + attackBox.x, yPos + attackBox.y, attackBox.width, attackBox.height);

			g.setColor(Color.green);
			g.fillRect(xPos + hitBox.x, yPos + hitBox.y, hitBox.width, hitBox.height);
		}
	}

	public void nextFrame()
	{
		super.nextFrame();

		directionsMovedLastFrame = 0;
		hasEntityCollision = false;
		hasWallCollision = false;

		if (useAnimSet && direction != directionForAnim)
		{
			selectAnimation();
		}

		if (damageCounter == 0)
		{
			isDamaged = false;
		}
		else
		{
			damageCounter--;
		}

		for (int i = statusEffects.size() - 1; i >= 0; i--)
		{
			StatusEffect se = statusEffects.get(i);

			se.cooldown--;

			if (se.cooldown <= 0)
			{
				statusEffects.remove(i);
			}
		}
	}

	public boolean equals(Object o)
	{
		if (o instanceof Entity)
		{
			Entity test = (Entity)o;

			return test.id == id;
		}

		return false;
	}
}
