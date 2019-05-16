import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel implements MouseListener{
	
	public static final int EMPTY = 0;
	public static final int BLACKSTONE = 1;
	public static final int WHITESTONE = -1;
	public static final int NUM_SQUARES_SIDE = 19;
	public static final int INNER_START = 7;
	public static final int INNER_END = 11;
	public static final int PLAYER1_TURN = 1;
	public static final int PLAYER2_TURN = -1;
	public static final int MAX_CAPTURES = 5;
	public static final int SLEEP_TIME = 250;


	
	private int bWidth, bHeight;
	
	private PenteBoardSquare testSquare;
	private int squareW, squareH;
	
	
	private int playerTurn;
	private boolean player1IsComputer = false;
	private boolean player2IsComputer = false;
	private String p1Name, p2Name;
	private boolean darkStoneMove2Taken = false;
	
	
	private PenteBoardSquare[][] gameBoard;
	private PenteScore myScoreBoard;
	private int p1Captures, p2Captures;
	private boolean gameOver = false;
	
	private ComputerMoveGenerator p1ComputerPlayer = null;
	private ComputerMoveGenerator p2ComputerPlayer = null;

	
	public PenteGameBoard(int w, int h, PenteScore sb)
	{
		
		//store these variables
		bWidth = w;
		bHeight = h;
		myScoreBoard = sb;
		
		this.setSize(w, h);
		this.setBackground(Color.CYAN);
		
		squareW = bWidth / this.NUM_SQUARES_SIDE;
		squareH = bHeight / this.NUM_SQUARES_SIDE;
		
		//testSquare = new PenteBoardSquare(0, 0, squareW, squareH);
		gameBoard = new PenteBoardSquare[NUM_SQUARES_SIDE][NUM_SQUARES_SIDE];
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++)
		{
			for(int col = 0; col < NUM_SQUARES_SIDE; col++)
			{
				gameBoard[row][col] = new PenteBoardSquare(col * squareW, row * squareH, squareW, squareH);
				if(col >= INNER_START && col <= INNER_END)
				{
					if(row >= INNER_START && row <= INNER_END)
					{
						gameBoard[row][col].setInner();
					}
				}
				
//				if((row + col) % 2 == 0)
//				{
//					gameBoard[row][col].setState(BLACKSTONE);
//				} else
//				{
//					gameBoard[row][col].setState(WHITESTONE);
//				}
				 
			}
		}
		initialDisplay();
		//this.paintImmediately(0, 0, bWidth, bHeight);
		addMouseListener(this);
		this.setFocusable(true);

	}
	
	//method to do drawing
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, bWidth, bHeight);
		
		//do this 19 x 19 times
		//testSquare.drawMe(g);
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++)
		{
			for(int col = 0; col < NUM_SQUARES_SIDE; col++)
			{
				gameBoard[row][col].drawMe(g);

			}
		}
		
	}
	
	public void resetBoard()
	{
		for(int row = 0; row < NUM_SQUARES_SIDE; row++)
		{
			for(int col = 0; col < NUM_SQUARES_SIDE; col++)
			{
				gameBoard[row][col].setState(EMPTY);
				gameBoard[row][col].setWinningSquare(false);

				gameOver = false;
			}
		}
		//this.paintImmediately(0, 0, bWidth, bHeight);
		repaint();

	}
	
	public void startNewGame(boolean firstGame)
	{
		p1Captures = 0;
		p2Captures = 0;
		if(firstGame)
		{
			p1Name = JOptionPane.showInputDialog("Name of player 1 (or type 'c' for computer)");
			if(p1Name != null && (p1Name.toLowerCase().equals("c") || p1Name.toLowerCase().equals("computer") || p1Name.toLowerCase().equals("comp")))
			{
				player1IsComputer = true;
				p1ComputerPlayer = new ComputerMoveGenerator(this, BLACKSTONE);
			}
		}
		myScoreBoard.setName(p1Name, BLACKSTONE);
		myScoreBoard.setCaptures(p1Captures, BLACKSTONE);
		
		if(firstGame)
		{
			p2Name = JOptionPane.showInputDialog("Name of player 2 (or type 'c' for computer)");
			if(p2Name != null && (p2Name.toLowerCase().equals("c") || p2Name.toLowerCase().equals("computer") || p2Name.toLowerCase().equals("comp")))
			{
				player2IsComputer = true;
				p2ComputerPlayer = new ComputerMoveGenerator(this, WHITESTONE);
			}
		}
		myScoreBoard.setName(p2Name, WHITESTONE);
		myScoreBoard.setCaptures(p2Captures, WHITESTONE);
		
		resetBoard();
		
		playerTurn = this.PLAYER1_TURN;
		this.gameBoard[NUM_SQUARES_SIDE / 2][NUM_SQUARES_SIDE / 2].setState(BLACKSTONE);
		this.darkStoneMove2Taken = false;
		changePlayerTurn();
		
		checkForComputerMove(playerTurn);
		
		repaint();

		}

	
	public void changePlayerTurn()
	{
		playerTurn *= -1;
		myScoreBoard.setPlayerTurn(playerTurn);
	}
	
	public boolean fiveInARow(int whichPlayer) //int r, int c, int pt, int upDown, int rightLeft)
	{
		boolean isFive = false;
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++)
		{
			for(int col = 0; col < NUM_SQUARES_SIDE; col++)
			{
				for(int rL = -1; rL <= 1; rL++)
				{
					for(int uD = -1; uD <=1; uD++)
					{
						if(fiveCheck(row, col, playerTurn, rL, uD) == true)
						{
							isFive = true;
						}
					}
				}
			}
		}
		return isFive;
	}
	
	public boolean fiveCheck(int r, int c, int pt, int upDown, int rightLeft) 
	{
		try 
		{
		boolean win = false;
		
		if(rightLeft != 0 || upDown != 0)
		{
			if(gameBoard[r][c].getState() == pt)
			{
				if(gameBoard[r + upDown][c+ rightLeft].getState() == pt)
				{
					if(gameBoard[r + (upDown * 2)][c+ (rightLeft * 2)].getState() == pt)
					{
						if(gameBoard[r + (upDown * 3)][c+ (rightLeft * 3)].getState() == pt)
						{
							if(gameBoard[r + (upDown * 4)][c+ (rightLeft * 4)].getState() == pt)
							{
								win = true;
								gameBoard[r][c].setWinningSquare(true);
								gameBoard[r + upDown][c + rightLeft].setWinningSquare(true);
								gameBoard[r + (upDown * 2)][c+ (rightLeft * 2)].setWinningSquare(true);
								gameBoard[r + (upDown * 3)][c+ (rightLeft * 3)].setWinningSquare(true);
								gameBoard[r + (upDown * 4)][c+ (rightLeft * 4)].setWinningSquare(true);
							}
						}
					}
				}
			}
		}
//		if(p1Captures >= MAX_CAPTURES || p2Captures >= MAX_CAPTURES)
//		{
//			win = true;
//		}
		return win;

		} catch(ArrayIndexOutOfBoundsException e)
		{
			//System.out.println("You have an error " + e.getMessage());
			return false;
		}
	}

	
	public void checkForWin(int whichPlayer)
	{
		if(whichPlayer == this.PLAYER1_TURN)
		{
			if(this.p1Captures >= MAX_CAPTURES)
			{
				JOptionPane.showMessageDialog(null, "Congratulations " + p1Name + 
						" wins" + 
						"\n with " + p1Captures + " captures!!"
						);
				gameOver = true;
			} else
			{
				if(fiveInARow(whichPlayer))
				{
					JOptionPane.showMessageDialog(null, "Congratulations " + p1Name + 
							" wins with 5 in a row!!"
							);
					gameOver = true;
				}
				
			}
		} else 
		{
			if(this.p2Captures >= MAX_CAPTURES)
			{
				JOptionPane.showMessageDialog(null, "Congratulations " + p2Name + 
						" wins" + 
						"\n with " + p2Captures + " captures!!"
						);
				gameOver = true;
			} else
			{
				if(fiveInARow(whichPlayer))
				{
					JOptionPane.showMessageDialog(null, "Congratulations " + p2Name + 
							" wins with 5 in a row!!"
							);
					gameOver = true;
				}
				
			}
		}
	}
	
	public void checkClick(int clickX, int clickY)
	{
		if(!gameOver)
		{
			for(int row = 0; row < NUM_SQUARES_SIDE; row++)
			{
				for(int col = 0; col < NUM_SQUARES_SIDE; col++)
				{
					boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
					
					if(squareClicked)
					{
						if(gameBoard[row][col].getState() == EMPTY)
						{
							if( !darkSquareProblem(row, col))
							{
							gameBoard[row][col].setState(playerTurn);
							checkForAllCaptures(row, col, playerTurn);
							//this.repaint();
							this.paintImmediately(0, 0, bWidth, bHeight);
							this.checkForWin(playerTurn);
							this.changePlayerTurn();
							checkForComputerMove(playerTurn);
							} else
							{
								JOptionPane.showMessageDialog(null, "Second dark stone move has to be outside of the light square");
							}
						} else
						{
							JOptionPane.showMessageDialog(null, "This square is taken, click on another");
						}
					}
	
				}
			}
		}
	}
	
	public void checkForComputerMove(int whichPlayer)
	{
		if(whichPlayer == this.PLAYER1_TURN && this.player1IsComputer)
		{
			int[] nextMove = this.p1ComputerPlayer.getComputerMove();
			int newR = nextMove[0];
			int newC = nextMove[1];
			gameBoard[newR][newC].setState(playerTurn);
			this.repaint();
			checkForAllCaptures(newR, newC, playerTurn);
			this.paintImmediately(0, 0, bWidth, bHeight);
			//repaint();
			this.checkForWin(playerTurn);
			if(!gameOver)
			{
				this.changePlayerTurn();
				checkForComputerMove(playerTurn);
			}
			
		}else if(whichPlayer == this.PLAYER2_TURN && this.player2IsComputer)
		{
			int[] nextMove = this.p2ComputerPlayer.getComputerMove();
			int newR = nextMove[0];
			int newC = nextMove[1];
			gameBoard[newR][newC].setState(playerTurn);
			this.repaint();
			checkForAllCaptures(newR, newC, playerTurn);
			this.paintImmediately(0, 0, bWidth, bHeight);
			//repaint();
			this.checkForWin(playerTurn);
			if(!gameOver)
			{
				this.changePlayerTurn();
				checkForComputerMove(playerTurn);
			}
			
		}
		this.repaint(); 
	}
	
	public boolean darkSquareProblem(int r, int c)
	{
		boolean dsp = false;
		
		if((darkStoneMove2Taken == false) && (playerTurn == BLACKSTONE))
		{	
			if((r >= INNER_START && r <= INNER_END) && (c >= INNER_START && c <= INNER_END))
				{
					dsp = true;
				} else
				{
					darkStoneMove2Taken = true;
				}
		}
		
		return dsp;
	}
	
	public boolean darkSquareProblemComputerMoveList(int r, int c)
	{
		boolean dsp = false;
		
		if((darkStoneMove2Taken == false) && (playerTurn == BLACKSTONE))
		{	
			if((r >= INNER_START && r <= INNER_END) && (c >= INNER_START && c <= INNER_END))
				{
					dsp = true;
				} else
				{
					//darkStoneMove2Taken = true;
				}
		}
		
		return dsp;
	}
	
	public void checkForAllCaptures(int r, int c, int pt)
	{
		boolean didCapture;
		
		for(int rL = -1; rL <= 1; rL++)
		{
			for(int uD = -1; uD <=1; uD++)
			{
				didCapture = checkForCaptures(r, c, pt, rL, uD);
			}
		}

	}
	
	public boolean checkForCaptures(int r, int c, int pt, int upDown, int rightLeft)
	{
		try 
		{
		boolean cap = false;
		
		if(gameBoard[r + upDown][c+ rightLeft].getState() == pt * -1)
		{
			if(gameBoard[r + (upDown * 2)][c+ (rightLeft * 2)].getState() == pt * -1)
			{
				if(gameBoard[r + (upDown * 3)][c+ (rightLeft * 3)].getState() == pt)
				{
					//System.out.println("It's a capture");
					
					gameBoard[r + upDown][c+ rightLeft].setState(this.EMPTY);
					gameBoard[r + (upDown * 2)][c+ (rightLeft * 2)].setState(this.EMPTY);
					cap = true;
					if(pt == this.PLAYER1_TURN) 
					{
						p1Captures++;
						this.myScoreBoard.setCaptures(p1Captures, playerTurn);
					} else
					{
						p2Captures++;
						this.myScoreBoard.setCaptures(p2Captures, playerTurn);

					}

				}
			}
		}
		return cap;

		} catch(ArrayIndexOutOfBoundsException e)
		{
			//System.out.println("You have an error " + e.toString());
			return false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("And you clicked at[" + e.getX() + ", " + e.getY() + "]");
		this.checkClick(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void initialDisplay()
	{
		//L
		this.gameBoard[1][2].setState(BLACKSTONE);
		this.gameBoard[2][2].setState(BLACKSTONE);
		this.gameBoard[3][2].setState(BLACKSTONE);
		this.gameBoard[4][2].setState(BLACKSTONE);
		this.gameBoard[5][2].setState(BLACKSTONE);
		this.gameBoard[5][3].setState(BLACKSTONE);
		this.gameBoard[5][4].setState(BLACKSTONE);
		
		//E
		this.gameBoard[1][6].setState(BLACKSTONE);
		this.gameBoard[2][6].setState(BLACKSTONE);
		this.gameBoard[3][6].setState(BLACKSTONE);
		this.gameBoard[4][6].setState(BLACKSTONE);
		this.gameBoard[5][6].setState(BLACKSTONE);
		this.gameBoard[1][7].setState(BLACKSTONE);
		this.gameBoard[1][8].setState(BLACKSTONE);
		this.gameBoard[3][7].setState(BLACKSTONE);
		this.gameBoard[5][7].setState(BLACKSTONE);
		this.gameBoard[5][8].setState(BLACKSTONE);

		//T
		this.gameBoard[1][10].setState(BLACKSTONE);
		this.gameBoard[1][11].setState(BLACKSTONE);
		this.gameBoard[1][12].setState(BLACKSTONE);
		this.gameBoard[2][11].setState(BLACKSTONE);
		this.gameBoard[3][11].setState(BLACKSTONE);
		this.gameBoard[4][11].setState(BLACKSTONE);
		this.gameBoard[5][11].setState(BLACKSTONE);

		//S
		this.gameBoard[1][16].setState(BLACKSTONE);
		this.gameBoard[1][15].setState(BLACKSTONE);
		this.gameBoard[1][14].setState(BLACKSTONE);
		this.gameBoard[2][14].setState(BLACKSTONE);
		this.gameBoard[3][14].setState(BLACKSTONE);
		this.gameBoard[3][15].setState(BLACKSTONE);
		this.gameBoard[3][16].setState(BLACKSTONE);
		this.gameBoard[4][16].setState(BLACKSTONE);
		this.gameBoard[5][16].setState(BLACKSTONE);
		this.gameBoard[5][15].setState(BLACKSTONE);
		this.gameBoard[5][14].setState(BLACKSTONE);

		
		
		
		//P
		this.gameBoard[12][2].setState(BLACKSTONE);
		this.gameBoard[13][2].setState(BLACKSTONE);
		this.gameBoard[14][2].setState(BLACKSTONE);
		this.gameBoard[15][2].setState(BLACKSTONE);
		this.gameBoard[16][2].setState(BLACKSTONE);
		this.gameBoard[17][2].setState(BLACKSTONE);
		this.gameBoard[12][3].setState(BLACKSTONE);
		this.gameBoard[12][4].setState(BLACKSTONE);
		this.gameBoard[13][4].setState(BLACKSTONE);
		this.gameBoard[14][4].setState(BLACKSTONE);
		this.gameBoard[14][3].setState(BLACKSTONE);

		//L
		this.gameBoard[12][6].setState(BLACKSTONE);
		this.gameBoard[13][6].setState(BLACKSTONE);
		this.gameBoard[14][6].setState(BLACKSTONE);
		this.gameBoard[15][6].setState(BLACKSTONE);
		this.gameBoard[16][6].setState(BLACKSTONE);
		this.gameBoard[17][6].setState(BLACKSTONE);
		this.gameBoard[17][7].setState(BLACKSTONE);
		this.gameBoard[17][8].setState(BLACKSTONE);

		//A
		this.gameBoard[17][10].setState(BLACKSTONE);
		this.gameBoard[16][10].setState(BLACKSTONE);
		this.gameBoard[15][10].setState(BLACKSTONE);
		this.gameBoard[14][10].setState(BLACKSTONE);
		this.gameBoard[13][10].setState(BLACKSTONE);
		this.gameBoard[12][10].setState(BLACKSTONE);
		this.gameBoard[12][11].setState(BLACKSTONE);
		this.gameBoard[12][12].setState(BLACKSTONE);
		this.gameBoard[12][13].setState(BLACKSTONE);
		this.gameBoard[13][13].setState(BLACKSTONE);
		this.gameBoard[14][13].setState(BLACKSTONE);
		this.gameBoard[15][13].setState(BLACKSTONE);
		this.gameBoard[16][13].setState(BLACKSTONE);
		this.gameBoard[17][13].setState(BLACKSTONE);
		this.gameBoard[14][11].setState(BLACKSTONE);
		this.gameBoard[14][12].setState(BLACKSTONE);


		//Y
		this.gameBoard[12][15].setState(BLACKSTONE);
		this.gameBoard[13][15].setState(BLACKSTONE);
		this.gameBoard[14][15].setState(BLACKSTONE);
		this.gameBoard[14][16].setState(BLACKSTONE);
		this.gameBoard[14][17].setState(BLACKSTONE);
		this.gameBoard[13][17].setState(BLACKSTONE);
		this.gameBoard[12][17].setState(BLACKSTONE);
		this.gameBoard[15][16].setState(BLACKSTONE);
		this.gameBoard[16][16].setState(BLACKSTONE);
		this.gameBoard[17][16].setState(BLACKSTONE);


	}
	
	public PenteBoardSquare[][] getBoard()
	{
		return gameBoard;
	}
	
	public boolean getDarkStone2Taken()
	{
		return darkStoneMove2Taken;
	}
		
		
	
}