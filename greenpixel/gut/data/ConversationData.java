package greenpixel.gut.data;

import java.util.ArrayList;

public class ConversationData
{
	public int repeatType;
	public int lowerProgressLock;
	public int upperProgressLock;
	public int exitTargetType;
	public int exitTargetIndex;
	public String name;
	public ArrayList<DialogData> dialog;

	public ConversationData()
	{
		name = "";
		repeatType = 0;
		lowerProgressLock = 0;
		upperProgressLock = 0;
		exitTargetType = 0;
		exitTargetIndex = 0;
		dialog = new ArrayList<DialogData>();
	}

	public String toString()
	{
		return name;
	}

	public void copyTo(ConversationData dest)
	{
		dest.name = name;
		dest.dialog.clear();

		for (int i = 0; i < dialog.size(); i++)
		{
			DialogData dData = dialog.get(i);

			dest.dialog.add(new DialogData(dData));
		}
	}
}
