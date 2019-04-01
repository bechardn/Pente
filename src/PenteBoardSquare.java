import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PenteBoardSquare {
	//data
	private int xLoc, yLoc;
	private int sWidth, sHeight;
	
	private int sState; //Open, player1, player2
	
	private Color sColor; //color of the square
	private Color lColor; //color of the lines
	private Color bColor; //color of the boarder
	private Color innerC; //for the inner 5 x 5 squares
	
	private Color darkStoneColor = new Color(4, 9, 64);
	private Color darkStoneTop = new Color(63, 69, 158);
	private Color darkStoneHighLight = new Color(188, 199, 255);
	
	private Color shadowGrey = new Color(169, 173, 142);
	private Color shadowGrey2 = new Color(158, 143, 99);
	private Color shadowGrey3 = new Color(150,126, 57);
	
	private Color lightStoneColor = new Color(224, 222, 204);
	private Color lightStoneColor2 = new Color(242, 240, 222);
	private Color lightStoneColorTop = new Color(250, 250, 250);
	
	boolean isInner = false;
	
	
	
	//constructor
	public PenteBoardSquare(int x, int y, int w, int h)
	{
		xLoc = x;
		yLoc = y;
		sWidth = w;
		sHeight = h;
		
		sColor = new Color(249, 218, 124);
		lColor = new Color(83, 85, 89);
		bColor = new Color(240, 230, 180);
		innerC = new Color(255, 238, 144);
		
		
		sState = PenteGameBoard.EMPTY;
	}
	
	public void setInner()
	{
		isInner = true;
	}
	
	public void drawMe(Graphics g)
	{
		if(isInner)
		{
			g.setColor(innerC);
		} else
		{
			g.setColor(sColor);
		}
		
		g.fillRect(xLoc, yLoc, sWidth, sHeight);
		
		//boarder color
		g.setColor(bColor);
		g.drawRect(xLoc, yLoc, sWidth, sHeight);
		
		if(sState != PenteGameBoard.EMPTY)
		{
			g.setColor(shadowGrey);
			g.fillOval(xLoc, yLoc + 6, sWidth - 8, sHeight - 8);
		} 
		
		g.setColor(lColor);
		//horizontal line
		g.drawLine(xLoc, yLoc + sHeight/2, xLoc + sWidth, yLoc + sHeight/2);
		
		//vertical line
		g.drawLine(xLoc + sWidth/2, yLoc, xLoc + sWidth/2, yLoc + sHeight);
		
	
		
		if(sState == PenteGameBoard.BLACKSTONE)
		{
			g.setColor(darkStoneColor);
			g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
			
			g.setColor(darkStoneTop);
			g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12, sHeight - 10);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			
			g2.setColor(darkStoneHighLight);
			
			g2.setStroke(new BasicStroke(1));
			
			g2.drawArc(xLoc + (int)(sWidth * .45), 
					yLoc + 10, 
					(int)(sWidth * .30), 
					(int)(sHeight * .35), 
					0, 
					90);
		}
		if(sState == PenteGameBoard.WHITESTONE)
		{
			g.setColor(lightStoneColor);
			g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
			
			g.setColor(lightStoneColorTop);
			g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12, sHeight - 10);
		}
	}
	
	public void setState(int newState)
	{
		if(newState < -1 || newState > 1)
		{
			System.out.println(newState + "is an illegal state");
		} else
		{
			sState = newState;
		}
		
		
	}

}
