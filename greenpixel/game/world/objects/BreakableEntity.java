package greenpixel.game.world.objects;

import greenpixel.game.sprite.Sprite2D;
import greenpixel.game.world.objects.Entity;
import greenpixel.gut.data.BreakableEntityData;
import greenpixel.gut.data.LootDropData;
import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class BreakableEntity extends Entity
{
	public boolean spawnLoot;
	public ArrayList<LootDropData> loot;

	private final int FIXED = 0;
	private final int BREAKING = 1;
	private final int BROKEN = 2;
	private int condition;

	private int fixedAnimGroup;
	private int fixedAnimIndex;
	private int breakingAnimGroup;
	private int breakingAnimIndex;
	private int brokenAnimGroup;
	private int brokenAnimIndex;

	private float initialHealth;
	private ArrayList<LootDropData> initialLoot;

	public BreakableEntity()
	{
		condition = FIXED;
		spawnLoot = false;

		loot = new ArrayList<LootDropData>();
		initialLoot = new ArrayList<LootDropData>();
		hitBox = new FloatRect(0, 0, 0, 0);
	}

	public void init(BreakableEntityData data, float xPos, float yPos, int mIndex)
	{
		super.init(xPos, yPos, data.collisionBox, 0, false, data.entityType);
		type = ENTITY_TYPE_BREAKABLE;
		masterArrayIndex = mIndex;

		fixedAnimGroup = data.fixedAnimGroup;
		fixedAnimIndex = data.fixedAnimIndex;
		breakingAnimGroup = data.breakingAnimGroup;
		breakingAnimIndex = data.breakingAnimIndex;
		brokenAnimGroup = data.brokenAnimGroup;
		brokenAnimIndex = data.brokenAnimIndex;

		health = data.health;
		initialHealth = data.health;

		hitBox.init(data.hitBox);

		for (int i = 0; i < data.loot.size(); i++)
		{
			LootDropData lData = new LootDropData();

			lData.lootType = data.loot.get(i).lootType;
			lData.minAmount = data.loot.get(i).minAmount;
			lData.maxAmount = data.loot.get(i).maxAmount;
			lData.dropPercentage = data.loot.get(i).dropPercentage;

			loot.add(lData);
			initialLoot.add(lData);
		}

		setAnim(fixedAnimGroup, fixedAnimIndex);
	}

	public void init(float xPos, float yPos, FloatRect bounds, int entityType)
	{
		super.init(xPos, yPos, bounds, 0, false, entityType);
	}

	public void nextFrame()
	{
		super.nextFrame();

		if (health <= 0)
		{
			switch (condition)
			{
				case FIXED:
					condition = BREAKING;
					isCollideable = false;
					setAnim(breakingAnimGroup, breakingAnimIndex);
					break;
				case BREAKING:
					if (isAnimFinished)
					{
						condition = BROKEN;
						zPos = Sprite2D.BOTTOM;
						setAnim(brokenAnimGroup, brokenAnimIndex);

						if (loot.size() > 0)
						{
							spawnLoot = true;
						}
					}
					break;
			}
		}
	}

	public Loot getSpawnInfo()
	{
		return new Loot();
	}

	public void reset()
	{
		loot.clear();

		for (int i = 0; i < initialLoot.size(); i++)
		{
			loot.add(new LootDropData(initialLoot.get(i)));
		}

		condition = FIXED;
		health = initialHealth;
		setAnim(fixedAnimGroup, fixedAnimIndex);
		spawnLoot = false;
		isCollideable = true;
	}
}
