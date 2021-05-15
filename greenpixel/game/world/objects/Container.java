package greenpixel.game.world.objects;

import greenpixel.game.DataManager;
import greenpixel.game.world.objects.Entity;

import greenpixel.gut.data.ContainerData;
import greenpixel.gut.data.EntityData;
import greenpixel.gut.data.InventoryData;

import java.util.ArrayList;

public class Container extends Entity
{
	private final int STATE_CLOSED = 0;
	private final int STATE_OPENING = 1;
	private final int STATE_OPEN = 2;

	public static final int OPEN_OK = 0;
	public static final int OPEN_ERROR_LOCKED = 1;
	public static final int OPEN_ERROR_OPENING = 2;
	public static final int OPEN_ERROR_ALREADY_OPEN = 3;

	public boolean isOpen;
	public boolean contentsCollected;

	public int lockType;
	public int lockGroup;
	public int lockIndex;
	public int lockMessageIndex;
	public int openMessageIndex;
	public int exitType;
	public int exitTarget;

	private int closedAnimGroup;
	private int closedAnimIndex;
	private int openingAnimGroup;
	private int openingAnimIndex;
	private int openAnimGroup;
	private int openAnimIndex;
	private ArrayList<InventoryData> inventory;
	private ArrayList<InventoryData> initialInventory;

	public Container()
	{
		inventory = new ArrayList<InventoryData>();
		initialInventory = new ArrayList<InventoryData>();

		closedAnimGroup = 0;
		closedAnimIndex = 0;
		openingAnimGroup = 0;
		openingAnimIndex = 0;
		openAnimGroup = 0;
		openAnimIndex = 0;
		lockType = 0;
		lockGroup = 0;
		lockIndex = 0;
		exitType = 0;
		exitTarget = 0;
		lockMessageIndex = 0;
		openMessageIndex = 0;
	}

	public void init(ContainerData data, float xPos, float yPos, int mIndex)
	{
		super.init(xPos, yPos, data.collisionBox, 0, false, data.entityType);
		type = ENTITY_TYPE_CONTAINER;
		masterArrayIndex = mIndex;

		closedAnimGroup = data.closedAnimGroup;
		closedAnimIndex = data.closedAnimIndex;
		openingAnimGroup = data.openingAnimGroup;
		openingAnimIndex = data.openingAnimIndex;
		openAnimGroup = data.openAnimGroup;
		openAnimIndex = data.openAnimIndex;

		lockType = data.lockType;
		lockGroup = data.lockGroup;
		lockIndex = data.lockIndex;
		lockMessageIndex = data.lockMessageIndex;
		openMessageIndex = data.openMessageIndex;
		exitType = data.exitType;
		exitTarget = data.exitTarget;

		for (int i = 0; i < data.inventory.size(); i++)
		{
			inventory.add(data.inventory.get(i));
			initialInventory.add(data.inventory.get(i));
		}

		isOpen = false;
		contentsCollected = false;
		setAnim(closedAnimGroup, closedAnimIndex);

		setState(STATE_CLOSED);
	}

	public void reset()
	{
		inventory.clear();

		for (int i = 0; i < initialInventory.size(); i++)
		{
			inventory.add(new InventoryData(initialInventory.get(i)));
		}

		isOpen = false;
		contentsCollected = false;
		setAnim(closedAnimGroup, closedAnimIndex);

		setState(STATE_CLOSED);

		resetPos();
	}

	public int open(Entity entity)
	{
		if (isOpen)
		{
			return OPEN_ERROR_ALREADY_OPEN;
		}

		switch (getState())
		{
			case STATE_OPENING:
				return OPEN_ERROR_OPENING;
			case STATE_CLOSED:
				switch (lockType)
				{
					case EntityData.LOCK_TYPE_QUEST:
						if (!DataManager.instance.hasCompletedQuest(lockIndex))
						{
							return OPEN_ERROR_LOCKED;
						}
						break;

					case EntityData.LOCK_TYPE_QUEST_OBJECTIVE:
						if (!DataManager.instance.hasCompletedQuestObjective(lockGroup, lockIndex))
						{
							return OPEN_ERROR_LOCKED;
						}
						break;
					case EntityData.LOCK_TYPE_ITEM:
						if (!entity.hasItem(lockIndex))
						{
							return OPEN_ERROR_LOCKED;
						}
						break;
					case EntityData.LOCK_TYPE_ITEM_CONSUME:
						if (!entity.hasItemAmount(lockIndex, 1))
						{
							return OPEN_ERROR_LOCKED;
						}
						else
						{
							entity.removeItemAmount(lockIndex, 1);
						}
						break;
				}
				break;
		}

		setAnim(openingAnimGroup, openingAnimIndex);
		setState(STATE_OPENING);

		return OPEN_OK;
	}

	public ArrayList<InventoryData> getInventory()
	{
		contentsCollected = true;

		return inventory;
	}

	public void nextFrame()
	{
		super.nextFrame();

		if (getState() == STATE_OPENING && isAnimFinished)
		{
			isOpen = true;
			setState(STATE_OPEN);
			setAnim(openAnimGroup, openAnimIndex);
		}
	}
}
