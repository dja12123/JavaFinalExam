package kr.dja.javaFinalExam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import kr.dja.javaFinalExam.ui.UIMain;
import kr.dja.javaMidtermExam.KmeansConsumerGroup;

public class ConsumerAnalyzerCore extends Application
{
	public static final ExecutorService mainThreadPool = Executors.newCachedThreadPool(new ThreadFactory()
	{
		public Thread newThread(Runnable r)
		{
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		}
	});

	public static void main(String[] args)
	{
		
		InputStream is = ConsumerAnalyzerCore.class.getResourceAsStream("/sampleData/BlackFriday.csv");

		launch();
	}

	private KmeansConsumerGroup group;
	private HashMap<Integer, EX_Consumer> exConsumers;
	private HashMap<String, DataType> dataTypes;
	private UIMain uiMain;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.group = new KmeansConsumerGroup();
		this.exConsumers = new HashMap<>();
		this.dataTypes = new HashMap<>();
		
		this.uiMain = new UIMain(primaryStage, this.group, this.exConsumers, this.dataTypes, this::loadDataCallback);
	}
	
	@Override
	public void stop()
	{

	}

	private void loadDataCallback(InputStream is)
	{
		DataSet ds = DataSet.getDataSetCSV(is);

		this.exConsumers.clear();
		this.group.clearConsumer();
		
		DataTypeBuilder genderBuilder = new DataTypeBuilder("gender");
		DataTypeBuilder ageBuilder = new DataTypeBuilder("age");
		DataTypeBuilder occupationBuilder = new DataTypeBuilder("occupation");
		DataTypeBuilder cityCatBuilder = new DataTypeBuilder("cityCat");
		DataTypeBuilder currentCityYearsBuilder = new DataTypeBuilder("currentCityYears");
		DataTypeBuilder isMaritalBuilder = new DataTypeBuilder("isMarital");
		
		ds.data.stream().parallel().forEach((String[] column)->
		{
			int id = Integer.parseInt(column[0]);
			String gender = column[2];
			String[] tmpArr = column[3].split("-|\\+");
			int age = Integer.parseInt(tmpArr[tmpArr.length - 1]);
			int occupation = Integer.parseInt(column[4]);
			String cityCat = column[5];
			int currentCityYears = Integer.parseInt(column[6].replace("+", ""));
			boolean isMarital = column[7].equals("1") ? true : false;
			
			genderBuilder.addData(gender);
			ageBuilder.addData(String.valueOf(age));
			occupationBuilder.addData(String.valueOf(occupation));
			cityCatBuilder.addData(cityCat);
			currentCityYearsBuilder.addData(String.valueOf(currentCityYears));
			isMaritalBuilder.addData(String.valueOf(isMarital));
			
			synchronized (this.exConsumers)
			{
				EX_Consumer consumer = this.exConsumers.getOrDefault(id, null);
				if(consumer == null)
				{
					consumer = new EX_Consumer(id, gender, age, occupation, cityCat, currentCityYears, isMarital);
					this.exConsumers.put(consumer.id, consumer);
				}
				String productID = column[1];
				int price = Integer.parseInt(column[11]);
				consumer.addProduct(productID, price);
			}
		});
		this.dataTypes.put(genderBuilder.name, genderBuilder.createInstance());
		this.dataTypes.put(ageBuilder.name, ageBuilder.createInstance());
		this.dataTypes.put(occupationBuilder.name, occupationBuilder.createInstance());
		this.dataTypes.put(cityCatBuilder.name, cityCatBuilder.createInstance());
		this.dataTypes.put(currentCityYearsBuilder.name, currentCityYearsBuilder.createInstance());
		this.dataTypes.put(isMaritalBuilder.name, isMaritalBuilder.createInstance());

		this.exConsumers.values().stream().parallel().forEach((EX_Consumer c)->
		{
			int freq = c.products.size();
			if(freq == 0)
			{
				synchronized (this.group)
				{
					this.group.addConsumer(c.id, 0, 0);
				}
				return;
			}
			
			int sum = 0;
			for(int j = 0; j < freq; ++j)
			{
				sum += c.products.get(j).price;
			}
			int avgPrice = sum / freq;
			synchronized (this.group)
			{
				this.group.addConsumer(c.id, avgPrice, freq);
			}
		});
	}
}
