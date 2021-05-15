package greenpixel.gut.data;

import greenpixel.gut.data.InventoryData;
import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class ContainerData
{
	public static int BASE_DATA_SIZE = 68;

	public int closedAnimGroup;
	public int closedAnimIndex;
	public int openingAnimGroup;
	public int openingAnimIndex;
	public int openAnimGroup;
	public int openAnimIndex;
	public int entityType;
	public int lockType;
	public int lockGroup;
	public int lockIndex;
	public int lockMessageIndex;
	public int openMessageIndex;
	public int exitType;
	public int exitTarget;
	public ArrayList<InventoryData> inventory;
	public FloatRect collisionBox;
	public String name;

	public ContainerData()
	{
		closedAnimGroup = 0;
		closedAnimIndex = 0;
		openingAnimGroup = 0;
		openingAnimIndex = 0;
		openAnimGroup = 0;
		openAnimIndex = 0;
		entityType = 0;
		lockType = 0;
		lockGroup = 0;
		lockIndex = 0;
		lockMessageIndex = 0;
		openMessageIndex = 0;
		exitType = 0;
		exitTarget = 0;

		collisionBox = new FloatRect(0, 0, 0, 0);
		inventory = new ArrayList<InventoryData>();
		name = "";
	}

	public String toString()
	{
		return name;
	}

	public int getDataSize()
	{
		return BASE_DATA_SIZE + inventory.size() * InventoryData.DATA_SIZE;
	}
}
