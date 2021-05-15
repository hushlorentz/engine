package greenpixel.game;

import greenpixel.game.AnimManager;
import greenpixel.game.DataManager;
import greenpixel.game.MapManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Stack;

public class GameBase extends BasicGame
{
	public static final int FPS_30 = 0;
	public static final int FPS_60 = 1;

	private int screenWidth;
	private int screenHeight;
	private int renderWidth;
	private int renderHeight;
	private float renderScale;

	private final int STATE_RUN = 0;
	private final int STATE_FADE_IN = 1;
	private final int STATE_FADE_OUT = 2;
	private final int STATE_NEXT_LAYER = 3;

	private float fadeAlpha = 0;
	private float FADE_INCREMENT = 0.05f;

	private GameLayer currentLayer;
	private GameLayer nextLayer;
	private Stack<GameLayer> gameLayers;
	private Stack<Integer> gameStates;
	private Input input;

	public GameBase(String title, int width, int height, boolean fullScreen, int fps) throws SlickException
	{
		super(title);
		setupScreen(width, height, width, height, 1.0f, fullScreen, fps);
	}

	public GameBase(String title, int width, int height, int rWidth, int rHeight, float scale, boolean fullScreen, int fps) throws SlickException
	{
		super(title);
		setupScreen(width, height, rWidth, rHeight, scale, fullScreen, fps);
	}

	private void setupScreen(int width, int height, int rWidth, int rHeight, float scale, boolean fullScreen, int fps) throws SlickException
	{
		screenWidth = width;
		screenHeight = height;
		renderWidth = rWidth;
		renderHeight = rHeight;
		renderScale = scale;

		AnimManager.instance = new AnimManager(fps);
		DataManager.instance = new DataManager();
		MapManager.instance = new MapManager(renderWidth, renderHeight);

		gameLayers = new Stack<GameLayer>();

		gameStates = new Stack<Integer>();
		gameStates.push(STATE_RUN);
	}

	public void init(GameContainer gc) throws SlickException 
	{
		gc.setVSync(true);

		input = gc.getInput();
		setup();
	}

	public void setup() {}

	public void update(GameContainer gc, int delta) throws SlickException 
	{
		if (currentLayer == null)
		{
			System.out.println("Current layer is null");
			System.exit(0);
		}

		switch (getGameState())
		{
			case STATE_RUN:
				currentLayer.updateManager(delta);
				break;
			case STATE_FADE_IN:
				fadeAlpha -= FADE_INCREMENT;

				if (fadeAlpha < 0)
				{
					fadeAlpha = 0;
					popState();
				}
				break;
			case STATE_FADE_OUT:
				fadeAlpha += FADE_INCREMENT;

				if (fadeAlpha > 1.0f)
				{
					fadeAlpha = 1.0f;
					popState();
				}
				break;
			case STATE_NEXT_LAYER:
				popState();
				pushLayer(nextLayer);
				break;
		}

		input.clearKeyPressedRecord();
		input.clearControlPressedRecord();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		g.scale(renderScale, renderScale);

		if (currentLayer == null)
		{
			System.out.println("Current layer is null");
			System.exit(0);
		}

		/*
		switch (getGameState())
		{
			case STATE_RUN:
				drawCurrentLayer(g);
				break;
			case STATE_FADE_IN:
				drawCurrentLayer(g);
				break;
			case STATE_FADE_OUT:
				drawCurrentLayer(g);
				break;
		}
		*/

		drawCurrentLayer(g);
		drawFade(g);
	}

	private void drawCurrentLayer(Graphics g)
	{
		currentLayer.drawManager(g);
	}

	public void drawFade(Graphics g)
	{
		Color currentColor = g.getColor();

		g.setColor(new Color(0, 0, 0, fadeAlpha));
		g.fillRect(0, 0, screenWidth, screenHeight);

		g.setColor(currentColor);
	}

	public void pushLayer(GameLayer layer)
	{
		gameLayers.push(layer);
		currentLayer = layer;
		currentLayer.init();
	}

	public void fadeAndPushLayer(GameLayer layer)
	{
		fadeAlpha = 0;
		nextLayer = layer;

		pushState(STATE_NEXT_LAYER);
		pushState(STATE_FADE_OUT);
	}

	public void fadeIn()
	{
		fadeAlpha = 1.0f;
		pushState(STATE_FADE_IN);
	}

	public void fadeOut()
	{
		fadeAlpha = 0;
		pushState(STATE_FADE_OUT);
	}

	protected void addAnimBundles(String [] paths)
	{
		for (int i = 0; i < paths.length; i++)
		{
			AnimManager.instance.addBundle(paths[i]);
		}
	}

	protected void loadAnimData(String path)
	{
		AnimManager.instance.loadAnimData(path);
	}

	protected void loadGUTData(String path)
	{
		DataManager.instance.loadGUTData(path);
	}

	protected void loadMapData(String path)
	{
		MapManager.instance.loadMapData(path);
	}

	protected void addMusic(String path)
	{
//		SoundManager.instance.addMusic(music);
	}

	protected void addEffect(String path)
	{
//		SoundManager.instance.addEffect(effect);
	}

	protected boolean isControllerDown(int controller)
	{
		return input.isControllerDown(controller);
	}

	protected boolean isControllerUp(int controller)
	{
		return input.isControllerUp(controller);
	}

	protected boolean isControllerLeft(int controller)
	{
		return input.isControllerLeft(controller);
	}

	protected boolean isControllerRight(int controller)
	{
		return input.isControllerRight(controller);
	}

	protected boolean isButtonPressed(int button, int controller)
	{
		return input.isButtonPressed(button, controller);
	}

	protected boolean keyDown(int keyCode)
	{
		return input.isKeyDown(keyCode);
	}

	protected boolean keyPressed(int keyCode)
	{
		return input.isKeyPressed(keyCode);
	}

	private void pushState(int state)
	{
		gameStates.push(state);
	}

	private void popState()
	{
		gameStates.pop();
	}

	private int getGameState()
	{
		return gameStates.peek();
	}
}
