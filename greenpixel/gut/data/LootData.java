package greenpixel.gut.data;

import greenpixel.gut.data.InventoryData;

import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class LootData
{
	public String name;
	public int lootAnimGroup;
	public int lootAnimIndex;
	public FloatRect collisionBox;
	public ArrayList<InventoryData> inventory;

	public LootData()
	{
		name = "";
		lootAnimGroup = 0;
		lootAnimIndex = 0;
		collisionBox = new FloatRect(0, 0, 0, 0);
		inventory = new ArrayList<InventoryData>();
	}

	public String toString()
	{
		return name;
	}
}
