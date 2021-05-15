package greenpixel.gut.anim;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

//each animation bundle consists of a sprite sheet, 
//the bounding rectangles that make up the animation frames
//and all the associated animations.
public class AnimationBundle
{
	public String name;
	public BufferedImage spriteSheet;
	public ArrayList<Rectangle> bounds;
	public ArrayList<Animation> anims;

	public static int LOOP = 0;
	public static int ONESHOT = 1;

	public AnimationBundle(String name)
	{
		this.name = name;
		bounds = new ArrayList<Rectangle>();
		anims = new ArrayList<Animation>();
	}

	public void loadData(String path)
	{
		try
		{
			System.out.println("loading animation: " + path);
			DataInputStream in = new DataInputStream(getClass().getClassLoader().getResourceAsStream(path));
			int width = in.readInt();
			int height = in.readInt();

			byte[] buff = new byte[width * height * 4];

			spriteSheet = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			int buffCount = 0;
			in.read(buff);

			for (int i = 0; i < width; i++)
			{
				for (int j = 0; j < height; j++)
				{
					byte a = buff[buffCount++];
					byte r = buff[buffCount++];
					byte g = buff[buffCount++];
					byte b = buff[buffCount++];
					spriteSheet.setRGB(i, j, r << 16 | g << 8 | b);
				}
			}

			int numBounds = in.readInt();

			for (int i = 0; i < numBounds; i++)
			{
				Rectangle r = new Rectangle();
				r.x = in.readInt();
				r.y = in.readInt();
				r.width = in.readInt();
				r.height = in.readInt();

				bounds.add(r);
			}

			int numAnims = in.readInt();

			for (int i = 0; i < numAnims; i++)
			{
				Animation anim = new Animation("");

				anim.type = in.readInt();
				anim.loopType = in.readInt();

				int numFrames = in.readInt();

				for (int j = 0; j < numFrames; j++)
				{
					FrameData fd = new FrameData();

					fd.numDrawFrames = in.readInt();
					fd.frameIndex = in.readInt();
					fd.offsetX = in.readInt();
					fd.offsetY = in.readInt();
					fd.frameOp = in.readInt();
					fd.orientation = in.readInt();

					anim.addFrame(fd);
				}

				anims.add(anim);
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	public String toString()
	{
		return name;
	}	
}
