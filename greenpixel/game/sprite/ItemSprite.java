package greenpixel.game.world.objects;

import greenpixel.game.sprite.Sprite2D;

import greenpixel.gut.data.ItemData;

public class ItemSprite extends Sprite2D
{
	public String description;
	public int amount;

	private int itemAnimGroup;
	private int itemAnimIndex;

	public ItemSprite()
	{
		description = "";
		itemAnimGroup = 0;
		itemAnimIndex = 0;
		amount = 0;
	}

	public void init(ItemData data)
	{
		description = data.description;
		itemAnimGroup = data.itemAnimGroup;
		itemAnimIndex = data.itemAnimIndex;
	}
}
