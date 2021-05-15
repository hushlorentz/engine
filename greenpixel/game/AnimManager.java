package greenpixel.game;

import greenpixel.game.GameBase;
import greenpixel.game.anim.AnimationBundle;

import greenpixel.gut.anim.Animation;
import greenpixel.gut.anim.DrawData;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.nio.ByteBuffer;

public class AnimManager
{
	public static AnimManager instance;
	public ArrayList<AnimationBundle> animBundles;

	private int fpsFactor;

	public AnimManager(int fps)
	{
		animBundles = new ArrayList<AnimationBundle>();

		switch (fps)
		{
			case GameBase.FPS_30:
				fpsFactor = 1;
				break;
			case GameBase.FPS_60:
				fpsFactor = 2;
				break;
		}
	}

	public void loadAnimData(String path)
	{
		animBundles.clear();

		try
		{
//			DataInputStream in = new DataInputStream(getClass().getClassLoader().getResourceAsStream(path));
			DataInputStream in = new DataInputStream(new FileInputStream(path));

			int numBundles = in.readInt();

			System.out.println("numAnimBundles: " + numBundles);

			for (int i = 0; i < numBundles; i++)
			{
				int dataSize = in.readInt();

				System.out.println(dataSize);

				byte[] animData = new byte[dataSize];
				ByteBuffer bufferData = ByteBuffer.allocate(dataSize);

				in.read(animData, 0, dataSize);
				bufferData.put(animData);
				bufferData.rewind();

				AnimationBundle bundle = new AnimationBundle("");
				bundle.loadData(bufferData);

				animBundles.add(bundle);
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	public void addBundle(String path)
	{
		AnimationBundle	bundle = new AnimationBundle("");
		
		bundle.loadData(path);

		animBundles.add(bundle);
	}

	public DrawData getDrawData(int animGroup, int animIndex, int frame)
	{
		DrawData drawData = new DrawData();

		AnimationBundle animBundle = animBundles.get(animGroup);

		ArrayList<Animation> animData = animBundle.anims;

		int boundsIndex = animData.get(animIndex).frameList.get(frame).frameIndex;
		int imageOffsetX = animData.get(animIndex).frameList.get(frame).offsetX;
		int imageOffsetY = animData.get(animIndex).frameList.get(frame).offsetY;
		int loopType = animData.get(animIndex).loopType;
		int totalFrames = animData.get(animIndex).frameList.size();
		int numDrawFrames = animData.get(animIndex).frameList.get(frame).numDrawFrames * fpsFactor;
		int frameOp = animData.get(animIndex).frameList.get(frame).frameOp;

		drawData.init(animBundle.spriteSheet, animBundle.bounds.get(boundsIndex), imageOffsetX, imageOffsetY, loopType, totalFrames, numDrawFrames, frameOp);

		return drawData;
	}
}
