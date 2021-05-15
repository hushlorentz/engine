package greenpixel.game.anim;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.nio.ByteBuffer;

import greenpixel.gut.anim.Animation;
import greenpixel.gut.anim.FrameData;

//each animation bundle consists of a sprite sheet, 
//the bounding rectangles that make up the animation frames
//and all the associated animations.
public class AnimationBundle
{
	public String name;
	public Image spriteSheet;
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

	public void loadData(ByteBuffer data)
	{
		int width = data.getInt();
		int height = data.getInt();

		ImageBuffer buffer = new ImageBuffer(width, height);

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				byte a = data.get();
				byte r = data.get();
				byte g = data.get();
				byte b = data.get();
				buffer.setRGBA(i, j, r, g, b, a);
			}
		}

		spriteSheet = new Image(buffer, Image.FILTER_NEAREST);

		int numBounds = data.getInt();

		for (int i = 0; i < numBounds; i++)
		{
			Rectangle r = new Rectangle();
			r.x = data.getInt();
			r.y = data.getInt();
			r.width = data.getInt();
			r.height = data.getInt();

			bounds.add(r);
		}

		int numAnims = data.getInt();

		for (int i = 0; i < numAnims; i++)
		{
			Animation anim = new Animation("");

			anim.type = data.getInt();
			anim.loopType = data.getInt();

			int numFrames = data.getInt();

			for (int j = 0; j < numFrames; j++)
			{
				FrameData fd = new FrameData();

				fd.numDrawFrames = data.getInt();
				fd.frameIndex = data.getInt();
				fd.offsetX = data.getInt();
				fd.offsetY = data.getInt();
				fd.frameOp = data.getInt();
				fd.orientation = data.getInt();

				anim.addFrame(fd);
			}

			anims.add(anim);
		}
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

			System.out.println(buff.length);

			ImageBuffer buffer = new ImageBuffer(width, height);

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
					buffer.setRGBA(i, j, r, g, b, a);
				}
			}

			spriteSheet = new Image(buffer, Image.FILTER_NEAREST);

			if (path.equals("anim/a8.abin"))
			{
//			System.out.println(width + " " + height);
			}

			int numBounds = in.readInt();

			System.out.println(numBounds);

			for (int i = 0; i < numBounds; i++)
			{
				Rectangle r = new Rectangle();
				r.x = in.readInt();
				r.y = in.readInt();
				r.width = in.readInt();
				r.height = in.readInt();
			if (path.equals("anim/a8.abin"))
			{
//				System.out.println(r);
			}

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
			System.out.println("Fucker died: " + e.getMessage());
			System.exit(0);
		}
	}

	public String toString()
	{
		return name;
	}	
}
