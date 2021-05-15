package greenpixel.gut.data;

import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class BarrierData
{
	public static final int EXIT_TYPE_NONE = 0;
	public static final int EXIT_TYPE_PROGRESS = 1;
	public static final int EXIT_TYPE_CONVERSATION = 2;
	public static final int EXIT_TYPE_CUTSCENE = 3;

	public static final int LOCK_TYPE_NONE = 0;
	public static final int LOCK_TYPE_PROGRESS = 1;
	public static final int LOCK_TYPE_ITEM = 2;

	public int closedAnimGroup;
	public int closedAnimIndex;
	public int openingAnimGroup;
	public int openingAnimIndex;
	public int openAnimGroup;
	public int openAnimIndex;
	public int lockType;
	public int lockGroup;
	public int lockIndex;
	public int lockMessageIndex;
	public int exitType;
	public int exitTarget;
	public boolean isAutomatic;
	public FloatRect collisionBox;
	public String name;

	public BarrierData()
	{
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

		collisionBox = new FloatRect(0, 0, 0, 0);
		name = "";
	}

	public String toString()
	{
		return name;
	}
}
