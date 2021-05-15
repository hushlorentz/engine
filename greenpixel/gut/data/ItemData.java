package greenpixel.gut.data;

public class ItemData
{
	public String description;
	public String name;
	public int itemAnimGroup;
	public int itemAnimIndex;

	public ItemData()
	{
		description = "";
		name = "";
		itemAnimGroup = 0;
		itemAnimIndex = 0;
	}

	public String toString()
	{
		return name;
	}
}
