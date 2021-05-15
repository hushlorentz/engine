package greenpixel.leveleditor.data;

import java.util.ArrayList;

public class ProjectData
{
	public String name;
	public ArrayList<LevelData> levels;

	public ProjectData()
	{
		name = "";
		levels = new ArrayList<LevelData>();
	}

	public String toString()
	{
		return name;
	}
}
