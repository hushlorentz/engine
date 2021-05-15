package greenpixel.gut.anim;

import org.newdawn.slick.Image;

import java.awt.Rectangle;

public class DrawData
{
	public Image img;
	public Rectangle bounds;
	public int imageOffsetX;
	public int imageOffsetY;
	public int loopType;
	public int totalFrames; //total frames in the animation
	public int numDrawFrames; //number of times to draw the current frame
	public int frameOp; //frame operation

	public DrawData()
	{
	}

	public void init(Image i, Rectangle b, int oX, int oY, int l, int tF, int nDF, int fO)
	{
		img = i;
		bounds = b;
		imageOffsetX = oX;
		imageOffsetY = oY;
		loopType = l;
		totalFrames = tF;
		numDrawFrames = nDF;
		frameOp = fO;
	}
}
