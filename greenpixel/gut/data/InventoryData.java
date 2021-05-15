package greenpixel.gut.data;

public class InventoryData
{
	public static int DATA_SIZE = 8;
	public int itemType;
	public int amount;

	public InventoryData()
	{
		itemType = 0;
		amount = 0;
	}

	public InventoryData(InventoryData iData)
	{
		itemType = iData.itemType;
		amount = iData.amount;
	}

	public InventoryData(int item, int amt)
	{
		itemType = item;
		amount = amt;
	}
}

