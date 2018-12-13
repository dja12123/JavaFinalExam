package kr.dja.javaMidtermExam;

import java.util.Random;

public class Core
{
	private static final int K = 4;
	
	public static final int MAXCOST = 1000;
	public static final int MAXCOUNT = 1000;
	
	private static final int RANDOMGENCOUNT = 50;
	
	private int idCount;
	
	private KmeansConsumerGroup group;
	private UI ui;
	
	private Core()
	{
		this.idCount = 1;
		
		this.group = new KmeansConsumerGroup();
		this.ui = new UI();
		this.ui.setBaseXY(MAXCOST, MAXCOUNT);
		this.ui.panelClickCallback = this::clickCallback;
		this.ui.resetButtonCallback = this::reset;
		this.ui.randomGenButtonCallback = this::randomGen;
		this.ui.kMeansButtonCallback = this::kmeans;
		
		this.ui.setConsumerGroup(this.group);
		
		this.ui.start();
	}
	
	private void clickCallback(int x, int y)
	{
		System.out.printf("click: %d, %d\n", x, y);
		this.group.addConsumer(this.idCount++, x, y);
	}
	
	private void reset()
	{
		this.group.clearConsumer();
	}
	
	private void randomGen()
	{
		System.out.println("randomGen");
		Random r = new Random();
		for(int i = 0; i < RANDOMGENCOUNT; ++i)
		{
			int cost = r.nextInt(MAXCOST);
			int count = r.nextInt(MAXCOUNT);
			this.group.addConsumer(this.idCount++, cost, count);
		}
	}
	
	private void kmeans()
	{
		try
		{
			this.group.getGroup(K);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		Core coreInst = new Core();
	}	
}
