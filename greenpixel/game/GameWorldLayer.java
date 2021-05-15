package greenpixel.game;

import greenpixel.game.sprite.Sprite2D;
import greenpixel.game.sprite.TextSprite;
import greenpixel.game.tilemap.TileMap;
import greenpixel.game.world.World;
import greenpixel.game.world.objects.Door;
import greenpixel.game.ui.TextField;
import greenpixel.gut.data.ConversationData;
import greenpixel.gut.data.DialogData;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import java.awt.Rectangle;
import java.util.Stack;

public class GameWorldLayer extends GameLayer
{
	protected World world;

	private int conversationIndex;
	private ConversationData currentConversation;
	private Door doorToNextMap;

	public GameWorldLayer(GameBase game)
	{
		super(game);
	}

	public void init()
	{

	}

	public void updateManager(int delta)
	{
		switch (getState())
		{
			case STATE_CHANGE_MAP:
				world.setTileMap(doorToNextMap);
				popState();
				fadeIn();
				break;
			case STATE_CONVERSATION:
				if (conversationIndex < currentConversation.dialog.size())
				{
					DialogData dData = currentConversation.dialog.get(conversationIndex);

					setPopupText(dData.text, dData.textSpeed);
					conversationIndex++;
				}
				else
				{
					popState();
				}
				break;
			default:
				super.updateManager(delta);
				break;
		}
	}

	public void update(int delta) {}

	protected void drawWorld(World world)
	{
		world.drawToBuffer(gameGraphics);
		world.nextFrame();
	}

	protected void drawSpriteInWorld(World world, Sprite2D sprite)
	{
		world.drawSprite(gameGraphics, sprite);
	}

	protected void drawTextSprite(TextSprite textSprite)
	{
		textSprite.drawToBuffer(gameGraphics);
		textSprite.nextFrame();
	}

	protected void changeMap(Door door)
	{
		doorToNextMap = door;

		fadeOut();
		pushState(STATE_CHANGE_MAP);
	}

	protected void playConversation(int index)
	{
		currentConversation = DataManager.instance.getConversation(index);
		conversationIndex = 0;

		pushState(STATE_CONVERSATION);
	}
}
