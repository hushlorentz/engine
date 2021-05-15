package greenpixel.math;

public class Vector2D
{
	public float x;
	public float y;

	public Vector2D(float xPos, float yPos)
	{
		x = xPos;
		y = yPos;
	}

	public Vector2D(Vector2D vec)
	{
		x = vec.x;
		y = vec.y;
	}

	public void init(float newX, float newY)
	{
		x = newX;
		y = newY;
	}

	public float getSquaredDistance()
	{
		return x * x + y * y;
	}

	public double getLength()
	{	
		return Math.sqrt(getSquaredDistance());
	}

	public void normalize()
	{
		double length = getLength();

		if (length != 0)
		{
			x /= length;
			y /= length;
		}
	}

	public void copy(Vector2D dest)
	{
		dest.x = x;
		dest.y = y;
	}

	public Vector2D getNormalVector()
	{
		return new Vector2D(-y, x);
	}

	public String toString()
	{
		return "<" + x +"," + y + ">";
	}

	public static Vector2D getRandomNormalizedVector()
	{
		double randomTheta = 2 * Math.PI * Math.random();

		return new Vector2D((float)Math.cos(randomTheta), (float)Math.sin(randomTheta));
	}
}
