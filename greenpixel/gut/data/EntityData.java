package greenpixel.gut.data;

import greenpixel.game.world.objects.Entity;
import greenpixel.gut.data.EntityAnimationData;
import greenpixel.gut.data.LootDropData;
import greenpixel.gut.data.QuestData;
import greenpixel.math.FloatRect;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

public class EntityData
{
	public static final int EXIT_TYPE_NONE = 0;
	public static final int EXIT_TYPE_PROGRESS = 1;
	public static final int EXIT_TYPE_CONVERSATION = 2;
	public static final int EXIT_TYPE_CUTSCENE = 3;

	public static final int LOCK_TYPE_NONE = 0;
	public static final int LOCK_TYPE_QUEST = 1;
	public static final int LOCK_TYPE_QUEST_OBJECTIVE = 2;
	public static final int LOCK_TYPE_ITEM = 3;
	public static final int LOCK_TYPE_ITEM_CONSUME = 4;

	public int initialDirection;
	public int aiType;
	public int health;
	public int money;
	public int experience;
	public int attackCooldown;
	public int damageCooldown;
	public int zPos;
	public float defense;
	public float attack;
	public float speed;
	public float range;
	public boolean isMoveable;
	public boolean isCollideable;
	public boolean isFriendly;
	public boolean isHittable;
	public String displayName;
	public String name;
	public FloatRect collisionBox;
	public FloatRect hitBox;
	public ArrayList<EntityAnimationData> animations;
	public ArrayList<Integer> dialog;
	public ArrayList<QuestData> quests;
	public ArrayList<LootDropData> loot;

	public EntityData()
	{
		initialDirection = Entity.DIR_UP;
		speed = 0;
		aiType = 0;
		health = 100;
		money = 0;
		experience = 0;
		attackCooldown = 60;
		defense = 0;
		attack = 32;
		speed = 2.5f;
		range = 100.0f;
		isMoveable = false;
		isCollideable = true;
		isFriendly = true;
		isHittable = true;
		animations = new ArrayList<EntityAnimationData>();
		dialog = new ArrayList<Integer>();
		loot = new ArrayList<LootDropData>();
		quests = new ArrayList<QuestData>();
		collisionBox = new FloatRect(0, 0, 0, 0);
		hitBox = new FloatRect(0, 0, 0, 0);
		name = "";
		displayName = "";
	}

	public String toString()
	{
		return name;
	}

	public int getSize() //gives the data size in bytes
	{
		int totalSize = 0;

		try 
		{
			totalSize += 4; //displayName.length()
			totalSize += displayName.getBytes("UTF-8").length;
			totalSize += 4; //aiType
			totalSize += 4; //initial direction
			totalSize += 4; //collisionBox.x
			totalSize += 4; //collisionBox.y			
			totalSize += 4; //collisionBox.width			
			totalSize += 4; //collisionBox.height
			totalSize += 4; //hitBox.x
			totalSize += 4; //hitBox.y
			totalSize += 4; //hitBox.width
			totalSize += 4; //hitBox.height
			totalSize += 4; //health
			totalSize += 4; //money
			totalSize += 4; //experience
			totalSize += 4; //attackCooldown
			totalSize += 4; //defense
			totalSize += 4; //attack
			totalSize += 4; //speed
			totalSize += 4; //range
			totalSize += 4; //isMoveable
			totalSize += 4; //isCollideable
			totalSize += 4; //isFriendly
			totalSize += 4; //isHittable
			totalSize += 4; //zPos			
			totalSize += 4; //animations.size()

			for (int i = 0; i < animations.size(); i++)
			{
				EntityAnimationData aData = animations.get(i);

				totalSize += 4; //aData name length
				totalSize += aData.name.getBytes("UTF-8").length; //aData name chars
				totalSize += 4; //up group
				totalSize += 4; //up index
				totalSize += 4; //down group
				totalSize += 4; //down index
				totalSize += 4; //left group
				totalSize += 4; //left index
				totalSize += 4; //right group
				totalSize += 4; //right index
			}
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}

		return totalSize;
	}
}
