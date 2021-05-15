package greenpixel.gut.anim;

import java.util.ArrayList;
import java.util.Vector;

//each animation consists of a collection of frame data and a loopType
//the regular "type" isn't used any more.
//loopType can be a one shot, or infinite looping.
public class Animation 
{
	public int type;
	public int loopType;
	public String name;
	public ArrayList<FrameData> frameList;
	
	public Animation(String name)
	{
		this.name = name;
		frameList = new ArrayList<FrameData>();
		type = 0;
		loopType = 0;
	}
	
	public void addFrame(FrameData frame)
	{
		frameList.add(frame);
	}
	
	public void removeFrame(int position)
	{
		frameList.remove(position);
	}
	
	public String toString()
	{
		return name;
	}

	public Object[] getFrames() 
	{
		return frameList.toArray();
	}
}
