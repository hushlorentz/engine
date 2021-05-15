package greenpixel.game.world;

import greenpixel.game.DataManager;
import greenpixel.game.MapManager;
import greenpixel.game.tilemap.Tile;
import greenpixel.game.tilemap.TileMap;
import greenpixel.game.sprite.Sprite2D;
import greenpixel.game.world.objects.Barrier;
import greenpixel.game.world.objects.BreakableEntity;
import greenpixel.game.world.objects.Door;
import greenpixel.game.world.objects.Entity;
import greenpixel.game.world.objects.Container;
import greenpixel.game.world.objects.Entity;
import greenpixel.game.world.objects.Loot;
import greenpixel.game.world.objects.Projectile;
import greenpixel.game.world.objects.Trigger;
import greenpixel.gut.data.DoorData;
import greenpixel.gut.data.EntityData;
import greenpixel.gut.data.LootData;
import greenpixel.gut.data.LootDropData;
import greenpixel.gut.data.InventoryData;
import greenpixel.math.Collisions;
import greenpixel.math.FloatRect;
import greenpixel.math.Vector2D;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.Graphics;

public class World
{
	public ArrayList<Entity> entityArray;
	public ArrayList<Projectile> projectileArray;
	public ArrayList<Entity> drawList;
	public ArrayList<Entity> bottomDrawList;
	public ArrayList<Entity> topDrawList;
	public boolean hasSpawnedInventory;
	public boolean hasPickedUpLoot;
	public boolean hasActivatedTriggers;
	public boolean hasActivatedDoor;
	public boolean hasLevelLoadedTriggers;

	protected TileMap map;

	private Entity player;
	private Vector2D spriteDrawPos;
	private ArrayList<InventoryData> inventoryQueue;
	private ArrayList<Loot> pickedUpLoot;
	private ArrayList<Trigger> activatedTriggerArray;
	private ArrayList<Trigger> levelLoadedTriggerArray;
	private Door activatedDoor;

	public World(Entity sprite)
	{
		activatedTriggerArray = new ArrayList<Trigger>();
		levelLoadedTriggerArray = new ArrayList<Trigger>();
		entityArray = new ArrayList<Entity>();
		projectileArray = new ArrayList<Projectile>();
		pickedUpLoot = new ArrayList<Loot>();
		inventoryQueue = new ArrayList<InventoryData>();
		drawList = new ArrayList<Entity>();
		topDrawList = new ArrayList<Entity>();
		bottomDrawList = new ArrayList<Entity>();
		player = sprite;

		reset();
	}

	public void update(int delta)
	{
		map.centerMap(player.x, player.y);
		spriteDrawPos = map.getMainSpriteDrawPos();

		hasSpawnedInventory = false;
		hasPickedUpLoot = false;
		hasActivatedTriggers = false;
		hasActivatedDoor = false;

		pickedUpLoot.clear();
		activatedTriggerArray.clear();

		for (int i = 0; i < entityArray.size(); i++)
		{
			Entity e = entityArray.get(i);

			switch (e.type)
			{
				case Entity.ENTITY_TYPE_BREAKABLE:
					BreakableEntity be = (BreakableEntity)e;

					if (be.spawnLoot)
					{
						int lootIndex = 0;
						be.spawnLoot = false;

						int lootSpawnX = (int)(be.collisionBox.width / map.tileSize);
						int lootSpawnY = (int)(be.collisionBox.height / map.tileSize);

						if (lootSpawnX < 1) lootSpawnX++;
						if (lootSpawnY < 1) lootSpawnY++;

						for (int k = 0; k < lootSpawnY; k++)
						{
							for (int l = 0; l < lootSpawnX; l++)
							{
								LootDropData ldData = be.loot.get(lootIndex);

								LootData lData = DataManager.instance.getLoot(ldData.lootType);
								Loot loot = new Loot();
								loot.init(lData);
								loot.setPos(be.x + be.collisionBox.x + l * map.tileSize, be.y + be.collisionBox.y + k * map.tileSize);
								addEntity(loot);

								lootIndex = (lootIndex + 1 < be.loot.size()) ? lootIndex + 1 : 0;
							}
						}
					}
					break;
				case Entity.ENTITY_TYPE_LOOT:
					
					if (player.collidesWith(e, 0, 0))
					{
						Loot loot = (Loot)e;
						pickedUpLoot.add(loot);
						hasPickedUpLoot = true;
					}
					break;
				case Entity.ENTITY_TYPE_CONTAINER:
					Container con = (Container)e;

					if (con.isOpen && !con.contentsCollected)
					{
						ArrayList<InventoryData> inventory = con.getInventory();

						for (int j = 0; j < inventory.size(); j++)
						{
							hasSpawnedInventory = true;
							inventoryQueue.add(inventory.get(j));
						}
					}
					break;
				case Entity.ENTITY_TYPE_DOOR:

					Door door = (Door)e;
					if (Collisions.rectInRect(player.getCollisionRect(), door.getDoorCollisionRect()))
					{
						hasActivatedDoor = true;
						activatedDoor = door;
					}
					break;
				case Entity.ENTITY_TYPE_TRIGGER:
					Trigger trigger = (Trigger)e;

					if (!trigger.isTriggered && player.collidesWith(e, 0, 0))
					{
						boolean handleTrigger = true;

						switch (trigger.lockType)
						{
							case EntityData.LOCK_TYPE_QUEST:
								if (!DataManager.instance.hasCompletedQuest(trigger.lockIndex))
								{
									handleTrigger = false;
								}
								break;
							case EntityData.LOCK_TYPE_QUEST_OBJECTIVE:
								if (!DataManager.instance.hasCompletedQuestObjective(trigger.lockGroup, trigger.lockIndex))
								{
									handleTrigger = false;
								}
								break;
							case EntityData.LOCK_TYPE_ITEM:
								if (!player.hasItem(trigger.lockIndex))
								{
									handleTrigger = false;
								}
								break;
						}

						if (handleTrigger)
						{
							activatedTriggerArray.add(trigger);
							hasActivatedTriggers = true;
						}
					}
					break;
				case Entity.ENTITY_TYPE_BARRIER:
					Barrier barrier = (Barrier)e;

					if (barrier.canOpen() && barrier.isAutomatic)
					{
						barrier.open(player);
					}
					break;
			}
		}
	}

