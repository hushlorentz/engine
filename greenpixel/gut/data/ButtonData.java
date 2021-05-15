package greenpixel.gut.data;

import greenpixel.math.FloatRect;

public class ButtonData
{
	public int upAnimGroup;
	public int upAnimIndex;
	public int downAnimGroup;
	public int downAnimIndex;
	public int selectedAnimGroup;
	public int selectedAnimIndex;
	public FloatRect collisionBox;
	public FloatRect textBox;
	public String text;
	public String name;

	public ButtonData()
	{
		upAnimGroup = 0;
		upAnimIndex = 0;
		downAnimGroup = 0;
		downAnimIndex = 0;
		selectedAnimGroup = 0;
		selectedAnimIndex = 0;

		collisionBox = new FloatRect(0, 0, 0, 0);
		textBox = new FloatRect(0, 0, 0, 0);
		name = "";
		text = "";
	}

	public String toString()
	{
		return name;
	}
}
