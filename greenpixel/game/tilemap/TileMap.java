package greenpixel.game.tilemap;

import greenpixel.game.DataManager;
import greenpixel.game.world.objects.Barrier;
import greenpixel.game.world.objects.BreakableEntity;
import greenpixel.game.world.objects.Container;
import greenpixel.game.world.objects.Entity;
import greenpixel.game.world.objects.Trigger;

import greenpixel.game.tilemap.Tile;
import greenpixel.math.Vector2D;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TileMap
{
	public int mapWidth;
	public int mapHeight;
	public int tileSize;
	public ArrayList<Trigger> onLoadTriggerArray;

	private int northLevel;
	private int eastLevel;
	private int southLevel;
	private int westLevel;
	private int music;
	private int screenWidth;
	private int screenHeight;
	private int drawTileWidth;
	private int drawTileHeight;
	private int drawTileStartX;
	private int drawTileStartY;
	private int scrollOffsetX;
	private int scrollOffsetY;
	private int animNum;
	private float nudgeThreshold;
	private long[][] map;
	private Image tileImage;
	private Vector2D mainSpriteDrawPos;

	private ArrayList<Barrier> persistentBarrierArray;
	private ArrayList<BreakableEntity> persistentBreakableArray;
	private ArrayList<Container> persistentContainerArray;
	private ArrayList<Trigger> persistentTriggerArray;

	public TileMap(int scrWidth, int scrHeight)
	{
		screenWidth = scrWidth;
		screenHeight = scrHeight;
		drawTileStartX = 0;
		drawTileStartY = 0;
		mapWidth = 0;
		mapHeight = 0;
		drawTileWidth = 0;
		drawTileHeight = 0;
		scrollOffsetX = 0;
		scrollOffsetY = 0;
		animNum = 0;
		nudgeThreshold = 0;
		mainSpriteDrawPos = new Vector2D(0, 0);

		persistentBarrierArray = new ArrayList<Barrier>();
		persistentBreakableArray = new ArrayList<BreakableEntity>();
		persistentContainerArray = new ArrayList<Container>();
		persistentTriggerArray = new ArrayList<Trigger>();
		onLoadTriggerArray = new ArrayList<Trigger>();
	}

	public void loadMap(ByteBuffer buff)
	{
		drawTileStartX = 0;
		drawTileStartY = 0;
		mapWidth = 0;
		mapHeight = 0;
		drawTileWidth = 0;
		drawTileHeight = 0;
		scrollOffsetX = 0;
		scrollOffsetY = 0;
		animNum = 0;

		music = buff.getInt();
		northLevel = buff.getInt();
		eastLevel = buff.getInt();
		southLevel = buff.getInt();
		westLevel = buff.getInt();

		int numTriggers = buff.getInt();

		for (int i = 0; i < numTriggers; i++)
		{
			Trigger trig = new Trigger();
			int index = buff.getInt();
			
			trig.init(DataManager.instance.getTrigger(index), -256, -256);
			trig.setMapLocation(-1, -1);
			onLoadTriggerArray.add(trig);
		}

		int imageWidth = buff.getInt();
		int imageHeight = buff.getInt();

		System.out.println("\tmap image width: " + imageWidth);
		System.out.println("\tmap image height: " + imageHeight);

		ImageBuffer iData = new ImageBuffer(imageWidth, imageHeight);

		for (int i = 0; i < imageHeight; i++)
		{
			for (int j = 0; j < imageWidth; j++)
			{
				int color = buff.getInt();

				iData.setRGBA(j, i, (byte)(color >> 16), (byte)(color >> 8), (byte)(color), (byte)(color >> 24));
			}
		}

		tileImage = iData.getImage(Image.FILTER_NEAREST).getScaledCopy(2); //change back

		mapWidth = buff.getInt();
		mapHeight = buff.getInt();
		tileSize = buff.getInt() * 2; //change back

		System.out.println("\tmap map width: " + mapWidth);
		System.out.println("\tmap map height: " + mapHeight);
		System.out.println("\tmap tileSize: " + tileSize);

		drawTileWidth = screenWidth / tileSize;
		drawTileHeight = screenHeight / tileSize;
		nudgeThreshold = tileSize / 2;

		map = new long[mapHeight][mapWidth];

		for (int i = 0; i < mapHeight; i++)
		{
			for (int j = 0; j < mapWidth; j++)
			{
				long tile = map[i][j] = buff.getLong();

				if (Tile.getSpecialType(tile) == Tile.SPECIAL_CONTAINER)
				{
					Container con = new Container();
					con.init(DataManager.instance.getContainer(Tile.getSpecialValue(tile)), j * tileSize, i * tileSize, Tile.getSpecialValue(tile));
					con.setMapLocation(j, i);
					persistentContainerArray.add(con);
				}

				if (Tile.getSpecialType(tile) == Tile.SPECIAL_BREAKABLE)
				{
					BreakableEntity be = new BreakableEntity();
					be.init(DataManager.instance.getBreakableEntity(Tile.getSpecialValue(tile)), j * tileSize, i * tileSize, Tile.getSpecialValue(tile));
					be.setMapLocation(j, i);
					persistentBreakableArray.add(be);
				}

				if (Tile.getSpecialType(tile) == Tile.SPECIAL_TRIGGER)
				{
					Trigger trig = new Trigger();
					trig.init(DataManager.instance.getTrigger(Tile.getSpecialValue(tile)), j * tileSize, i * tileSize);
					trig.setMapLocation(j, i);
					persistentTriggerArray.add(trig);
				}

				if (Tile.getSpecialType(tile) == Tile.SPECIAL_BARRIER)
				{
					Barrier barrier = new Barrier();
					barrier.init(DataManager.instance.getBarrier(Tile.getSpecialValue(tile)), j * tileSize, i * tileSize);
					barrier.setMapLocation(j, i);
					persistentBarrierArray.add(barrier);
				}
			}
		}
	}

	public void centerMap(float xPos, float yPos)
	{
		int tileX = (int)(xPos / tileSize);
		int tileY = (int)(yPos / tileSize);
		int diffX = tileX - (drawTileWidth >> 1);
		int diffY = tileY - (drawTileHeight >> 1);
		mainSpriteDrawPos.x = xPos;
		mainSpriteDrawPos.y = yPos;

		drawTileStartX = 0;
		drawTileStartY = 0;
		scrollOffsetX = 0;
		scrollOffsetY = 0;

		//Add one, otherwise we don't know if the
		//screen should be scrolled when the diff is [0..1]
		//it just gets rounded down to 0
		if ((diffX + 1) > 0 && (mapWidth > drawTileWidth))
		{
			drawTileStartX = diffX;
			mainSpriteDrawPos.x = (drawTileWidth >> 1) * tileSize;

			if (drawTileStartX + drawTileWidth >= mapWidth)
			{
				drawTileStartX = mapWidth - drawTileWidth;
				mainSpriteDrawPos.x = xPos - (mapWidth - drawTileWidth) * tileSize;
			}
			else 
			{
				scrollOffsetX -= xPos % tileSize;
			}
		}
		//same as above
		if ((diffY + 1) > 0 && (mapHeight > drawTileHeight))
		{
			drawTileStartY = diffY;
			mainSpriteDrawPos.y = (drawTileHeight >> 1) * tileSize;

			if (drawTileStartY + drawTileHeight >= mapHeight)
			{
				drawTileStartY = mapHeight - drawTileHeight;
				mainSpriteDrawPos.y = yPos - (mapHeight - drawTileHeight) * tileSize;
			}
			else 
			{
				scrollOffsetY -= yPos % tileSize;
			}
		}
	}

	public Vector2D moveEntityUp(Entity sprite, float dist, boolean doNudge)
	{
		int tileX = (int)((sprite.x + sprite.collisionBox.x) / tileSize);
		int tileY = (int)(((sprite.y + sprite.collisionBox.y) - dist) / tileSize);
		float nudgeX = 0;
		float overlapRightX = 0; //distance tileX is offset from the sprite on right side
		float overlapLeftX = 0; //distance tileX is offset from the sprite on left side

		if ((sprite.y + sprite.collisionBox.y) - dist < 0)
		{
			sprite.y = -sprite.collisionBox.y;
			return new Vector2D(0, 0);
		}

		overlapRightX = sprite.x + sprite.collisionBox.x - (tileX * tileSize);
		overlapLeftX = sprite.x + sprite.collisionBox.x + sprite.collisionBox.width - ((tileX + 1) * tileSize);

		if (doNudge && overlapLeftX > 0 && (overlapLeftX <= nudgeThreshold) && (tileX + 1 < mapWidth) && Tile.hasWall(map[tileY][tileX + 1]) && !Tile.hasWall(map[tileY][tileX]))
		{
			sprite.y = (tileY + 1) * tileSize - sprite.collisionBox.y;

			if (!sprite.hasMoveDirection(Entity.DIR_RIGHT))
			{
				nudgeX = -Math.min(dist, overlapLeftX);
			}

			return new Vector2D(nudgeX, 0);
		}
		else if (doNudge && overlapRightX > tileSize - nudgeThreshold && (tileX + 1 < mapWidth) && Tile.hasWall(map[tileY][tileX]) && !Tile.hasWall(map[tileY][tileX + 1]))
		{
			sprite.y = (tileY + 1) * tileSize - sprite.collisionBox.y;

			if (!sprite.hasMoveDirection(Entity.DIR_LEFT))
			{
				nudgeX = Math.min(dist, tileSize - overlapRightX);
			}

			return new Vector2D(nudgeX, 0);
		}
		else if (Tile.hasWall(map[tileY][tileX]) || ((tileX + 1 < mapWidth) && (sprite.x + sprite.collisionBox.x + sprite.collisionBox.width > (tileX + 1) * tileSize) && Tile.hasWall(map[tileY][tileX + 1])))
		{
			sprite.ySpeed = 0;
			sprite.y = (tileY + 1) * tileSize - sprite.collisionBox.y;
			return new Vector2D(0, 0);
		}

		return new Vector2D(0, -dist);
	}

	public Vector2D moveEntityDown(Entity sprite, float dist, boolean doNudge)
	{
		int tileX = (int)((sprite.x + sprite.collisionBox.x) / tileSize);
		int tileY = (int)((sprite.y + sprite.collisionBox.y + sprite.collisionBox.height + dist) / tileSize);
		float nudgeX = 0;
		float overlapRightX = 0;
		float overlapLeftX = 0; 

		if (sprite.y + sprite.collisionBox.y + sprite.collisionBox.height + dist >= mapHeight * tileSize)
		{
			sprite.y = mapHeight * tileSize - (sprite.collisionBox.y + sprite.collisionBox.height);
			return new Vector2D(0, 0);
		}

		overlapRightX = sprite.x + sprite.collisionBox.x - (tileX * tileSize);
		overlapLeftX = sprite.x + sprite.collisionBox.x + sprite.collisionBox.width - ((tileX + 1) * tileSize);

		if (doNudge && overlapLeftX > 0 && (overlapLeftX <= nudgeThreshold) && (tileX + 1 < mapWidth) && Tile.hasWall(map[tileY][tileX + 1]) && !Tile.hasWall(map[tileY][tileX]))
		{
			sprite.y = tileY * tileSize - (sprite.collisionBox.y + sprite.collisionBox.height);

			if (!sprite.hasMoveDirection(Entity.DIR_RIGHT))
			{
				nudgeX = -Math.min(dist, overlapLeftX);
			}

			return new Vector2D(nudgeX, 0);
		}
		else if (doNudge && overlapRightX > tileSize - nudgeThreshold && (tileX + 1 < mapWidth) && Tile.hasWall(map[tileY][tileX]) && !Tile.hasWall(map[tileY][tileX + 1]))
		{
			sprite.y = tileY * tileSize - (sprite.collisionBox.y + sprite.collisionBox.height);

			if (!sprite.hasMoveDirection(Entity.DIR_LEFT))
			{
				nudgeX = Math.min(dist, tileSize - overlapRightX);
			}

			return new Vector2D(nudgeX, 0);
		}
		else if (Tile.hasWall(map[tileY][tileX]) || ((tileX + 1 < mapWidth) && (sprite.x + sprite.collisionBox.x + sprite.collisionBox.width > (tileX + 1) * tileSize) && Tile.hasWall(map[tileY][tileX + 1])))
		{
			sprite.ySpeed = 0;
			sprite.y = tileY * tileSize - (sprite.collisionBox.y + sprite.collisionBox.height);
			return new Vector2D(0, 0);
		}

		return new Vector2D(0, dist);
	}

	public Vector2D moveEntityLeft(Entity sprite, float dist, boolean doNudge)
	{
		int tileX = (int)((sprite.x + sprite.collisionBox.x - dist) / tileSize);
		int tileY = (int)((sprite.y + sprite.collisionBox.y) / tileSize);
		float nudgeY = 0;
		float overlapUpY = 0; //distance tileX is offset from the sprite on up side
		float overlapDownY = 0; //distance tileX is offset from the sprite on down side

		if (sprite.x + sprite.collisionBox.x - dist < 0)
		{
			sprite.x = -sprite.collisionBox.x;
			return new Vector2D(0, 0);
		}

		overlapUpY = sprite.y + sprite.collisionBox.y - (tileY * tileSize);
		overlapDownY = sprite.y + sprite.collisionBox.y + sprite.collisionBox.height - ((tileY + 1) * tileSize);

		if (doNudge && overlapDownY > 0 && (overlapDownY <= nudgeThreshold) && (tileY + 1 < mapHeight) && Tile.hasWall(map[tileY + 1][tileX]) && !Tile.hasWall(map[tileY][tileX]))
		{
			sprite.x = (tileX + 1) * tileSize - sprite.collisionBox.x;

			if (!sprite.hasMoveDirection(Entity.DIR_DOWN))
			{
				nudgeY = -Math.min(dist, overlapDownY);
			}

			return new Vector2D(0, nudgeY);
		}
		else if (doNudge && overlapUpY > (tileSize - nudgeThreshold) && (tileY + 1 < mapHeight) && Tile.hasWall(map[tileY][tileX]) && !Tile.hasWall(map[tileY + 1][tileX]))
		{
			sprite.x = (tileX + 1) * tileSize - sprite.collisionBox.x;

			if (!sprite.hasMoveDirection(Entity.DIR_UP))
			{
				nudgeY = Math.min(dist, tileSize - overlapUpY);
			}

			return new Vector2D(0, nudgeY);
		}
		else if (Tile.hasWall(map[tileY][tileX]) || ((tileY + 1 < mapHeight) && (sprite.y + sprite.collisionBox.y + sprite.collisionBox.height > (tileY + 1) * tileSize) && Tile.hasWall(map[tileY + 1][tileX])))
		{
			sprite.xSpeed = 0;
			sprite.x = (tileX + 1) * tileSize - sprite.collisionBox.x;
			return new Vector2D(0, 0);
		}

		return new Vector2D(-dist, 0);
	}

	public Vector2D moveEntityRight(Entity sprite, float dist, boolean doNudge)
	{
		int tileX = (int)((sprite.x + sprite.collisionBox.x + sprite.collisionBox.width + dist) / tileSize);
		int tileY = (int)((sprite.y + sprite.collisionBox.y) / tileSize);
		float nudgeY = 0;
		float overlapUpY = 0;
		float overlapDownY = 0;

		if (sprite.x + sprite.collisionBox.x + sprite.collisionBox.width + dist >= mapWidth * tileSize)
		{
			sprite.x = mapWidth * tileSize - (sprite.collisionBox.x + sprite.collisionBox.width);
			return new Vector2D(0, 0);
		}

		overlapUpY = sprite.y + sprite.collisionBox.y - (tileY * tileSize);
		overlapDownY = sprite.y + sprite.collisionBox.y + sprite.collisionBox.height - ((tileY + 1) * tileSize);

		if (doNudge && overlapDownY > 0 && (overlapDownY <= nudgeThreshold) && (tileY + 1 < mapHeight) && Tile.hasWall(map[tileY + 1][tileX]) && !Tile.hasWall(map[tileY][tileX]))
		{
			sprite.x = tileX * tileSize - (sprite.collisionBox.x + sprite.collisionBox.width);

			if (!sprite.hasMoveDirection(Entity.DIR_DOWN))
			{
				nudgeY = Math.min(dist, overlapDownY);
			}

			return new Vector2D(0, nudgeY);
		}
		else if (doNudge && overlapUpY > (tileSize - nudgeThreshold) && (tileY + 1 < mapHeight) && Tile.hasWall(map[tileY][tileX]) && !Tile.hasWall(map[tileY + 1][tileX]))
		{
			sprite.x = tileX * tileSize - (sprite.collisionBox.x + sprite.collisionBox.width);

			if (!sprite.hasMoveDirection(Entity.DIR_UP))
			{
				nudgeY = -Math.min(dist, tileSize - overlapUpY);
			}

			return new Vector2D(0, nudgeY);
		}
		else if (Tile.hasWall(map[tileY][tileX]) || ((tileY + 1 < mapHeight) && (sprite.y + sprite.collisionBox.y + sprite.collisionBox.height > (tileY + 1) * tileSize) && Tile.hasWall(map[tileY + 1][tileX])))
		{
			sprite.xSpeed = 0;
			sprite.x = tileX * tileSize - (sprite.collisionBox.x + sprite.collisionBox.width);
			return new Vector2D(0, 0);
		}

		return new Vector2D(dist, 0);
	}

	public void drawToBuffer(Graphics g)
	{
		for (int i = 0; i <= drawTileHeight; i++)
		{
			for (int j = 0; j <= drawTileWidth; j++)
			{
				if (i + drawTileStartY >= mapHeight || j + drawTileStartX >= mapWidth)
				{
					continue;
				}

				long tile = map[i + drawTileStartY][j + drawTileStartX];
				byte imgX = Tile.getImageX(tile);
				byte imgY = Tile.getImageY(tile);

				tileImage.draw(j * tileSize + scrollOffsetX, i * tileSize + scrollOffsetY, j * tileSize + scrollOffsetX + tileSize, i * tileSize + scrollOffsetY + tileSize, imgX * tileSize, imgY * tileSize, (imgX + 1) * tileSize, (imgY + 1) * tileSize);
			}
		}
	}

	public void nextFrame()
	{

	}

	public Vector2D getMapOffset()
	{
		return new Vector2D(tileSize * drawTileStartX - scrollOffsetX, tileSize * drawTileStartY - scrollOffsetY);
	}

	public Vector2D getMainSpriteDrawPos()
	{
		return mainSpriteDrawPos;
	}

	public Container getContainer(int tileX, int tileY)
	{
		for (int i = 0; i < persistentContainerArray.size(); i++)
		{
			Container con = persistentContainerArray.get(i);

			if (con.xTile == tileX && con.yTile == tileY)
			{
				return con;
			}
		}

		return null;
	}

	public void resetContainers()
	{
		for (int i = 0; i < persistentContainerArray.size(); i++)
		{
			Container con = persistentContainerArray.get(i);
			con.reset();
		}
	}

	public BreakableEntity getBreakableEntity(int tileX, int tileY)
	{
		for (int i = 0; i < persistentBreakableArray.size(); i++)
		{
			BreakableEntity be = persistentBreakableArray.get(i);

			if (be.xTile == tileX && be.yTile == tileY)
			{
				return be;
			}
		}

		return null;
	}

	public Barrier getBarrier(int tileX, int tileY)
	{
		for (int i = 0; i < persistentBarrierArray.size(); i++)
		{
			Barrier b = persistentBarrierArray.get(i);

			if (b.xTile == tileX && b.yTile == tileY)
			{
				return b;
			}
		}

		return null;
	}

	public void resetBreakableEntities()
	{
		for (int i = 0; i < persistentBreakableArray.size(); i++)
		{
			BreakableEntity be = persistentBreakableArray.get(i);
			be.reset();
		}
	}

	public Trigger getTrigger(int tileX, int tileY)
	{
		for (int i = 0; i < persistentTriggerArray.size(); i++)
		{
			Trigger trig = persistentTriggerArray.get(i);

			if (trig.xTile == tileX && trig.yTile == tileY)
			{
				return trig;
			}
		}

		return null;
	}

	public boolean hasWall(int tileX, int tileY)
	{
		return Tile.hasWall(map[tileY][tileX]);
	}

	public boolean hasWall(float xPos, float yPos)
	{
		int tileX = (int)(xPos / tileSize);
		int tileY = (int)(yPos / tileSize);

		return Tile.hasWall(map[tileY][tileX]);
	}

	public boolean hasSpecial(int tileX, int tileY)
	{
		return Tile.hasSpecial(map[tileY][tileX]);
	}

	public int getSpecialType(int tileX, int tileY)
	{
		return Tile.getSpecialType(map[tileY][tileX]);
	}

	public int getSpecialValue(int tileX, int tileY)
	{
		return Tile.getSpecialValue(map[tileY][tileX]);
	}
}
