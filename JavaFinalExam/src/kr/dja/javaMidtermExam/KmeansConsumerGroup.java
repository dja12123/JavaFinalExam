package kr.dja.javaMidtermExam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


public class KmeansConsumerGroup
{
	private final List<Consumer> _consumerList;
	public final List<Consumer> consumerList;
	private int[] assignments;
	private int k;
	
	public Runnable stateChangeCallback;
	
	public KmeansConsumerGroup()
	{
		this._consumerList = new ArrayList<>();
		this.consumerList = Collections.unmodifiableList(this._consumerList);
		this.assignments = new int[0];
		this.k = 0;
		

		this.stateChangeCallback = null;
	}
	
	public Consumer addConsumer(int id, int cost, int count)
	{
		Consumer c = new Consumer(id, cost, count);
		this._consumerList.add(c);
		
		if(this.stateChangeCallback != null)
		{
			this.stateChangeCallback.run();
		}
		return c;
	}
	
	public void clearConsumer()
	{
		this._consumerList.clear();
		this.assignments = new int[0];
		
		if(this.stateChangeCallback != null)
		{
			this.stateChangeCallback.run();
		}
	}
	
	public void getGroup(int k) throws Exception
	{
		this.k = k;

		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("cost", Attribute.NUMERIC));
		attributes.add(new Attribute("count", Attribute.NUMERIC));
		Instances instances = new Instances("consumer", attributes, this.consumerList.size());

		for(int i = 0; i < this.consumerList.size(); ++i)
		{
			Consumer consumer = this.consumerList.get(i);
			Instance instance = new DenseInstance(attributes.size());
			instance.setValue(0, consumer.getCost());
			instance.setValue(1, consumer.getCount());
			instances.add(instance);
		}

		SimpleKMeans kmeans = new SimpleKMeans();
		

		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(this.k);
		kmeans.buildClusterer(instances);
		this.assignments = kmeans.getAssignments();


		StringBuffer printBuffer = new StringBuffer();
		for(int i = 0; i < k; ++i)
		{
			printBuffer.append(String.format("%d그룹 = (", i + 1));
			for(int j = 0; j < this.assignments.length; ++j)
			{
				if(this.assignments[j] != i)
				{
					continue;
				}
				printBuffer.append(String.format("%d,", this._consumerList.get(j).ID));
			}
			printBuffer.delete(printBuffer.length() - 1, printBuffer.length());
			printBuffer.append(")  ");
			
		}
		System.out.println(printBuffer.toString());
		
		if(this.stateChangeCallback != null)
		{
			this.stateChangeCallback.run();
		}

		return;
	}
	
	public int[] getAssign()
	{
		return this.assignments;
	}
}


