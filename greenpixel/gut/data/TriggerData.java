package greenpixel.gut.data;

import greenpixel.math.FloatRect;

public class TriggerData
{
	public static final int TYPE_CONVERSATION = 0;
	public static final int TYPE_DOOR = 1;
	public static final int TYPE_CUTSCENE = 2;
	public static final int TYPE_QUEST = 3;
	public static final int TYPE_QUEST_OBJECTIVE = 4;
	public static final int TYPE_USER = 5;

	public static final int ACTIVATION_PLAYER = 0;
	public static final int ACTIVATION_ANY = 1;

	public boolean isVisible;
	public int triggerType;
	public int triggerGroup;
	public int triggerIndex;
	public int lockType;
	public int lockGroup;
	public int lockIndex;
	public FloatRect collisionBox;

	public int fireAmount;
	public int activationType;
	public int idleAnimGroup;
	public int idleAnimIndex;
	public int activatingAnimGroup;
	public int activatingAnimIndex;
	public int activatedAnimGroup;
	public int activatedAnimIndex;
	public String name;

	public TriggerData()
	{
		name = "";
		triggerType = 0;
		triggerGroup = 0;
		triggerIndex = 0;
		activationType = 0;
		collisionBox = new FloatRect(0, 0, 0, 0);
		isVisible = false;
		idleAnimGroup = 0;
		idleAnimIndex = 0;
		activatingAnimGroup = 0;
		activatingAnimIndex = 0;
		activatedAnimGroup = 0;
		activatedAnimIndex = 0;
		fireAmount = 0;
		lockType = 0;
		lockGroup = 0;
		lockIndex = 0;
	}

	public String toString()
	{
		return name;
	}
}
