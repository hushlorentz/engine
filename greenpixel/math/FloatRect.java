package greenpixel.math;

import java.awt.Rectangle;

public class FloatRect
{
	public float x;
	public float y;
	public float width;
	public float height;

	public FloatRect(float xPos, float yPos, float w, float h)
	{
		x = xPos;
		y = yPos;
		width = w;
		height = h;
	}

	public FloatRect(Rectangle r)
	{
		x = r.x;
		y = r.y;
		width = r.width;
		height = r.height;
	}

	public void init(FloatRect rect)
	{
		x = rect.x;
		y = rect.y;
		width = rect.width;
		height = rect.height;
	}

	public String toString()
	{
		return "Rect: [" + x + " " + ", " + y + ", " + width + ", " + height + "]";
	}
}