	public void moveEntityLeft(Entity sprite, float speed, boolean doNudge)
	{
		if (sprite.hasMoved(Entity.DIR_LEFT))
		{
			return;
		}

		sprite.move(Entity.DIR_LEFT);

		Vector2D moveVec = map.moveEntityLeft(sprite, speed, doNudge);

		if (moveVec.x == 0)
		{
			sprite.hasWallCollision = true;
		}
		if (moveVec.x < 0)
		{
			float shortestDist = Float.MAX_VALUE;
			Entity closestEntity = null;

			for (int i = 0; i < entityArray.size(); i++)
			{
				Entity e = entityArray.get(i);

				if (skipCollisionTest(sprite, e))
				{
					continue;
				}

				if (sprite.collidesWith(e, -speed, 0))
				{
					float dist = sprite.x - e.x;

					if (dist <= shortestDist)
					{
						closestEntity = e;
						shortestDist = dist;
					}
				}
			}

			if (closestEntity != null)
			{
				Entity e = closestEntity;

				sprite.setCollisionData(e, Entity.DIR_LEFT);

				FloatRect checkRect = sprite.getCollisionRect();
				checkRect.x = e.x + e.collisionBox.x + e.collisionBox.width;

				if (areaIsFree(sprite, checkRect))
				{
					float overlapUpY = sprite.y + sprite.collisionBox.y - (e.y + e.collisionBox.y);
					float overlapDownY = sprite.y + sprite.collisionBox.y + sprite.collisionBox.height - (e.y + e.collisionBox.y);
					float nudgeThreshold = 8;//e.collisionBox.height / 2;

					sprite.x = checkRect.x + sprite.collisionBox.x;

					if (doNudge && overlapDownY > 0 && overlapDownY <= nudgeThreshold && !sprite.hasMoveDirection(Entity.DIR_DOWN))
					{
						moveEntityUp(sprite, Math.min(overlapDownY, speed), doNudge);
						return;
					}
					else if (doNudge && overlapUpY > 0 && overlapUpY > (e.collisionBox.height - nudgeThreshold) && !sprite.hasMoveDirection(Entity.DIR_UP))
					{
						moveEntityDown(sprite, Math.min(overlapUpY, speed), doNudge);
						return;
					}

				}
			}
			else
			{
				sprite.x += moveVec.x;
			}
		}
		else if (moveVec.y < 0)
		{
			moveEntityUp(sprite, Math.abs(moveVec.y), doNudge);
		}
		else if (moveVec.y > 0)
		{
			moveEntityDown(sprite, Math.abs(moveVec.y), doNudge);
		}
	}
	
