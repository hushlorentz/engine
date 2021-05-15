package greenpixel.game.sprite;

import greenpixel.game.sprite.Sprite2D;
import greenpixel.game.ui.TextField;
import greenpixel.math.FloatRect;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.Rectangle;

public class TextSprite extends Sprite2D
{
	private TextField textField;
	private boolean useBGAnim;

	public TextSprite(AngelCodeFont font, Rectangle bounds, int animType, Color color, int alignment, boolean useAnim)
	{
		textField = new TextField(font, bounds, animType, color, alignment);
		collisionBox = new FloatRect(bounds);
		useBGAnim = useAnim;
	}

	public void setText(String text, int delay)
	{
		textField.setText(text, delay);
	}

	public void showPage()
	{
		textField.showPage();
	}

	public void nextPage()
	{
		textField.nextPage();
	}

	public int getPageState()
	{
		return textField.getPageState();
	}

	public void drawToBuffer(Graphics g)
	{
		if (useBGAnim)
		{
			super.drawToBuffer(g, (int)x, (int)y);
		}	

		textField.drawToBuffer(g, (int)x, (int)y);
	}

	public void nextFrame()
	{
		if (useBGAnim)
		{
			super.nextFrame();
		}

		textField.nextFrame();
	}

	public void setDebug(boolean debug)
	{
		textField.setDebug(debug);
	}
}
