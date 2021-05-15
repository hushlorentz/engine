package greenpixel.game.data;

import java.util.ArrayList;

public class Quest
{
	private boolean debug;

	public String name;
	public String description;
	public ArrayList<QuestObjective> objectives;

	public Quest()
	{
		debug = false;
		name = "";
		description = "";
		objectives = new ArrayList<QuestObjective>();
	}

	public void addObjective(QuestObjective qo)
	{
		objectives.add(qo);
	}

	public void completeObjective(int index)
	{
		objectives.get(index).complete();
	}

	public boolean isComplete()
	{
		for (int i = 0; i < objectives.size(); i++)
		{
			QuestObjective qo = objectives.get(i);

			if (!qo.isComplete)
			{
				return false;
			}
		}

		return true;
	}	

	public boolean isObjectiveComplete(int index)
	{
		return objectives.get(index).isComplete;
	}

	public int getNumberOfCompleteObjectives()
	{
		int objectivesComplete = 0;

		for (int i = 0; i < objectives.size(); i++)
		{
			QuestObjective qo = objectives.get(i);

			if (qo.isComplete)
			{
				objectivesComplete++;
			}
		}

		return objectivesComplete;
	}

	public void complete(int index)
	{
		objectives.get(index).complete();

		if (debug)
		{
			System.out.println("Completed: " + objectives.get(index).name);
		}
	}

	public void setDebug(boolean dbg)
	{
		debug = dbg;
	}

	public String toString()
	{
		return name;
	}	
}
