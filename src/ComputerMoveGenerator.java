import java.util.ArrayList;
import java.util.Collections;

public class ComputerMoveGenerator {

	public static final int OFFENSE = 1;
	public static final int DEFENSE = -1;
	
	public static final int ONE_IN_A_ROW_DEF = 1;
	public static final int TWO_IN_A_ROW_DEF = 4;
	public static final int TWO_IN_A_ROW_OPEN = 5;
	public static final int TWO_IN_A_ROW_CAP = 6;
	public static final int THREE_DEF = 9;
	public static final int THREE_EMPTY_DEF = 10;
	public static final int FOUR_DEF = 11;
	public static final int FOUR_EMPTY_DEF = 15;
	public static final int FOUR_2_2_DEF = 15;
	public static final int FIVE_DEF = 20;
	
	public static final int ONE_IN_A_ROW_OFF = 2;
	public static final int TWO_IN_A_ROW_OFF = 3;
	public static final int THREE_OFF = 7;
	public static final int THREE_EMPTY_OFF = 7;
	public static final int FOUR_OFF = 10;
	public static final int FOUR_EMPTY_OFF = 12;
	public static final int FOUR_2_2_OFF = 20;
	public static final int FIVE_OFF = 20;

	boolean firstMove = true;
	int firstInt = 0;


	PenteGameBoard myGame;
	int myStone;
	
	
	ArrayList<CMObject> moves = new ArrayList<CMObject>();

	
	public ComputerMoveGenerator(PenteGameBoard gb, int myStoneColor)
	{
		myStone = myStoneColor;
		myGame = gb;
		
		//System.out.println("Computer is play as player " + myStone);
	}
	