	public void moveEntityRight(Entity sprite, float speed, boolean doNudge)
	{
		if (sprite.hasMoved(Entity.DIR_RIGHT))
		{
			return;
		}

		sprite.move(Entity.DIR_RIGHT);

		Vector2D moveVec = map.moveEntityRight(sprite, speed, doNudge);

		if (moveVec.x == 0)
		{
			sprite.hasWallCollision = true;
		}
		if (moveVec.x > 0)
		{
			float shortestDist = Float.MAX_VALUE;
			Entity closestEntity = null;

			for (int i = 0; i < entityArray.size(); i++)
			{
				Entity e = entityArray.get(i);

				if (skipCollisionTest(sprite, e))
				{
					continue;
				}

				if (sprite.collidesWith(e, speed, 0))
				{
					float dist = e.x - sprite.x;

					if (dist <= shortestDist)
					{
						closestEntity = e;
						shortestDist = dist;
					}
				}
			}

			if (closestEntity != null)
			{
				Entity e = closestEntity; 

				sprite.setCollisionData(e, Entity.DIR_RIGHT);

				FloatRect checkRect = sprite.getCollisionRect();
				checkRect.x = e.x + e.collisionBox.x - sprite.collisionBox.width;

				if (areaIsFree(sprite, checkRect))
				{
					float overlapUpY = sprite.y + sprite.collisionBox.y - (e.y + e.collisionBox.y);
					float overlapDownY = sprite.y + sprite.collisionBox.y + sprite.collisionBox.height - (e.y + e.collisionBox.y);
					float nudgeThreshold = 8;

					sprite.x = checkRect.x - sprite.collisionBox.x;

					if (doNudge && overlapDownY > 0 && overlapDownY <= nudgeThreshold && !sprite.hasMoveDirection(Entity.DIR_DOWN))
					{
						moveEntityUp(sprite, Math.min(overlapDownY, speed), doNudge);
						return;
					}
					else if (doNudge && overlapUpY > 0 && overlapUpY > (e.collisionBox.height - nudgeThreshold) && !sprite.hasMoveDirection(Entity.DIR_UP))
					{
						moveEntityDown(sprite, Math.min(overlapUpY, speed), doNudge);
						return;
					}
				}
			}
			else
			{
				sprite.x += moveVec.x;
			}
		}
		else if (moveVec.y > 0)
		{
			moveEntityUp(sprite, Math.abs(moveVec.y), doNudge);
		}
		else if (moveVec.y < 0)
		{
			moveEntityDown(sprite, Math.abs(moveVec.y), doNudge);
		}
	}

	public void moveEntityUp(Entity sprite, float speed, boolean doNudge)
	{
		if (sprite.hasMoved(Entity.DIR_UP))
		{
			return;
		}

		sprite.move(Entity.DIR_UP);

		Vector2D moveVec = map.moveEntityUp(sprite, speed, doNudge);

		if (moveVec.y == 0)
		{
			sprite.hasWallCollision = true;
		}

		if (moveVec.y < 0)
		{
			float shortestDist = Float.MAX_VALUE;
			Entity closestEntity = null;

			for (int i = 0; i < entityArray.size(); i++)
			{
				Entity e = entityArray.get(i);

				if (skipCollisionTest(sprite, e))
				{
					continue;
				}

				if (sprite.collidesWith(e, 0, -speed))
				{
					float dist = sprite.y - e.y;

					if (dist <= shortestDist)
					{
						closestEntity = e;
						shortestDist = dist;
					}
				}
			}

			if (closestEntity != null)
			{
				Entity e = closestEntity;

				sprite.setCollisionData(e, Entity.DIR_UP);

				FloatRect checkRect = sprite.getCollisionRect();
				checkRect.y = e.y + e.collisionBox.y + e.collisionBox.height;

				if (areaIsFree(sprite, checkRect))
				{
					float overlapRightX;
					float overlapLeftX;
					float nudgeThreshold = 8;//e.collisionBox.width / 2;

					overlapRightX = sprite.x + sprite.collisionBox.x - (e.x + e.collisionBox.x);
					overlapLeftX = sprite.x + sprite.collisionBox.x + sprite.collisionBox.width - (e.x + e.collisionBox.x);

					sprite.y = checkRect.y - sprite.collisionBox.y;

					if (doNudge && overlapLeftX > 0 && overlapLeftX <= nudgeThreshold && !sprite.hasMoveDirection(Entity.DIR_RIGHT))
					{
						moveEntityLeft(sprite, Math.min(overlapLeftX, speed), doNudge);
						return;
					}
					else if (doNudge && overlapRightX > 0 && overlapRightX > (e.collisionBox.width - nudgeThreshold) && !sprite.hasMoveDirection(Entity.DIR_LEFT))
					{
						moveEntityRight(sprite, Math.min(e.collisionBox.width - overlapRightX, speed), doNudge);
						return;
					}
				}
			}
			else
			{
				sprite.y += moveVec.y;
			}
		}
		else if (moveVec.x < 0)
		{
			moveEntityLeft(sprite, Math.abs(moveVec.x), doNudge);
		}
		else if (moveVec.x > 0)
		{
			moveEntityRight(sprite, Math.abs(moveVec.x), doNudge);
		}
	}

