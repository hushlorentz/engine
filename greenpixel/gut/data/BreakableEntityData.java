package greenpixel.gut.data;

import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class BreakableEntityData
{
	public String name;
	public FloatRect collisionBox;
	public FloatRect hitBox;
	public int fixedAnimGroup;
	public int fixedAnimIndex;
	public int breakingAnimGroup;
	public int breakingAnimIndex;
	public int brokenAnimGroup;
	public int brokenAnimIndex;
	public int health;
	public int entityType;
	public ArrayList<LootDropData> loot;

	public BreakableEntityData()
	{
		name = "";
		fixedAnimGroup = 0;
		fixedAnimIndex = 0;
		breakingAnimGroup = 0;
		breakingAnimIndex = 0;
		brokenAnimGroup = 0;
		brokenAnimIndex = 0;
		health = 100;
		entityType = 0;
		collisionBox = new FloatRect(0, 0, 0, 0);
		hitBox = new FloatRect(0, 0, 0, 0);
		loot = new ArrayList<LootDropData>();
	}

	public String toString()
	{
		return name;
	}
}
