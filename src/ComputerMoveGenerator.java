import java.util.ArrayList;

public class ComputerMoveGenerator {

	public static final int OFFENSE = 1;
	public static final int DEFENSE = -1;
	PenteGameBoard myGame;
	int myStone;
	
	
	ArrayList<CMObject> oMoves = new ArrayList<CMObject>();
	ArrayList<CMObject> dMoves = new ArrayList<CMObject>();

	
	public ComputerMoveGenerator(PenteGameBoard gb, int myStoneColor)
	{
		myStone = myStoneColor;
		myGame = gb;
		
		//System.out.println("Computer is play as player " + myStone);
	}
	
	public int[] getComputerMove()
	{
		int[] newMove = generateRandomMove();
		
		findDefMoves();
		findOffMoves();
	
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		return newMove;
	}
	
	public void findDefMoves()
	{
		findOneDef();
	}
	
	public void findOneDef()
	{
		
	}
	
	public void findOffMoves()
	{
		
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
