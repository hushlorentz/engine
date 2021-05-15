package greenpixel.game.ui;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.awt.Rectangle;

//simple implementation of a text area. Features paging and word wrapping according to the
//bounds of the textfield.
public class TextField
{
	public static final int STATIC = 0;
	public static final int ANIMATED = 1;

	public static final int JUSTIFY_LEFT = 0;
	public static final int JUSTIFY_RIGHT = 1;
	public static final int JUSTIFY_CENTER = 2;

	public static final int PAGE_DRAWING = 0;
	public static final int PAGE_FINISHED = 1;
	public static final int TEXT_FINISHED = 2;

	public static final String LINE_BREAK = "^";

	public AngelCodeFont font;
	public String originalText;
	public String convertedText; //This is what is actually displayed. Converted for wrapping and paging.
	public Rectangle bounds;
	public int textIndex;

	private int numLines; //number of lines to be displayed for current page
	private int maxLines; //max number of lines the textfield can display
	private int lineIndices[]; //The indices into the converted text string that represent the index of the last character to display on each line of the textfield.
	private int lastSplitIndex; //The index of the word we left off on the previous page
	private int maxSplitIndex; //The total number of words in the original text
	private int type;
	private Color textColor;
	private int justifyPos; //Can be LEFT, RIGHT, or CENTER justified
	private int counter;
	private int maxCounter;
	private int pageState;

	private boolean debug = false;

	public TextField(AngelCodeFont theFont, Rectangle theBounds, int textType, Color color, int justify)
	{
		originalText = "";
		convertedText = "";
		font = theFont;
		bounds = theBounds;
		textIndex = 0;
		pageState = PAGE_DRAWING;
		maxLines = theBounds.height / theFont.getLineHeight();
		lineIndices = new int[maxLines];
		lastSplitIndex = 0;
		type = textType;
		textColor = color;
		justifyPos = justify;
	}
	
	public void setText(String newText, int timer)
	{
		originalText = newText;
		convertedText = "";
		textIndex = 0;
		pageState = PAGE_DRAWING;
		lastSplitIndex = 0;
		counter = 0;
		maxCounter = timer;

		for (int i = 0; i < lineIndices.length; i++)
		{
			lineIndices[i] = 0;
		}

		analyseText();

		if (timer < 0)
		{
			showPage();
		}
	}

	//build a new string from the passed string where we strip the space " " from the
	//beginning of each wrapped line. Then create an array of indices to display
	//the text on multiple lines.
	private void analyseText()
	{
		int lineIndex = 0;
		String stringBuffer = "";
		String splitString[] = originalText.split(" ");

		maxSplitIndex = splitString.length;
		convertedText = "";

		for (int i = 0; i < lineIndices.length; i++)
		{
			lineIndices[i] = 0;
		}

		numLines = 0;

		for (int i = lastSplitIndex; i < maxSplitIndex; i++)
		{
			if (splitString[i].equals(LINE_BREAK))
			{
					lineIndices[numLines++] = lineIndex; //lineIndex is now the index of the final character in the last word of the line
					
					if (numLines < maxLines) //add the word that made the previous line too long, to the current line
					{
						//don't add the LINE_BREAK character to the next line

						convertedText += stringBuffer;
						stringBuffer = "";
					}
			}
			else if (font.getWidth(stringBuffer + " " + splitString[i]) > bounds.width)
			{
				lineIndices[numLines++] = lineIndex; //lineIndex is now the index of the final character in the last word of the line
				
				if (numLines < maxLines) //add the word that made the previous line too long, to the current line
				{
					convertedText += stringBuffer;
					stringBuffer = splitString[i];
					lineIndex += splitString[i].length();
				}
			}
			else
			{
				if (i > lastSplitIndex && !stringBuffer.equals("")) //add in spaces manually
				{
					stringBuffer += " ";
					lineIndex++;
				}

				stringBuffer += splitString[i];
				lineIndex += splitString[i].length();
			}

			lastSplitIndex = i; //keep track of where the current page leaves off

			if (numLines == maxLines)
			{
				break;
			}
		}

		if (stringBuffer.length() != 0) //If there's still text left in the stringBuffer, we add it to the convertedText. We don't need to set lineIndices as we know this is the last line.
		{
			convertedText += stringBuffer;

			if (numLines < maxLines)
			{
				lineIndices[numLines] = lineIndex;
			}
		}

		textIndex = (type == STATIC) ? convertedText.length() : 0;
	}

	//text field drawing consists of drawing the current line one or more character(s) at a time
	//and all previously drawn lines using the lineIndices.
	public void drawToBuffer(Graphics g, int xPos, int yPos)
	{
		int numDrawLines = 0;
		int startIndex = 0;
		int endIndex = lineIndices[0];

		if (debug)
		{
			g.setColor(Color.blue);
			g.fillRect(xPos + bounds.x, yPos + bounds.y, bounds.width, bounds.height);
		}

		for (int i = 0; i < lineIndices.length; i++)
		{
			if (textIndex <= lineIndices[i])
			{
				break;
			}
			else
			{
				numDrawLines++;
			}
		}

		for (int i = 0; i < numDrawLines; i++)
		{
//			font.drawString(bounds.x, bounds.y + i * font.getLineHeight(), convertedText.substring(startIndex, endIndex), textColor);
			drawLine(xPos + bounds.x, yPos + bounds.y + i * font.getLineHeight(), convertedText.substring(startIndex, endIndex));
			startIndex = endIndex;

			if (i < (lineIndices.length - 1)) //we don't need an endIndex for the last line
			{
				endIndex = lineIndices[i + 1];
			}
		}

		drawLine(xPos + bounds.x, yPos + bounds.y + numDrawLines * font.getLineHeight(), convertedText.substring(startIndex, textIndex));
	}

	private void drawLine(int x, int y, String line)
	{
		int xPos = x;

		switch (justifyPos)
		{
			case JUSTIFY_RIGHT:
				xPos += bounds.width - font.getWidth(line);
				break;
			case JUSTIFY_CENTER:
				xPos += (bounds.width - font.getWidth(line)) / 2;
				break;
		}

		font.drawString(xPos, y, line, debug ? Color.white : textColor);
	}

	public void nextFrame()
	{
		if (textIndex < convertedText.length())
		{
			if (counter == 0)
			{
				textIndex++;
				counter = maxCounter;
			}
			else
			{
				counter--;	
			}
		}
		else
		{
			pageState = PAGE_FINISHED;

			if (lastSplitIndex == (maxSplitIndex - 1))
			{
				pageState = TEXT_FINISHED;
			}
		}
	}

	public void nextPage()
	{
		analyseText();
		pageState = PAGE_DRAWING;
	}

	public void showPage()
	{
		textIndex = convertedText.length();
		pageState = PAGE_FINISHED;

		if (lastSplitIndex == (maxSplitIndex - 1))
		{
			pageState = TEXT_FINISHED;
		}
	}

	public int getPageState()
	{
		return pageState;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}
}
