package greenpixel.gut.data;

import java.util.ArrayList;

public class QuestData
{
	public String name;
	public String displayName;
	public String description;
	public ArrayList<QuestObjectiveData> objectives;

	public QuestData()
	{
		name = "";
		displayName = "";
		description = "";
		objectives = new ArrayList<QuestObjectiveData>();
	}

	public String toString()
	{
		return name;
	}	
}