	public void moveEntityDown(Entity sprite, float speed, boolean doNudge)
	{
		if (sprite.hasMoved(Entity.DIR_DOWN))
		{
			return;
		}

		sprite.move(Entity.DIR_DOWN);

		Vector2D moveVec = map.moveEntityDown(sprite, speed, doNudge);

		if (moveVec.y == 0)
		{
			sprite.hasWallCollision = true;
		}
		if (moveVec.y > 0)
		{
			float shortestDist = Float.MAX_VALUE;
			Entity closestEntity = null;

			for (int i = 0; i < entityArray.size(); i++)
			{
				Entity e = entityArray.get(i);

				if (skipCollisionTest(sprite, e))
				{
					continue;
				}

				if (sprite.collidesWith(e, 0, speed))
				{
					float dist = e.y - sprite.y;

					if (dist <= shortestDist)
					{
						closestEntity = e;
						shortestDist = dist;
					}
				}
			}

			if (closestEntity != null)
			{
				Entity e = closestEntity;

				sprite.setCollisionData(e, Entity.DIR_DOWN);

				FloatRect checkRect = sprite.getCollisionRect();
				checkRect.y = e.y - sprite.collisionBox.height;

				if (areaIsFree(sprite, checkRect))
				{
					float overlapRightX;
					float overlapLeftX;
					float nudgeThreshold = 8;//e.collisionBox.width / 2;

					overlapRightX = sprite.x + sprite.collisionBox.x - (e.x + e.collisionBox.x);
					overlapLeftX = sprite.x + sprite.collisionBox.x + sprite.collisionBox.width - (e.x + e.collisionBox.x);

					sprite.y = e.y + e.collisionBox.y - (sprite.collisionBox.y + sprite.collisionBox.height);

					if (doNudge && overlapLeftX > 0 && overlapLeftX <= nudgeThreshold && !sprite.hasMoveDirection(Entity.DIR_RIGHT))
					{
						moveEntityLeft(sprite, Math.min(overlapLeftX, speed), doNudge);
						return;
					}
					else if (doNudge && overlapRightX > 0 && overlapRightX > (e.collisionBox.width - nudgeThreshold) && !sprite.hasMoveDirection(Entity.DIR_LEFT))
					{
						moveEntityRight(sprite, Math.min(e.collisionBox.width - overlapRightX, speed), doNudge);
						return;
					}
				}
			}
			else
			{
				sprite.y += moveVec.y;
			}
		}
		else if (moveVec.x < 0)
		{
			moveEntityLeft(sprite, Math.abs(moveVec.x), doNudge);
		}
		else if (moveVec.x > 0)
		{
			moveEntityRight(sprite, Math.abs(moveVec.x), doNudge);
		}
	}

	private boolean skipCollisionTest(Entity src, Entity test)
	{
		return (src.id == test.id || (!src.isFriendly && test == player) || !src.isCollideable || !test.isCollideable || (src == player && src.isDamaged && !test.isFriendly) || (!test.isFriendly && !src.isFriendly)); //skip if same, skip if src is an enemy and test is the player (we handle that collision in the game), skip if test or src are not not collideable, skip if the damaged player touches and enemy, skip if both are enemies
	}

	public boolean areaIsFreeOfWalls(FloatRect checkRect)
	{
		int checkX = (int)(Math.ceil((checkRect.x + checkRect.width) / map.tileSize) - (int)(checkRect.x / map.tileSize));
		int checkY = (int)(Math.ceil((checkRect.y + checkRect.height) / map.tileSize) - (int)(checkRect.y / map.tileSize));

		for (int i = 0; i < checkY; i++)
		{
			for (int j = 0; j < checkX; j++)
			{
				if (map.hasWall(checkRect.x + j * map.tileSize, checkRect.y + i * map.tileSize))
				{
					return false;
				}
			}
		}

		return true;
	}

