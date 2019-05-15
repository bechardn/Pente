
public class CMObject implements Comparable<Object>{ 
	
	private int priority = 0;
	private int row = -1, col = -1;
	private int moveType = 0; //offense or defense
	
	public void setPriority(int newP)
	{
		priority = newP;
	}

	public void setRow(int newR)
	{
		row = newR;
	}
	
	public void setCol(int newC)
	{
		col = newC;
	}
	
	public void setMoveType(int newT)
	{
		moveType = newT;
	}
	
	public int getPriority()
	{
		return priority;
	}
	public Integer getPriorityInt()
	{
		return new Integer(priority);
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public int getType()
	{
		return moveType;
	}
	
	public String toString()
	{
		return "Move at: [" + row + ", " + col + "] priority: " + priority;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int comparePriority = ((CMObject)o).getPriority();
		return comparePriority - this.priority;
	}
	
}
