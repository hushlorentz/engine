package greenpixel.leveleditor.data;

public class TileMapData
{
	public int tileSize;
	public int mapWidth;
	public int mapHeight;
	public long[][] map;

	public TileMapData(int tSize, int width, int height)
	{
		tileSize = tSize;
		mapWidth = width;
		mapHeight = height;
		map = new long[height][width];
	}
}
