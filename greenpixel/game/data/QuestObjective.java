package greenpixel.game.data;

public class QuestObjective
{
	public String name;
	public String description;
	public boolean isComplete;
	public boolean isCriteriaMet;

	public QuestObjective()
	{
		name = "";
		description = "";
		isComplete = false;
		isCriteriaMet = false;
	}

	public void complete()
	{
		isComplete = true;
		isCriteriaMet = true;
	}

	public String toString()
	{
		return name;
	}
}
