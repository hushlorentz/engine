package greenpixel.gut.data;

public class DialogData
{
	public int animationGroup;
	public int animationIndex;
	public int textSpeed;
	public boolean useAnimation;
	public String text;

	public DialogData()
	{
		text = "";
		textSpeed = 0;
		animationGroup = 0;
		animationIndex = 0;
		useAnimation = false;
	}

	public DialogData(DialogData dData)
	{
		text = dData.text;
		textSpeed = dData.textSpeed;
		animationGroup = dData.animationGroup;
		animationIndex = dData.animationIndex;
		useAnimation = dData.useAnimation;
	}
}
