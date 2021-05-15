package greenpixel.gut.data;

public class EntityAnimationData
{
	public int upAnimGroup;
	public int upAnimIndex;
	public int downAnimGroup;
	public int downAnimIndex;
	public int leftAnimGroup;
	public int leftAnimIndex;
	public int rightAnimGroup;
	public int rightAnimIndex;
	public String name;

	public EntityAnimationData()
	{
		name = "";
		upAnimGroup = 0;
		upAnimIndex = 0;
		downAnimGroup = 0;
		downAnimIndex = 0;
		leftAnimGroup = 0;
		leftAnimIndex = 0;
		rightAnimGroup = 0;
		rightAnimIndex = 0;
	}

	public void copyFrom(EntityAnimationData aData)
	{
		name = new String(aData.name);
		upAnimGroup = aData.upAnimGroup;
		upAnimIndex = aData.upAnimIndex;
		downAnimGroup = aData.downAnimGroup;
		downAnimIndex = aData.downAnimIndex;
		leftAnimGroup = aData.leftAnimGroup;
		leftAnimIndex = aData.leftAnimIndex;
		rightAnimGroup = aData.rightAnimGroup;
		rightAnimIndex = aData.rightAnimIndex;
	}

	public String toString()
	{
		String returnString = name + " ";
		/*
		returnString += "\n" + upAnimGroup;
		returnString += "\n" + downAnimGroup;
		returnString += "\n" + leftAnimGroup;
		returnString += "\n" + rightAnimGroup;
		returnString += "\n" + upAnimIndex;
		returnString += "\n" + downAnimIndex;
		returnString += "\n" + leftAnimIndex;
		returnString += "\n" + rightAnimIndex;
		*/
		return returnString;
	}
}
