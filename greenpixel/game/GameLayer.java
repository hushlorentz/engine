package greenpixel.game;

import greenpixel.game.GameBase;
import greenpixel.game.sprite.Sprite2D;
import greenpixel.game.sprite.TextSprite;
import greenpixel.game.ui.TextField;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.awt.Rectangle;
import java.util.Stack;

public class GameLayer
{
	protected final int STATE_BASE = 0;
	protected final int STATE_TEXT = 1;
	protected final int STATE_CONVERSATION = 2;
	protected final int STATE_CHANGE_MAP = 3;

	protected TextSprite textPopup;
	protected Graphics gameGraphics;

	private GameBase game;
	private Stack<Integer> layerStates;

	public GameLayer(GameBase game)
	{
		this.game = game;

		layerStates = new Stack<Integer>();
		pushState(STATE_BASE);
	}

	public void init()
	{

	}

	public void updateManager(int delta)
	{
		switch (getState())
		{
			case STATE_TEXT:
				updateText(delta);
				break;
			default:
				update(delta);
				break;
		}
	}

	public void update(int delta) {}

	public void updateText(int delta)
	{
		if (isKeyPressed(Input.KEY_SPACE) || isButtonPressed(0, 0))
		{
			switch (textPopup.getPageState())
			{
				case TextField.PAGE_DRAWING:
					textPopup.showPage();
					break;
				case TextField.PAGE_FINISHED:
					textPopup.nextPage();
					break;
				case TextField.TEXT_FINISHED:
					popState();
					break;
			}
		}

		textPopup.nextFrame();
	}

	public void drawManager(Graphics g)
	{
		gameGraphics = g;

		switch (getState())
		{
			case STATE_TEXT:
			case STATE_CONVERSATION:
				drawText();
				break;
			case STATE_CHANGE_MAP:
			case STATE_BASE:
				draw();
				break;
		}
	}

	public void drawText()
	{
		draw();
		drawTextSprite(textPopup);
	}

	public void draw() {}

	protected void drawSprite(Sprite2D sprite)
	{
		drawSpriteAt(sprite, sprite.x, sprite.y);
	}

	protected void drawSpriteAt(Sprite2D sprite, float xPos, float yPos)
	{
		sprite.drawToBuffer(gameGraphics, (int)xPos, (int)yPos);
		sprite.nextFrame();
	}

	protected void drawTextSprite(TextSprite textSprite)
	{
		textSprite.drawToBuffer(gameGraphics);
		textSprite.nextFrame();
	}

	protected void initPopup(AngelCodeFont font, Rectangle r, int animType, Color color, int alignment, boolean useAnim)
	{
		textPopup = new TextSprite(font, r, animType, color, alignment, useAnim);
	}

	protected void setPopupDebug(boolean debug)
	{
		textPopup.setDebug(debug);
	}

	protected void setPopupAnim(int animGroup, int animIndex)
	{
		textPopup.setAnim(animGroup, animIndex);
	}

	protected void setPopupPos(float xPos, float yPos)
	{
		textPopup.setPos(xPos, yPos);
	}

	protected void setPopupText(String text, int delay)
	{
		pushState(STATE_TEXT);
		textPopup.setText(text, delay);
	}

	protected void fadeIn()
	{
		game.fadeIn();
	}

	protected void fadeOut()
	{
		game.fadeOut();
	}

	protected boolean isControllerDown(int controller)
	{
		return game.isControllerDown(controller);
	}

	protected boolean isControllerUp(int controller)
	{
		return game.isControllerUp(controller);
	}

	protected boolean isControllerLeft(int controller)
	{
		return game.isControllerLeft(controller);
	}

	protected boolean isControllerRight(int controller)
	{
		return game.isControllerRight(controller);
	}

	protected boolean isButtonPressed(int button, int controller)
	{
		return game.isButtonPressed(button, controller);
	}

	protected boolean isKeyPressed(int keyCode)
	{
		return game.keyPressed(keyCode);
	}

	protected boolean isKeyDown(int keyCode)
	{
		return game.keyDown(keyCode);
	}

	protected void popState()
	{
		if (layerStates.size() == 1)
		{
			System.out.println("Cannot pop base state from game layer");
			return;
		}

		layerStates.pop();
	}

	protected void pushState(int state)
	{
		layerStates.push(state);
	}

	protected int getState()
	{
		return layerStates.peek();
	}
}
