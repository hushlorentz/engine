package greenpixel.gut.data;

public class LootDropData
{
	public int lootType;
	public int minAmount;
	public int maxAmount;
	public float dropPercentage;

	public LootDropData()
	{
		lootType = 0;
		minAmount = 0;
		maxAmount = 1;
		dropPercentage = 0;
	}

	public LootDropData(LootDropData lData)
	{
		lootType = lData.lootType;
		minAmount = lData.minAmount;
		maxAmount = lData.maxAmount;
		dropPercentage = lData.dropPercentage;
	}
}
