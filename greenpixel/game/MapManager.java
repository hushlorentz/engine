package greenpixel.game;

import greenpixel.game.tilemap.TileMap;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

import java.nio.ByteBuffer;

public class MapManager
{
	public static MapManager instance;
	public ArrayList<TileMap> maps;

	private int screenWidth;
	private int screenHeight;

	public MapManager(int scrWidth, int scrHeight)
	{
		maps = new ArrayList<TileMap>();
		screenWidth = scrWidth;
		screenHeight = scrHeight;
	}

	public void loadMapData(String path)
	{
		maps.clear();

		try
		{
//			DataInputStream in = new DataInputStream(getClass().getClassLoader().getResourceAsStream(path));
			DataInputStream in = new DataInputStream(new FileInputStream(path));

			int numMaps = in.readInt();

			System.out.println("numMaps: " + numMaps);

			for (int i = 0; i < numMaps; i++)
			{
				int dataSize = in.readInt();

				System.out.println("\tmap data size: " + dataSize);
				byte[] mapData = new byte[dataSize];
				ByteBuffer bufferData = ByteBuffer.allocate(dataSize);

				in.read(mapData, 0, dataSize);//dataStart, dataSize);
				bufferData.put(mapData);
				bufferData.rewind();

				TileMap map = new TileMap(screenWidth, screenHeight);
				map.loadMap(bufferData);
				maps.add(map);
			}

			in.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	public TileMap getMap(int index)
	{
		return maps.get(index);
	}

	public void resetContainers(int index)
	{
		maps.get(index).resetContainers();
	}

	public void resetBreakableEntities(int index)
	{
		maps.get(index).resetBreakableEntities();
	}

	public void resetPersistentData()
	{
		for (int i = 0; i < maps.size(); i++)
		{
			maps.get(i).resetContainers();
			maps.get(i).resetBreakableEntities();
		}
	}
}
