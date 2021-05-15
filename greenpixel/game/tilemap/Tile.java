package greenpixel.game.tilemap;

public class Tile 
{
	public static final int SPECIAL_NONE = 0;
	public static final int SPECIAL_ENTITY = 1;
	public static final int SPECIAL_BREAKABLE = 2;
	public static final int SPECIAL_CONTAINER = 3;
	public static final int SPECIAL_DOOR = 4;
	public static final int SPECIAL_LOOT = 5;
	public static final int SPECIAL_PLAYER = 6;
	public static final int SPECIAL_TRIGGER = 7;
	public static final int SPECIAL_BARRIER = 8;

	public static final int TILE_SPECIAL_SHIFT = 56;
	public static final int TILE_TYPE_SHIFT = 32;

	public static final int TILE_XPOS_SHIFT = 16;
	public static final int TILE_YPOS_SHIFT = 8;
	public static final byte HFLIP = (byte)1 << 0;
	public static final byte VFLIP = (byte)1 << 1;
	public static final byte WALL = (byte)1 << 2;
	public static final byte OVER = (byte)1 << 3;
	public static final byte UNDER = (byte)1 << 4;
	
	public static final int SPECIAL_FLAG = 0xFFFFFFFF;
	
	public static long setImgCoords(int x, int y, long tile)
	{
		tile &= 0xFF0000FF; //clear the x and y bytes
		
		tile |= (x << Tile.TILE_XPOS_SHIFT);
		tile |= (y << Tile.TILE_YPOS_SHIFT);
		
		return tile;
	}
	
	public static long setAllFlags(long tile, long flags)
	{
		tile &= 0xFFFFFF00;
		return (tile |= (byte)flags);
	}
	
	public static long setFlag(long tile, byte flag)
	{
		return (tile |= flag);
	}
	
	public static long unsetFlag(long tile, byte flag)
	{
		int intFlag = flag;
		
		return (tile &= ~intFlag);
	}
	
	public static boolean hasFlag(long tile, byte flag)
	{
		return ((byte)tile & flag) == flag;
	}

	public static boolean hasWall(long tile)
	{
		return ((byte)tile & WALL) == WALL;// || ((byte)tile & UNDER) == UNDER;
	}
	
	public static byte getImageX(long tile)
	{
		return (byte)(tile >> Tile.TILE_XPOS_SHIFT);
	}
	
	public static byte getImageY(long tile)
	{
		return (byte)(tile >> Tile.TILE_YPOS_SHIFT);
	}
	
	public static long setSpecial(long tile, int special, int type)
	{
		tile &= 0x00FFFFFF; //clear the special bits. Java is weird as the 0x notation is for integers of 32 bits. We need a long, but Java will take the first number after the x and use that for the other 32 bits. Here, we really have 0x0000000000FFFFFF
		long spec = ((long)special << TILE_SPECIAL_SHIFT) | ((long)type << TILE_TYPE_SHIFT);
		return tile |= spec;
	}

	public static boolean hasSpecial(long tile) 
	{
		return ((tile >>> Tile.TILE_SPECIAL_SHIFT) & Tile.SPECIAL_FLAG) != 0;
	}
	
	public static int getSpecialType(long tile)
	{
		return (int)(tile >>> Tile.TILE_SPECIAL_SHIFT);
	}
	
	public static int getSpecialValue(long tile)
	{
		return (int)((tile >>> Tile.TILE_TYPE_SHIFT) & 0x00FFFFFF);
	}
}
