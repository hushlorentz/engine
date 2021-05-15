package greenpixel.game.world;

public class WorldLootData
{
	public float x;
	public float y;
	public int lootDataType;

	public WorldLootData(float xPos, float yPos, int dataType)
	{
		x = xPos;
		y = yPos;
		lootDataType = dataType;
	}
}
