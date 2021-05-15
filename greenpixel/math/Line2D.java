package greenpixel.math;

import greenpixel.math.Vector2D;

public class Line2D
{
	public Vector2D pointA;
	public Vector2D pointB;

	public Line2D(Vector2D a, Vector2D b)
	{
		pointA = a;
		pointB = b;
	}

	public Line2D(float aX, float aY, float bX, float bY)
	{
		pointA = new Vector2D(aX, aY);
		pointB = new Vector2D(bX, bY);
	}
}
