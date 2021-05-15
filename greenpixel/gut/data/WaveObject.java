package greenpixel.gut.data;

import java.util.ArrayList;

import greenpixel.gut.data.EntityData;

public class WaveObject
{
	public static final int TYPE_ORDER = 0;
	public static final int TYPE_RANDOM = 1;

	public ArrayList<Integer> amounts;
	public ArrayList<Integer> enemies;
	public ArrayList<Integer> delays;
	public ArrayList<EntityData> enemyArray;
	public int type;

	public WaveObject(ArrayList<EntityData> array)
	{
		enemyArray = array;	

		enemies = new ArrayList<Integer>();
		amounts = new ArrayList<Integer>();
		delays = new ArrayList<Integer>();
		type = TYPE_ORDER;
	}

	public String toString()
	{
		String waveString = "";

		for (int i = 0; i < amounts.size(); i++)
		{
			if (i > 0 && i < amounts.size())
			{
				waveString += " | ";
			}	

			waveString += enemyArray.get(enemies.get(i)).name + "  x" + amounts.get(i) + "  D:" + delays.get(i);
		}

		return waveString;
	}
}