	public boolean areaIsFree(Entity checkEntity, FloatRect checkRect)
	{
		if (!areaIsFreeOfWalls(checkRect))
		{
			return false;
		}

		for (int i = 0; i < entityArray.size(); i++)
		{
			Entity e = entityArray.get(i);

			if (e.id == checkEntity.id || !e.isCollideable)
			{
				continue;
			}

			if (Collisions.rectInRect(checkRect, e.getCollisionRect()))
			{
				return false;
			}		
		}

		return true;
	}

	public boolean areaIsFreeForProjectile(Projectile p)
	{
		FloatRect checkRect = p.getCollisionRect();

		int checkX = (int)(Math.ceil((checkRect.x + checkRect.width) / map.tileSize) - (int)(checkRect.x / map.tileSize));
		int checkY = (int)(Math.ceil((checkRect.y + checkRect.height) / map.tileSize) - (int)(checkRect.y / map.tileSize));

		for (int i = 0; i < checkY; i++)
		{
			for (int j = 0; j < checkX; j++)
			{
				float posX = checkRect.x + j * map.tileSize;
				float posY = checkRect.y + i * map.tileSize;

				if (posX < 0 || posY < 0 || posX >= map.mapWidth * map.tileSize || posY >= map.mapHeight * map.tileSize || map.hasWall(posX, posY))
				{
					return false;
				}
			}
		}

		return true;
	}

	public void setTileMap(Door door)
	{
		player.setPos(door.xTile * map.tileSize, door.yTile * map.tileSize);
		setTileMap(door.mapIndex, false);
	}

	public void resetData()
	{
		MapManager.instance.resetPersistentData();
		reset();
	}

	public void setTileMap(int mapIndex, boolean useMapPlayerPos)
	{
		reset();

		map = MapManager.instance.getMap(mapIndex);

		//scan the map and populate the world
		for (int i = 0; i < map.mapHeight; i++)
		{
			for (int j = 0; j < map.mapWidth; j++)
			{
				if (map.hasSpecial(j, i))
				{
					switch (map.getSpecialType(j, i))
					{
						case Tile.SPECIAL_ENTITY:
							Entity e = new Entity();
							e.init(DataManager.instance.getCharacter(map.getSpecialValue(j, i)));
							e.setAnimSet(0);
							e.setPos(j * map.tileSize, i * map.tileSize);
							addEntity(e);
							break;
						case Tile.SPECIAL_BREAKABLE:
							addEntity(map.getBreakableEntity(j, i));
							break;
						case Tile.SPECIAL_CONTAINER:
							addEntity(map.getContainer(j, i));
							break;
						case Tile.SPECIAL_DOOR:
							Door door = new Door();
							door.init(DataManager.instance.getDoor(map.getSpecialValue(j, i)));
							door.setPos(j * map.tileSize, i * map.tileSize);
							addEntity(door);
							break;
						case Tile.SPECIAL_LOOT:
							Loot loot = new Loot();
							loot.init(DataManager.instance.getLoot(map.getSpecialValue(j, i)));
							loot.setPos(j * map.tileSize, i * map.tileSize);
							addEntity(loot);
							break;
						case Tile.SPECIAL_PLAYER:
							if (player != null && useMapPlayerPos)
							{
								player.setPos(j * map.tileSize, i * map.tileSize);
								player.direction = map.getSpecialValue(j, i);
							}
							break;
						case Tile.SPECIAL_TRIGGER:
							addEntity(map.getTrigger(j, i));
							break;
						case Tile.SPECIAL_BARRIER:
							addEntity(map.getBarrier(j, i));
							break;
					}
				}
			}
		}

		hasLevelLoadedTriggers = false;

		for (int i = 0; i < map.onLoadTriggerArray.size(); i++)
		{
			Trigger t = map.onLoadTriggerArray.get(i);

			if (!t.isTriggered)
			{
				boolean handleTrigger = true;

				switch (t.lockType)
				{
					case EntityData.LOCK_TYPE_QUEST:
						if (!DataManager.instance.hasCompletedQuest(t.lockIndex))
						{
							handleTrigger = false;
						}
						break;
					case EntityData.LOCK_TYPE_QUEST_OBJECTIVE:
						if (!DataManager.instance.hasCompletedQuestObjective(t.lockGroup, t.lockIndex))
						{
							handleTrigger = false;
						}
						break;
					case EntityData.LOCK_TYPE_ITEM:
						if (!player.hasItem(t.lockIndex))
						{
							handleTrigger = false;
						}
						break;
				}

				if (handleTrigger)
				{
					switch (t.triggerType)
					{
						case Trigger.TYPE_QUEST:
							break;
						case Trigger.TYPE_QUEST_OBJECTIVE:
							DataManager.instance.completeQuestObjective(t.triggerGroup, t.triggerIndex);
							break;
						default:
							levelLoadedTriggerArray.add(t);
							hasLevelLoadedTriggers = true;
							break;
					}
				}
			}
		}

		map.centerMap(player.x, player.y);
		spriteDrawPos = map.getMainSpriteDrawPos();
	}

