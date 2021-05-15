package greenpixel.game.world.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import greenpixel.game.world.objects.Entity;
import greenpixel.gut.data.TriggerData;

import greenpixel.math.FloatRect;

public class Trigger extends Entity
{
	public static final int TYPE_CONVERSATION = 0;
	public static final int TYPE_DOOR = 1;
	public static final int TYPE_CUTSCENE = 2;
	public static final int TYPE_QUEST = 3;
	public static final int TYPE_QUEST_OBJECTIVE = 4;
	public static final int TYPE_USER = 5;

	public boolean isTriggered;
	public int triggerType;
	public int triggerGroup;
	public int triggerIndex;
	public int lockType;
	public int lockGroup;
	public int lockIndex;

	public Trigger()
	{
		collisionBox = new FloatRect(0, 0, 0, 0);
		triggerType = 0;
		triggerIndex = 0;
		lockType = 0;
		lockGroup = 0;
		lockIndex = 0;
		isTriggered = false;
	}
	
	public void init(TriggerData tData, float xPos, float yPos)
	{
		super.init(xPos, yPos, tData.collisionBox, 0, false, 0);
		type = ENTITY_TYPE_TRIGGER;

		lockType = tData.lockType;
		lockGroup = tData.lockGroup;
		lockIndex = tData.lockIndex;
		triggerType = tData.triggerType;
		triggerGroup = tData.triggerGroup;
		triggerIndex = tData.triggerIndex;
		isCollideable = false;
		isVisible = false;
	}

	public void drawToBuffer(Graphics g, int xPos, int yPos)
	{
		if (debug)
		{
			g.setColor(Color.red);
			g.fillRect(xPos + collisionBox.x, yPos + collisionBox.y, collisionBox.width, collisionBox.height);
		}
	}

	public void reset()
	{
		isTriggered = false;
	}
}
