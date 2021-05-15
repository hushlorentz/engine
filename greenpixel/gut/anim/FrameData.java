package greenpixel.gut.anim;

public class FrameData
{
	public static final int OP_NONE = 0;
	public static final int OP_ATTACK = 1;

	public static final int OR_NONE = 0;
	public static final int OR_CENTER_X = 1;
	public static final int OR_CENTER_Y = 2;
	public static final int OR_CENTER_BOTH = 3;

	public int numDrawFrames;
	public int frameIndex;
	public int offsetX;
	public int offsetY;
	public int frameOp;
	public int orientation;

	public FrameData()
	{
		numDrawFrames = 0;
		frameIndex = 0;
		offsetX = 0;
		offsetY = 0;
		frameOp = OP_NONE;
		orientation = OR_NONE;
	}

	public FrameData(FrameData data)
	{
		numDrawFrames = data.numDrawFrames;
		frameIndex = data.frameIndex;
		offsetX = data.offsetX;
		offsetY = data.offsetY;
		frameOp = data.frameOp;
		orientation = data.orientation;
	}

	public String toString()
	{
		String frameString = frameIndex + " X " + numDrawFrames;

		if (frameOp != OP_NONE)
		{
			frameString += " *";
		}

		return frameString;
	}
}
