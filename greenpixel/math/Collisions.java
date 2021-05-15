package greenpixel.math;

import java.util.ArrayList;

import greenpixel.math.FloatRect;
import greenpixel.math.Vector2D;

public class Collisions
{
	public static boolean rectInRect(FloatRect r1, FloatRect r2)
	{
		return boxInBox(r1.x, r1.y, r1.width, r1.height, r2.x, r2.y, r2.width, r2.height);
	}

	public static boolean boxInBox(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2)
	{
		return !((x1 >= x2 + w2) || (x2 >= x1 + w1) || (y1 >= y2 + h2) || (y2 >= y1 + h1)); 
	}

	public static boolean pointInBox(float xPoint, float yPoint, float xBox, float yBox, float width, float height)
	{
		return (xPoint >= xBox && xPoint <= (xBox + width) && yPoint >= yBox && yPoint <= (yBox + height));
	}

	public static boolean pointInCircle(float xPoint, float yPoint, float xCircle, float yCircle, float radius)
	{
		Vector2D dist = new Vector2D(xPoint - xCircle, yPoint - yCircle);
		return dist.getSquaredDistance() <= radius * radius;
	}

	//compares two lines, A and B, and returns true if they intersect
	//uses the separating axis theorum
	public static boolean lineIntersectsLine(float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2)
	{
		int i;
		ArrayList<Vector2D> axes = new ArrayList<Vector2D>();

		Vector2D a = new Vector2D(ax2 - ax1, ay2 - ay1);
		Vector2D b = new Vector2D(bx2 - bx1, by2 - by1);

		a.normalize();
		b.normalize();

		axes.add(a.getNormalVector());
		axes.add(b.getNormalVector());

		for (i = 0; i < axes.size(); i++)
		{
			float minA = Float.MAX_VALUE;
			float maxA = Float.MIN_VALUE;
			float minB = Float.MAX_VALUE;
			float maxB = Float.MIN_VALUE;

			float lineProj1 = dotProduct(axes.get(i), new Vector2D(ax1, ay1));
			float lineProj2 = dotProduct(axes.get(i), new Vector2D(ax2, ay2));

			if (lineProj1 < lineProj2)
			{
				minA = lineProj1;
				maxA = lineProj2;
			}
			else
			{
				minA = lineProj2;
				maxA = lineProj1;
			}

			float lineProj3 = dotProduct(axes.get(i), new Vector2D(bx1, by1));
			float lineProj4 = dotProduct(axes.get(i), new Vector2D(bx2, by2));

			if (lineProj3 < lineProj4)
			{
				minB = lineProj3;
				maxB = lineProj4;
			}
			else
			{
				minB = lineProj4;
				maxB = lineProj3;
			}

			if (minA > maxB || maxA < minB) //does not intersect
			{
				return false;
			}
		}

		return true;
	}

	public static boolean lineInAABB(Line2D line, FloatRect rect)
	{
		return lineInBox(line.pointA.x, line.pointA.y, line.pointB.x, line.pointB.y, rect.x, rect.y, rect.width, rect.height);
	}

	//compares a line and a box and returns true if they intersect
	//uses the separating axis theorum
	public static boolean lineInBox(float lineStartX, float lineStartY, float lineEndX, float lineEndY, float xBox, float yBox, float width, float height)
	{
		int i;
		int j;
		ArrayList<Vector2D> axes = new ArrayList<Vector2D>();
		ArrayList<Vector2D> testVecs = new ArrayList<Vector2D>();

		Vector2D vec = new Vector2D(lineEndX - lineStartX, lineEndY - lineStartY);
		vec.normalize();

		axes.add(vec);
		axes.add(new Vector2D(1, 0));
		axes.add(new Vector2D(0, 1));

		testVecs.add(new Vector2D(xBox, yBox));
		testVecs.add(new Vector2D(xBox + width, yBox + height));

		for (i = 0; i < axes.size(); i++)
		{
			float minLine;
			float maxLine;
			float minBox = Float.MAX_VALUE;
			float maxBox = -Float.MAX_VALUE;

			float lineProj1 = dotProduct(axes.get(i), new Vector2D(lineStartX, lineStartY));
			float lineProj2 = dotProduct(axes.get(i), new Vector2D(lineEndX, lineEndY));

			if (lineProj1 < lineProj2)
			{
				minLine = lineProj1;
				maxLine = lineProj2;
			}
			else
			{
				minLine = lineProj2;
				maxLine = lineProj1;
			}

			for (j = 0; j < testVecs.size(); j++)
			{
				float boxProj = dotProduct(axes.get(i), testVecs.get(j));

				if (boxProj < minBox)
				{
					minBox = boxProj;
				}
				if (boxProj > maxBox)
				{
					maxBox = boxProj;
				}
			}

			if (minLine > maxBox || maxLine < minBox) //does not intersect
			{
				return false;
			}
		}

		return true;
	}

	public static float dotProduct(Vector2D v1, Vector2D v2)
	{
		return (v1.x * v2.x + v1.y * v2.y);
	}
}
