package kr.dja.javaMidtermExam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI
{
	public static final int PANELPADDING = 30;
	
	private JFrame jframe;
	private DotPanel dotPanel;
	private JButton resetButton;
	private JButton randomGenButton;
	private JButton kMeansButton;
	
	public BiConsumer<Integer, Integer> panelClickCallback;
	public Runnable resetButtonCallback;
	public Runnable randomGenButtonCallback;
	public Runnable kMeansButtonCallback;

	private int baseX;
	private int baseY;
	
	private KmeansConsumerGroup consumerGroup;
	
	public UI()
	{	
		this.jframe = new JFrame();
		this.dotPanel = new DotPanel();
		this.resetButton = new JButton("reset");
		this.randomGenButton = new JButton("random");
		this.kMeansButton = new JButton("KMeans");
		
		this.panelClickCallback = null;
		this.resetButtonCallback = null;
		this.randomGenButtonCallback = null;
		this.kMeansButtonCallback = null;
		
		this.baseX = 0;
		this.baseY = 0;
		
		this.consumerGroup = null;
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(this.resetButton);
		buttonPanel.add(this.randomGenButton);
		buttonPanel.add(this.kMeansButton);
		
		Container c = this.jframe.getContentPane();
		c.add(this.dotPanel, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
		
		this.dotPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(UI.this.panelClickCallback == null)
				{
					return;
				}
				
				if(!(e.getX() > UI.PANELPADDING && e.getX() < UI.this.dotPanel.getWidth() - UI.PANELPADDING))
				{
					return;
				}
				if(!(e.getY() > UI.PANELPADDING && e.getY() < UI.this.dotPanel.getHeight() - UI.PANELPADDING))
				{
					return;
				}
				int panelWidth = UI.this.dotPanel.getWidth() - (UI.PANELPADDING * 2);
				int panelHeight = UI.this.dotPanel.getHeight() - (UI.PANELPADDING * 2);
				
				int x = (int)((double)(e.getX() - UI.PANELPADDING) / panelWidth * UI.this.baseX);
				int y = (int)((double)(e.getY() - UI.PANELPADDING) / panelHeight * UI.this.baseY);
				
				if(!(x >= 0 && x < UI.this.baseX))
				{
					return;
				}
				if(!(y >= 0 && y < UI.this.baseY))
				{
					return;
				}
				
				UI.this.panelClickCallback.accept(x, y);
			}
		});
		
		this.resetButton.addActionListener((ActionEvent e)->
		{
			if(this.resetButtonCallback == null)
			{
				return;
			}
			this.resetButtonCallback.run();
		});
	
		this.randomGenButton.addActionListener((ActionEvent e)->
		{
			if(this.randomGenButtonCallback == null)
			{
				return;
			}
			this.randomGenButtonCallback.run();
		});
		
		this.kMeansButton.addActionListener((ActionEvent e)->
		{
			if(this.kMeansButtonCallback == null)
			{
				return;
			}
			this.kMeansButtonCallback.run();
		});
	}
	
	public void start()
	{
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setSize(500, 500);
		this.jframe.setVisible(true);
	}
	
	public void setBaseXY(int x, int y)
	{
		this.baseX = x;
		this.baseY = y;
		this.dotPanel.setBaseXY(x, y);
	}
	
	public void setConsumerGroup(KmeansConsumerGroup group)
	{
		if(this.consumerGroup != null)
		{
			this.consumerGroup.stateChangeCallback = null;
		}
		
		this.consumerGroup = group;
		
		this.consumerGroup.stateChangeCallback = this::update;
		this.update();
	}
	
	public void update()
	{
		if(this.consumerGroup == null)
		{
			return;
		}
		this.dotPanel.updateList(this.consumerGroup.getAssign(), this.consumerGroup.consumerList);
		this.dotPanel.repaint();
	}
}

class DotPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static Color[] colorArr = new Color[] {Color.RED, Color.BLUE, Color.ORANGE, Color.PINK, Color.CYAN, Color.GREEN, Color.YELLOW, Color.MAGENTA};
	
	private List<Consumer> allConsumer;
	private int[] assign;
	
	private int baseX, baseY;
	
	public DotPanel()
	{
		this.setLayout(null);
		
		this.baseX = 0;
		this.baseY = 0;
		
	}
	
	public void updateList(int[] assign, List<Consumer> consumerList)
	{
		this.assign = assign;
		this.allConsumer = consumerList;
		
		this.repaint();
		
	}

	public void setBaseXY(int x, int y)
	{
		this.baseX = x;
		this.baseY = y;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		int width = this.getWidth() - (UI.PANELPADDING * 2);
		int height = this.getHeight() - (UI.PANELPADDING * 2);
		int[] xy;
		Consumer consumer;
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(UI.PANELPADDING, UI.PANELPADDING, width, height);
		
		List<Consumer> undrawConsumer = new ArrayList<Consumer>();
		undrawConsumer.addAll(this.allConsumer);
		
		for(int i = 0; i < this.assign.length; ++i)
		{
			int group = this.assign[i];
			g.setColor(colorArr[group]);
			consumer = this.allConsumer.get(i);
			undrawConsumer.remove(consumer);
			xy = this.getVisibleXY(consumer.getCost(), consumer.getCount(), width, height);
			g.drawString(String.format("G%d ID%d(CST%d CNT%d)", group, consumer.ID, consumer.getCost(), consumer.getCount()), xy[0] - 20, xy[1] + 20);
			g.drawOval(xy[0] - 1, xy[1] - 1, 2, 2);
		}
		
		g.setColor(Color.GRAY);
		
		for(int i = 0; i < undrawConsumer.size(); ++i)
		{
			consumer = undrawConsumer.get(i);
			xy = this.getVisibleXY(consumer.getCost(), consumer.getCount(), width, height);
			g.drawString(String.format("ID%d(CST%d, CNT%d)", consumer.ID, consumer.getCost(), consumer.getCount()), xy[0] - 20, xy[1] + 20);
			g.drawOval(xy[0] - 1, xy[1] - 1, 2, 2);
		}
	}
	
	private int[] getVisibleXY(double structX, double structY, int width, int height)
	{
		int x = (int)(structX / this.baseX * width) + UI.PANELPADDING;
		int y = (int)(structY / this.baseY * height) + UI.PANELPADDING;
		return new int[] {x,y};
	}
}