	public void reset()
	{
		entityArray.clear();
		projectileArray.clear();
		pickedUpLoot.clear();
		activatedTriggerArray.clear();
		inventoryQueue.clear();
		drawList.clear();
		bottomDrawList.clear();
		topDrawList.clear();

		hasSpawnedInventory = false;
		hasPickedUpLoot = false;
		hasActivatedTriggers = false;
		hasActivatedDoor = false;

		addEntity(player);
	}

	public void addEntity(Entity e)
	{
		entityArray.add(e);
	}

	public Projectile getAvailableProjectile()
	{
		for (int i = 0; i < projectileArray.size(); i++)
		{
			Projectile p = projectileArray.get(i);

			if (p.getState() == Projectile.STATE_EXPLODED)
			{
				return p;
			}
		}

		Projectile p = new Projectile();
		projectileArray.add(p);

		return projectileArray.get(projectileArray.size() - 1);
	}

	public Door getActivatedDoor()
	{
		return activatedDoor;
	}

	public ArrayList<Trigger> getActivatedTriggers()
	{
		return activatedTriggerArray;
	}

	public ArrayList<Loot> getPickedUpLoot()
	{
		return pickedUpLoot;
	}

	public void removePickedUpLoot()
	{
		for (int i = 0; i < pickedUpLoot.size(); i++)
		{
			entityArray.remove(pickedUpLoot.get(i));
		}
	}

	public ArrayList<Trigger> getLevelLoadedTriggers()
	{
		return levelLoadedTriggerArray;
	}

	public void clearLevelLoadedTriggers()
	{
		hasLevelLoadedTriggers = false;
		levelLoadedTriggerArray.clear();
	}

	public ArrayList<InventoryData> getInventoryData()
	{
		return inventoryQueue;
	}

	public void clearInventoryData()
	{
		inventoryQueue.clear();
	}

	public int getTileSize()
	{
		return map.tileSize;
	}

	public void drawToBuffer(Graphics g)
	{
		map.drawToBuffer(g);
		map.nextFrame();

		bottomDrawList.clear();
		drawList.clear();
		topDrawList.clear();

		for (int i = 0; i < entityArray.size(); i++)
		{
			Entity e = entityArray.get(i);

			if (e.zPos == Sprite2D.BOTTOM)
			{
				bottomDrawList.add(e);
			}
			else if (e.zPos == Sprite2D.TOP)
			{
				topDrawList.add(e);
			}
			else
			{
				drawList.add(e);
			}
		}

		Collections.sort(drawList);

		for (int i = 0; i < bottomDrawList.size(); i++)
		{
			Entity e = bottomDrawList.get(i);
			drawSprite(g, e);
		}

		for (int i = 0; i < drawList.size(); i++)
		{
			Entity e = drawList.get(i);
			drawSprite(g, e);
		}

		for (int i = 0; i < topDrawList.size(); i++)
		{
			Entity e = topDrawList.get(i);
			drawSprite(g, e);
		}

		for (int i = 0; i < projectileArray.size(); i++)
		{
			Projectile p = projectileArray.get(i);

			if (p.getState() != Projectile.STATE_EXPLODED)
			{
				drawSprite(g, p);
			}
		}
	}

	public void nextFrame()
	{
		map.nextFrame();
	}

	public void drawSprite(Graphics g, Sprite2D sprite)
	{
		Vector2D mapOffset = map.getMapOffset();
		sprite.drawToBuffer(g, (int)(sprite.x - mapOffset.x), (int)(sprite.y - mapOffset.y));
		sprite.nextFrame();
	}
}
