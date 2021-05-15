package greenpixel.gut.data;

import java.util.ArrayList;

public class WaveBundle
{
	public static final int EXIT_TYPE_NONE = 0;
	public static final int EXIT_TYPE_CUTSCENE = 1;
	public static final int EXIT_TYPE_PROGRESS = 2;

	public ArrayList<WaveObject> waves;
	public String name;
	public int xPos;
	public int yPos;
	public int exitType;
	public int exitTarget;

	public WaveBundle(String n)
	{
		waves = new ArrayList<WaveObject>();
		name = n;

		xPos = 0;
		yPos = 0;
		exitType = EXIT_TYPE_NONE;
		exitTarget = 0;
	}

	public String toString()
	{
		return name;
	}
}