	public int[] getComputerMove()
	{
		
		int[] newMove = new int[2];
		newMove[0] = -1;
		newMove[1] = 0;
		
		moves.clear();
		
		findMoves();
		
		setPriorities();		
		
		printPriorities();
		System.out.println();

		
		
		if(moves.size() > 0)
		{
			//int whichOne = (int)(Math.random() * dMoves.size());
			CMObject ourMove;
			
			if(moves.get(0).getPriority() <= this.ONE_IN_A_ROW_DEF) 
			{
				ourMove = moves.get((int)(Math.random() * moves.size()));
			} else
			{
				ourMove = moves.get(0);
			}
			newMove[0] = ourMove.getRow();
			newMove[1] = ourMove.getCol();
			
			if(myGame.darkSquareProblem(newMove[0], newMove[1]) == true) 
			{
				System.out.println("bad problem");
			}
		} else
		{
			if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStone2Taken() == false) 
			{
				//System.out.println("dark stone problem");
				int newBStoneProbRow = -1;
				int newBStoneProbCol = -1;
				int innerSafeSquareSideLen = PenteGameBoard.INNER_END - PenteGameBoard.INNER_START + 1;
				
				while(myGame.getDarkStone2Taken() == false)
				{
					newBStoneProbRow = (int)(Math.random() * (innerSafeSquareSideLen + 2) ) 
										+ (innerSafeSquareSideLen + 1);
					
					newBStoneProbCol = (int)(Math.random() * (innerSafeSquareSideLen + 2) ) 
							+ (innerSafeSquareSideLen + 1);
					
					myGame.darkSquareProblem(newBStoneProbRow, newBStoneProbCol);
				}
				newMove[0] = newBStoneProbRow;
				newMove[1] = newBStoneProbCol;
			} else
			{
				newMove = generateRandomMove();
			}
		}
	
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		return newMove;
	}
	
	public void setPriorities()
	{
		Collections.sort(moves);
	}
	
	public void printPriorities()
	{
		for(CMObject m: moves)
		{
			System.out.println(m);
		}
		
	}
	
	
	public void findMoves()
	{
		for(int row = 0; row < PenteGameBoard.NUM_SQUARES_SIDE; row++)
		{
			for(int col = 0; col < PenteGameBoard.NUM_SQUARES_SIDE; col++)
			{
				if(myGame.getBoard()[row][col].getState() == myStone * -1)
				{
					findOneDef(row, col);
					findTwoDef(row, col);
					findThreeDef(row, col);
					findThreeEmptyDef(row, col); //1-2
					findFourDef(row, col);
					findFourEmptyDef(row, col); //1-3
					findFour2_2_Def(row, col); //2-2
					findFiveDef(row, col);

					findOneOff(row, col);
					findTwoOff(row, col);
					findThreeOff(row, col);
					findThreeEmptyOff(row, col);
					findFourOff(row, col);
					findFourEmptyOff(row, col);
					findFiveOff(row, col);
					findFour2_2_Off(row, col); //2-2


				}				
			}
		}
	
	}
	public boolean isOnBoard(int r, int c)
	{
		boolean isOn = false;
		
		if(r >= 0 && r < PenteGameBoard.NUM_SQUARES_SIDE)
		{
			if(c >= 0 && c < PenteGameBoard.NUM_SQUARES_SIDE)
			{
				isOn = true;
			}
		}
		return isOn;
	}
	
	public void setMove(int r, int c, int p, int t)
	{
		if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStone2Taken() == false)
		{
			if(myGame.darkSquareProblemComputerMoveList(r, c) == false)
			{
				CMObject newMove = new CMObject();
				newMove.setRow(r);
				newMove.setCol(c);
				newMove.setPriority(p);
				newMove.setMoveType(t);
				moves.add(newMove);
			}else
			{
				//System.out.println("There is a dark square problem: ");
				//System.out.println("\tmove at [" + r + "," + c + "] is being thrown out");
			}
		}
		else
		{
			CMObject newMove = new CMObject();
			newMove.setRow(r);
			newMove.setCol(c);
			newMove.setPriority(p);
			newMove.setMoveType(t);
			moves.add(newMove);
		}
	}
	public void findOneDef(int r, int c)
	{
		for(int rL = -1; rL <= 1; rL++)
		{
			for(int uD = -1; uD <=1; uD++)
			{
				try 
				{
					if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY)
					{
						setMove(r + rL, c + uD,ONE_IN_A_ROW_DEF, DEFENSE);
					}
				} catch(ArrayIndexOutOfBoundsException e) 
				{
					System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
				}
			}
		}
	}
	public void findTwoDef(int r, int c)
	{
		for(int rL = -1; rL <= 1; rL++)
		{
			for(int uD = -1; uD <=1; uD++)
			{
				try 
				{
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rL * 2)][c + (uD * 2)].getState() == PenteGameBoard.EMPTY)
						{
							//if r-rL is the wall
							if(isOnBoard(r - rL, c - uD) == false) 
							{
								setMove(r + (rL*2), c + (uD*2),TWO_IN_A_ROW_DEF, DEFENSE);
							} else if(myGame.getBoard()[r - rL][c - uD].getState() == PenteGameBoard.EMPTY)
							{
								setMove(r + (rL*2), c + (uD*2),TWO_IN_A_ROW_OPEN, DEFENSE);
							} else if(myGame.getBoard()[r - rL][c - uD].getState() == myStone)
							{
								setMove(r + (rL*2), c + (uD*2), TWO_IN_A_ROW_CAP, DEFENSE);
							}
							
							
							//if r-rL, c-UD is open
							
							//if r-rL, c-uD is us
							
							//if r-rL, c-uD is opponent (we dont care)

							
						}
					}
				} catch(ArrayIndexOutOfBoundsException e) 
				{
					//System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
				}
			}
		}
	}
	public void findThreeDef(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{

							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == PenteGameBoard.EMPTY)
							{
								setMove(r + (rl * 3),c + (ud *3), THREE_DEF, DEFENSE);
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findThreeEmptyDef(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == PenteGameBoard.EMPTY)
						{
							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == myStone * -1)
							{
								setMove(r + (rl * 2),c + (ud *2), THREE_EMPTY_DEF, DEFENSE);
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFourDef(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == myStone * -1)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() == PenteGameBoard.EMPTY)
								{
									setMove(r + (rl * 4),c + (ud *4), FOUR_DEF, DEFENSE);
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFourEmptyDef(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{
							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == PenteGameBoard.EMPTY)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() == myStone * -1)
								{
								setMove(r + (rl * 3),c + (ud *3), FOUR_EMPTY_DEF, DEFENSE);
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFour2_2_Def(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == PenteGameBoard.EMPTY)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() ==  myStone * -1)
								{
									if(myGame.getBoard()[r + (rl* 5)][c + (ud* 5)].getState() ==  myStone * -1)
									{
									setMove(r + (rl * 3),c + (ud * 3), FOUR_2_2_DEF, DEFENSE);
									}
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFiveDef(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == myStone * -1)
							{
								if(myGame.getBoard()[r + (rl*4)][c + (ud*4)].getState() == myStone * -1)
								{
									if(myGame.getBoard()[r + (rl* 5)][c + (ud* 5)].getState() == PenteGameBoard.EMPTY)
									{
									setMove(r + (rl * 5),c + (ud *5), FIVE_DEF, DEFENSE);
									}
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	
	public void findOneOff(int r, int c)
	{
		for(int rL = -1; rL <= 1; rL++)
		{
			for(int uD = -1; uD <=1; uD++)
			{
				try 
				{
					if(myGame.getBoard()[r + rL][c + uD].getState() == PenteGameBoard.EMPTY)
					{
						setMove(r + rL, c + uD,ONE_IN_A_ROW_OFF, OFFENSE);
					}
				} catch(ArrayIndexOutOfBoundsException e) 
				{
					System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
				}
			}
		}
	}
	public void findTwoOff(int r, int c)
	{
		for(int rL = -1; rL <= 1; rL++)
		{
			for(int uD = -1; uD <=1; uD++)
			{
				try 
				{
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rL * 2)][c + (uD * 2)].getState() == PenteGameBoard.EMPTY)
						{
							//if r-rL is the wall
							if(isOnBoard(r - rL, c - uD) == false) 
							{
								setMove(r + (rL*2), c + (uD*2),TWO_IN_A_ROW_OFF, OFFENSE);
							}
							
							
							//if r-rL, c-UD is open
							
							//if r-rL, c-uD is us
							
							//if r-rL, c-uD is opponent (we dont care)

							
						}
					}
				} catch(ArrayIndexOutOfBoundsException e) 
				{
					//System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
				}
			}
		}
	}
	public void findThreeOff(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone)
						{

							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == PenteGameBoard.EMPTY)
							{
								setMove(r + (rl * 3),c + (ud *3), THREE_OFF, OFFENSE);
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findThreeEmptyOff(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == PenteGameBoard.EMPTY)
						{
							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == myStone)
							{
								setMove(r + (rl * 2),c + (ud *2), THREE_EMPTY_OFF, OFFENSE);
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFourOff(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == myStone)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() == PenteGameBoard.EMPTY)
								{
									setMove(r + (rl * 4),c + (ud *4), FOUR_OFF, OFFENSE);
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFourEmptyOff(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == PenteGameBoard.EMPTY)
						{
							if(myGame.getBoard()[r + (rl* 3)][c + (ud* 3)].getState() == myStone)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() == myStone)
								{
								setMove(r + (rl * 2),c + (ud *2), FOUR_EMPTY_OFF, OFFENSE);
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFour2_2_Off(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone * -1)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone * -1)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == PenteGameBoard.EMPTY)
							{
								if(myGame.getBoard()[r + (rl* 4)][c + (ud* 4)].getState() ==  myStone * -1)
								{
									if(myGame.getBoard()[r + (rl* 5)][c + (ud* 5)].getState() ==  myStone * -1)
									{
									setMove(r + (rl * 3),c + (ud * 3), FOUR_2_2_OFF, OFFENSE);
									}
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	public void findFiveOff(int r, int c)
	{
		for(int rl = -1; rl <= 1; rl++)
		{
			for(int ud = -1; ud <= 1; ud++)
			{
				try 
				{
					if(myGame.getBoard()[r + rl][c + ud].getState() == myStone)
					{
						if(myGame.getBoard()[r + (rl*2)][c + (ud*2)].getState() == myStone)
						{
							if(myGame.getBoard()[r + (rl*3)][c + (ud*3)].getState() == myStone)
							{
								if(myGame.getBoard()[r + (rl*4)][c + (ud*4)].getState() == myStone)
								{
									if(myGame.getBoard()[r + (rl* 5)][c + (ud* 5)].getState() == PenteGameBoard.EMPTY)
									{
										setMove(r + (rl * 5),c + (ud *5), FIVE_OFF, OFFENSE);
									}
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					//System.out.println("off the board in findOneDef at [" + r + "][" + c + "]");
				}
			}
		}
	}
	
	public int[] generateRandomMove()
	{
		int[] move = new int[2];
		
		boolean done = false;
		
		int newR, newC;
		do
		{
			newR  = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE);
			newC  = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE);
			
			if(myGame.getBoard()[newR][newC].getState() == PenteGameBoard.EMPTY)
			{
				done = true;
				move[0] = newR;
				move[1] = newC;
			}
			
		} while(!done);

		
		return move;
	}
	
	public void sleepForAMove() throws InterruptedException
	{
		Thread currThread = Thread.currentThread();
		Thread.sleep(PenteGameBoard.SLEEP_TIME);
	}
	
}
