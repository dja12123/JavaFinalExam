package kr.dja.javaMidtermExam;

public class Consumer
{
	public final int ID;
	
	private int cost;
	private int count;
	
	public Consumer(int id, int cost, int count)
	{
		this.ID = id;
		
		this.cost = cost;
		this.count = count;
	}
	
	public int getCost()
	{
		return this.cost;
	}
	
	public int getCount()
	{
		return this.count;
	}
}
