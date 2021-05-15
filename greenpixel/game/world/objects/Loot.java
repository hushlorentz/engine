package greenpixel.game.world.objects;

import greenpixel.game.world.objects.Entity;
import greenpixel.gut.data.InventoryData;
import greenpixel.gut.data.LootData;

import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class Loot extends Entity
{
	public ArrayList<InventoryData> inventory;

	public Loot()
	{
		inventory = new ArrayList<InventoryData>();	
	}

	public void init(LootData data)
	{
		init(0, 0, data.collisionBox, 0, false, -1);
		type = ENTITY_TYPE_LOOT;
		isCollideable = false;

		for (int i = 0; i < data.inventory.size(); i++)
		{
			InventoryData iData = data.inventory.get(i);
			InventoryData tempData = new InventoryData();

			tempData.itemType = iData.itemType;
			tempData.amount = iData.amount;

			inventory.add(tempData);
		}

		setAnim(data.lootAnimGroup, data.lootAnimIndex);
	}
}
