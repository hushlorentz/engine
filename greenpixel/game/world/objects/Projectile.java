package greenpixel.game.world.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import greenpixel.game.world.objects.Entity;

import greenpixel.math.Collisions;
import greenpixel.math.FloatRect;
import greenpixel.math.Line2D;
import greenpixel.math.Vector2D;

public class Projectile extends Entity
{
	public final static int STATE_IDLE = 0;
	public final static int STATE_EXPLODING = 1;
	public final static int STATE_EXPLODED = 2;
	public Line2D trajectory;

	private int idleAnimGroup;
	private int idleAnimIndex;
	private int explosionAnimGroup;
	private int explosionAnimIndex;

	public Projectile()
	{
		super();

		idleAnimGroup = 0;
		idleAnimIndex = 0;
		explosionAnimGroup = 14;
		explosionAnimIndex = 21;
		collisionBox = new FloatRect(0, 0, 32, 32);
		trajectory = new Line2D(0, 0, 0, 0);
	}

	public void setup(float xPos, float yPos, float theta)
	{
		setPos(xPos, yPos);

		setState(STATE_IDLE);
		heading = new Vector2D((float)Math.cos(theta), (float)Math.sin(theta));
		setAnim(idleAnimGroup, idleAnimIndex);
	}

	public void updatePos(float newX, float newY)
	{
		trajectory.pointA.x = x + collisionBox.x + collisionBox.width / 2;
		trajectory.pointA.y = y + collisionBox.y + collisionBox.height / 2;
		trajectory.pointB.x = newX + collisionBox.x + collisionBox.width / 2;
		trajectory.pointB.y = newY + collisionBox.y + collisionBox.height / 2;

		x = newX;
		y = newY;
	}

	public boolean collidesWithEntity(Entity e)
	{
		if (e.isFriendly == isFriendly)
		{
			return false;
		}

		if (Collisions.rectInRect(getCollisionRect(), e.getCollisionRect()) || Collisions.lineInAABB(trajectory, e.getCollisionRect()))
		{
			return true;
		}

		return false;
	}

	public void explode()
	{
		if (getState() != STATE_IDLE)
		{
			return;
		}

		setState(STATE_EXPLODING);
		setAnim(explosionAnimGroup, explosionAnimIndex);
	}

	public void drawToBuffer(Graphics g, int xPos, int yPos)
	{
		super.drawToBuffer(g, xPos, yPos);

		if (debug)
		{
			g.setColor(Color.blue);
			g.fillRect(xPos, yPos, collisionBox.width, collisionBox.height);

			g.setColor(Color.red);
			g.setLineWidth(10);
			g.drawLine(trajectory.pointA.x - (x - xPos), trajectory.pointA.y - (y - yPos), trajectory.pointB.x - (x - xPos), trajectory.pointB.y - (y - yPos));
		}
	}

	public void nextFrame()
	{
		super.nextFrame();

		if (getState() == STATE_EXPLODING && isAnimFinished)
		{
			setState(STATE_EXPLODED);
		}
	}
}
