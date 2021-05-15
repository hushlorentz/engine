package greenpixel.game.sprite;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import greenpixel.game.AnimManager;
import greenpixel.gut.anim.AnimationBundle;
import greenpixel.gut.anim.DrawData;
import greenpixel.gut.anim.FrameData;
import greenpixel.math.FloatRect;

public class Sprite2D implements Comparable<Sprite2D>
{
	public static final int BOTTOM = 0;
	public static final int MIDDLE = 1;
	public static final int TOP = 2;

	public float x;
	public float y;
	public FloatRect collisionBox;
	public boolean isAnimFinished;
	public boolean isVisible;
	public int zPos;

	protected DrawData drawData; //contains everything needed to draw the sprite
	protected boolean debug;
	protected int currentAnimBundle; //selected animation group
	protected int currentAnimIndex; //current animation in the group
	protected int currentFrame; //current frame of the animation
	protected int frameCounter; //a timer to determine when to switch frames
	protected int frameOpStatus; //frame operation flag for things like attacking on a certain frame
	protected boolean imgLoaded;

	public Sprite2D()
	{
		currentAnimBundle = -1;
		currentAnimIndex = -1;
		debug = false;
		imgLoaded = false;
		isVisible = false;
		collisionBox = new FloatRect(0, 0, 0, 0);
		zPos = MIDDLE;
	}

	public void setPos(float xPos, float yPos)
	{
		x = xPos;
		y = yPos;
	}

	public void resetAnim()
	{
		currentFrame = 0;
		frameCounter = 0;

		setDrawData();

		isAnimFinished = false;
		imgLoaded = true;
		isVisible = true;
	}

	public void setAnim(int animBundle, int animIndex)
	{
		if (currentAnimBundle == animBundle && currentAnimIndex == animIndex)
		{
			return;
		}

		currentAnimBundle = animBundle;
		currentAnimIndex = animIndex;

		currentFrame = 0;
		frameCounter = 0;

		setDrawData();

		isAnimFinished = false;
		imgLoaded = true;
		isVisible = true;
	}

	protected void setDrawData()
	{
		drawData = AnimManager.instance.getDrawData(currentAnimBundle, currentAnimIndex, currentFrame);
		frameOpStatus = drawData.frameOp;
	}

	public FloatRect getCollisionRect()
	{
		return new FloatRect(x + collisionBox.x, y + collisionBox.y, collisionBox.width, collisionBox.height);
	}	

	public void drawToBuffer(Graphics g, int xPos, int yPos)
	{
		if (imgLoaded && isVisible)
		{
			drawData.img.draw(xPos + drawData.imageOffsetX, yPos + drawData.imageOffsetY, xPos + drawData.imageOffsetX + drawData.bounds.width, yPos + drawData.imageOffsetY + drawData.bounds.height, drawData.bounds.x, drawData.bounds.y, drawData.bounds.x + drawData.bounds.width, drawData.bounds.y + drawData.bounds.height);
		}

		if (debug && isVisible)
		{
			g.setColor(Color.black);
			g.fillRect(xPos + collisionBox.x, yPos + collisionBox.y, collisionBox.width, collisionBox.height);
		}
	}

	public void nextFrame()
	{
		if (!imgLoaded || !isVisible)
		{
			return;
		}

		if (frameCounter == drawData.numDrawFrames)
		{
			frameCounter = 0;

			if (currentFrame  < (drawData.totalFrames - 1))
			{
				currentFrame++;
			}
			else
			{
				if (drawData.loopType == AnimationBundle.ONESHOT)
				{
					isAnimFinished = true;
				}
				else
				{
					currentFrame = 0;
				}
			}

			if (!isAnimFinished)
			{
				setDrawData();
			}
		}
		else
		{
			frameCounter++;
		}
	}

	public int checkFrameOps()
	{
		int frameOp = frameOpStatus;

		frameOpStatus = FrameData.OP_NONE;

		return frameOp;
	}

	public void setDebug(boolean dbg)
	{
		debug = dbg;
	}

	public int compareTo(Sprite2D e)
	{
		return (int)((y + collisionBox.y) - (e.y + e.collisionBox.y));
	}
}
