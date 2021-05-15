package greenpixel.game.world.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import greenpixel.game.DataManager;
import greenpixel.game.world.objects.Entity;
import greenpixel.gut.data.BarrierData;
import greenpixel.gut.data.EntityData;

import greenpixel.math.FloatRect;

public class Barrier extends Entity
{
	private final int STATE_CLOSED = 0;
	private final int STATE_OPENING = 1;
	private final int STATE_OPEN = 2;

	public static final int OPEN_OK = 0;
	public static final int OPEN_ERROR_LOCKED = 1;
	public static final int OPEN_ERROR_OPENING = 2;
	public static final int OPEN_ERROR_ALREADY_OPEN = 3;

	public boolean isOpen;
	public int lockType;
	public int lockGroup;
	public int lockIndex;
	public int lockMessageIndex;
	public int exitType;
	public int exitTarget;
	public boolean isAutomatic;

	private int closedAnimGroup;
	private int closedAnimIndex;
	private int openingAnimGroup;
	private int openingAnimIndex;
	private int openAnimGroup;
	private int openAnimIndex;

	public Barrier()
	{
		super();

		closedAnimGroup = 0;
		closedAnimIndex = 0;
		openingAnimGroup = 0;
		openingAnimIndex = 0;
		openAnimGroup = 0;
		openAnimIndex = 0;
		lockType = 0;
		lockGroup = 0;
		lockIndex = 0;
		lockMessageIndex = 0;
		exitType = 0;
		exitTarget = 0;
		isAutomatic = true;
	}

	public void init(BarrierData data, float xPos, float yPos)
	{
		super.init(xPos, yPos, data.collisionBox, 0, false, 0);
		type = ENTITY_TYPE_BARRIER;

		closedAnimGroup = data.closedAnimGroup;
		closedAnimIndex = data.closedAnimIndex;
		openingAnimGroup = data.openingAnimGroup;
		openingAnimIndex = data.openingAnimIndex;
		openAnimGroup = data.openAnimGroup;
		openAnimIndex = data.openAnimIndex;

		isAutomatic = data.isAutomatic;

		lockType = data.lockType;
		lockGroup = data.lockGroup;
		lockIndex = data.lockIndex;
		lockMessageIndex = data.lockMessageIndex;

		exitType = data.exitType;
		exitTarget = data.exitTarget;

		isOpen = false;
		setAnim(closedAnimGroup, closedAnimIndex);

		setState(STATE_CLOSED);
	}

	public boolean canOpen()
	{
		if (isOpen || getState() == STATE_OPENING)
		{
			return false;
		}

		return true;
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

	public void nextFrame()
	{
		super.nextFrame();

		if (getState() == STATE_OPENING && isAnimFinished)
		{
			isOpen = true;
			isCollideable = false;
			setState(STATE_OPEN);
			setAnim(openAnimGroup, openAnimIndex);
		}
	}
}
