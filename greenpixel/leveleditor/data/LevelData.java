package greenpixel.leveleditor.data;

import greenpixel.leveleditor.data.TileMapData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelData
{
	public int music;
	public int northLevelIndex;
	public int southLevelIndex;
	public int westLevelIndex;
	public int eastLevelIndex;
	public LevelData northLevel;
	public LevelData southLevel;
	public LevelData westLevel;
	public LevelData eastLevel;
	public String name;
	public TileMapData tileMap;
	public BufferedImage tileSheet;
	public ArrayList<Integer> triggers;

	public LevelData()
	{
		name = "";
		triggers = new ArrayList<Integer>();
	}

	public String toString()
	{
		return name;
	}

	public boolean equals(Object o)
	{
		if (o instanceof LevelData)
		{
			return name.equals(((LevelData)o).name);
		}

		return false;
	}
}
