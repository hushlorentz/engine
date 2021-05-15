package greenpixel.gut.data;

import greenpixel.math.FloatRect;

import java.util.ArrayList;

public class DoorData
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
	public int mapIndex;
	public int xTile;
	public int yTile;
	public int exitType;
	public int exitTarget;
	public boolean isVisible;
	public FloatRect collisionBox;
	public FloatRect doorBox;
	public String name;

	public DoorData()
	{
		mapIndex = 0;
		xTile = 0;
		yTile = 0;
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
		isVisible = true;

		collisionBox = new FloatRect(0, 0, 0, 0);
		doorBox = new FloatRect(0, 0, 0, 0);
		name = "";
	}

	public String toString()
	{
		return name;
	}
}
