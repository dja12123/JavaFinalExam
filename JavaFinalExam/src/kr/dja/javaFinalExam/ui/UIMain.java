package kr.dja.javaFinalExam.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import kr.dja.javaFinalExam.ConsumerAnalyzerCore;
import kr.dja.javaFinalExam.DataType;
import kr.dja.javaFinalExam.DataTypeBuilder;
import kr.dja.javaFinalExam.EX_Consumer;
import kr.dja.javaMidtermExam.KmeansConsumerGroup;

public class UIMain
{
	private final KmeansConsumerGroup group;
	private final HashMap<Integer, EX_Consumer> exConsumers;
	private final HashMap<String, DataType> dataTypes;
	private final Consumer<InputStream> readDataCallback;
	
	private final ArrayList<EX_Consumer> vipGroup;
	private final ArrayList<EX_Consumer> highFreqGroup;
	private final ArrayList<EX_Consumer> highPriceGroup;
	private final ArrayList<EX_Consumer> normalGroup;
	
	private final ArrayList<XYChart.Data<Number, Number>> chartData;
	
	private ScatterChart<Number, Number> scatterChart_mainChart;
	
	private CheckBox checkBox_groupVIP;
	private CheckBox checkBox_groupHighFreq;
	private CheckBox checkBox_groupHighPrice;
	private CheckBox checkBox_groupNormal;
	
	private ListView<String> listView_consumerType;
	private ListView<String> listView_consumerData;
		
	private Button btn_loadSample;
	private Button btn_kMeans;
	private Button btn_analyze;

	private Label label_info;
	
	private String selectType;

	public UIMain(Stage primaryStage, KmeansConsumerGroup group, HashMap<Integer, EX_Consumer> exConsumers, HashMap<String, DataType> dataTypes, Consumer<InputStream> readDataCallback)
	{
		this.group = group;
		this.exConsumers = exConsumers;
		this.dataTypes = dataTypes;
		this.readDataCallback = readDataCallback;
		
		this.chartData = new ArrayList<>();
		
		this.vipGroup = new ArrayList<EX_Consumer>();
		this.highFreqGroup = new ArrayList<EX_Consumer>();
		this.highPriceGroup = new ArrayList<EX_Consumer>();
		this.normalGroup = new ArrayList<EX_Consumer>();
		
		Parent root;
		try
		{
			root = FXMLLoader.load(getClass().getResource("/gui/main.fxml"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		primaryStage.setTitle("ConsumerAnalyzer");
		
		Scene sc = new Scene(root);
		primaryStage.setScene(sc);
		primaryStage.show();
		
		this.scatterChart_mainChart = (ScatterChart<Number, Number>)sc.lookup("#scatterChart_mainChart");
		
		this.checkBox_groupVIP = (CheckBox)sc.lookup("#checkBox_groupVIP");
		this.checkBox_groupHighFreq = (CheckBox)sc.lookup("#checkBox_groupHighFreq");
		this.checkBox_groupHighPrice = (CheckBox)sc.lookup("#checkBox_groupHighPrice");
		this.checkBox_groupNormal = (CheckBox)sc.lookup("#checkBox_groupNormal");

		this.listView_consumerType = (ListView<String>)sc.lookup("#listView_consumerType");
		this.listView_consumerData = (ListView<String>)sc.lookup("#listView_consumerData");
		
		this.btn_loadSample = (Button)sc.lookup("#btn_loadSample");
		this.btn_kMeans = (Button)sc.lookup("#btn_kMeans");
		this.btn_analyze = (Button)sc.lookup("#btn_analyze");
		
		this.label_info = (Label)sc.lookup("#label_info");
		
		this.scatterChart_mainChart.getXAxis().setLabel("구매횟수");
		this.scatterChart_mainChart.getYAxis().setLabel("구매평균가");
		
		this.listView_consumerType.setOnMouseClicked(this::listView_consumerType_mouseEvent);
		this.listView_consumerData.setOnMouseClicked(this::listView_consumerData_mouseEvent);
		
		this.btn_loadSample.setOnAction(this::btn_loadSample_action);
		this.btn_kMeans.setOnAction(this::btn_kMeans_action);
	}

	private void listView_consumerType_mouseEvent(MouseEvent event)
	{
		String str = this.listView_consumerType.getSelectionModel().getSelectedItem();
		if (str == null) return;
		DataType type = this.dataTypes.get(str);
		ObservableList<String> items = this.listView_consumerData.getItems();
		items.clear();
		if (type == null) return;
		this.selectType = str;
		for(int i = 0; i < type.kinds.length; ++i)
		{
			items.add(type.kinds[i]);
		}
	}
	
	private XYChart.Series<Number, Number> highlightSeries;

	private void listView_consumerData_mouseEvent(MouseEvent event)
	{
		
		String str = this.listView_consumerData.getSelectionModel().getSelectedItem();
		if(str == null) return;
		
		DataType type = this.dataTypes.getOrDefault(this.selectType, null);
		if(type == null) return;
		
		System.out.println("clicked on " + type.name + " " + str);
		if(highlightSeries != null) this.scatterChart_mainChart.getData().remove(highlightSeries);
		highlightSeries = new XYChart.Series<>();
		highlightSeries.setName("강조");
		
		for(int i = 0; i < this.chartData.size(); ++i)
		{
			EX_Consumer c = (EX_Consumer)this.chartData.get(i).getExtraValue();
			boolean isHighlight = false;
			switch(type.name)
			{
			case "gender":
				if(c.gender.equals(str)) isHighlight = true; break;
			case "age":
				if(String.valueOf(c.age).equals(str)) isHighlight = true; break;
			case "occupation":
				if(String.valueOf(c.occupation).equals(str)) isHighlight = true; break;
			case "cityCat":
				if(c.cityCat.equals(str)) isHighlight = true; break;
			case "currentCityYears":
				if(String.valueOf(c.currentCityYears).equals(str)) isHighlight = true; break;
			case "isMarital":
				if(String.valueOf(c.isMarital).equals(str)) isHighlight = true; break;
			}
			if(isHighlight)
			{
				XYChart.Data<Number, Number> data =  new XYChart.Data<Number, Number>(c.products.size(), c.getAvgPrice());
				highlightSeries.getData().add(data);
			}
		}
		
		this.scatterChart_mainChart.getData().add(highlightSeries);
	}

	private void btn_loadSample_action(ActionEvent e)
	{
		this.setkMeansButtonActive(false);
		this.label_info.setText("샘플 로드중");
		ConsumerAnalyzerCore.mainThreadPool.execute(this::loadSampleExecute);
	}
	
	private void loadSampleExecute()
	{
		this.readDataCallback.accept(ConsumerAnalyzerCore.class.getResourceAsStream("/sampleData/BlackFriday.csv"));
		Platform.runLater(()->
		{
			this.setkMeansButtonActive(true);
			this.label_info.setText("샘플 로드 완료");
		});
	}
	
	private void btn_kMeans_action(ActionEvent e)
	{
		this.setkMeansButtonActive(false);
		this.label_info.setText("kMeans 연산중");
		ConsumerAnalyzerCore.mainThreadPool.execute(this::kMeansExecute);
	}
	
	private void kMeansExecute()
	{
		try
		{
			this.group.getGroup(4);
		}
		catch(Exception e)
		{
			Platform.runLater(()->
			{
				this.setkMeansButtonActive(true);
				this.label_info.setText("kMeans 연산 오류: " + e.getMessage());
			});
			return;
		}
		
		ArrayList<ConsumerGroup> tempGroups = new ArrayList<>();
		tempGroups.add(getGroup(this.group, 0, this.exConsumers));
		tempGroups.add(getGroup(this.group, 1, this.exConsumers));
		tempGroups.add(getGroup(this.group, 2, this.exConsumers));
		tempGroups.add(getGroup(this.group, 3, this.exConsumers));
		
		this.assignConsumerGroup(tempGroups);
		
		Platform.runLater(()->
		{
			XYChart.Series<Number, Number> seriesVIP;
			XYChart.Series<Number, Number> seriesHighFreq;
			XYChart.Series<Number, Number> seriesHighPrice;
			XYChart.Series<Number, Number> seriesNormal;
			
			seriesVIP = new XYChart.Series<>();
			seriesVIP.setName("VIP");
			seriesHighFreq = new XYChart.Series<>();
			seriesHighFreq.setName("자주구매");
			seriesHighPrice = new XYChart.Series<>();
			seriesHighPrice.setName("고가구매");
			seriesNormal = new XYChart.Series<>();
			seriesNormal.setName("일반");
			
			this.scatterChart_mainChart.getData().clear();
			this.chartData.clear();
			this.showChart(seriesVIP, this.vipGroup);
			this.showChart(seriesHighFreq, this.highFreqGroup);
			this.showChart(seriesHighPrice, this.highPriceGroup);
			this.showChart(seriesNormal, this.normalGroup);
			this.scatterChart_mainChart.getData().addAll(seriesVIP, seriesHighFreq, seriesHighPrice, seriesNormal);
			
			ObservableList<String> items = this.listView_consumerType.getItems();
			items.clear();
			for(DataType dt : this.dataTypes.values())
			{
				items.add(dt.name);
			}
			
			this.setkMeansButtonActive(true);
			this.label_info.setText("kMeans 연산 완료");
		});
	}
	
	private void assignConsumerGroup(ArrayList<ConsumerGroup> tempGroups)
	{
		this.vipGroup.clear();
		this.highFreqGroup.clear();
		this.highPriceGroup.clear();
		this.normalGroup.clear();
		
		ConsumerGroup tempGroup;
		int temp;
		
		tempGroup = tempGroups.get(0);
		temp = tempGroups.get(0).freqAvg * tempGroups.get(0).priceAvg;
		for(int i = 1; i < 3; ++i)
		{// 구매 횟수 * 평균이 가장 높은 고객 그룹
			int x = tempGroups.get(i).freqAvg * tempGroups.get(i).priceAvg;
			if(x > temp)
			{
				tempGroup = tempGroups.get(i);
				temp = x;
			}
		}
		tempGroups.remove(tempGroup);
		this.vipGroup.addAll(tempGroup.list);
		
		tempGroup = tempGroups.get(0);
		temp = tempGroups.get(0).freqAvg * tempGroups.get(0).priceAvg;
		for(int i = 1; i < 2; ++i)
		{// 구매 횟수 * 평균이 가장 적은 고객 그룹
			int x = tempGroups.get(i).freqAvg * tempGroups.get(i).priceAvg;
			if(x < temp)
			{
				tempGroup = tempGroups.get(i);
				temp = x;
			}
		}
		tempGroups.remove(tempGroup);
		this.normalGroup.addAll(tempGroup.list);
		
		double freqDist = (double)Math.min(tempGroups.get(0).freqAvg, tempGroups.get(1).freqAvg)
				/ (double)Math.max(tempGroups.get(0).freqAvg, tempGroups.get(1).freqAvg);
		
		double priceDist = (double)Math.min(tempGroups.get(0).priceAvg, tempGroups.get(1).priceAvg)
				/ (double)Math.max(tempGroups.get(0).priceAvg, tempGroups.get(1).priceAvg);
		
		if(freqDist > priceDist)
		{// 비교 대상 선택(구매횟수 or 구매가격) 차이가 큰 것을 선택.
			if(tempGroups.get(0).freqAvg > tempGroups.get(1).freqAvg)
			{
				this.highFreqGroup.addAll(tempGroups.get(0).list);
				this.highPriceGroup.addAll(tempGroups.get(1).list);
			}
			else
			{
				this.highFreqGroup.addAll(tempGroups.get(1).list);
				this.highPriceGroup.addAll(tempGroups.get(0).list);
			}
		}
		else
		{
			if(tempGroups.get(0).priceAvg > tempGroups.get(1).priceAvg)
			{
				this.highPriceGroup.addAll(tempGroups.get(0).list);
				this.highFreqGroup.addAll(tempGroups.get(1).list);
			}
			else
			{
				this.highPriceGroup.addAll(tempGroups.get(1).list);
				this.highFreqGroup.addAll(tempGroups.get(0).list);
			}
		}
	}
	
	private void setkMeansButtonActive(boolean state)
	{
		this.btn_loadSample.setDisable(!state);
		this.btn_kMeans.setDisable(!state);
		this.btn_analyze.setDisable(!state);
	}
	
	private void showChart(XYChart.Series<Number, Number> series, ArrayList<EX_Consumer> group)
	{
		series.getData().clear();
		
		for(int i = 0; i < group.size(); ++i)
		{
			EX_Consumer c = group.get(i);
			XYChart.Data<Number, Number> data =  new XYChart.Data<Number, Number>(c.products.size(), c.getAvgPrice(), c);
			this.chartData.add(data);
			series.getData().add(data);
		}

	}
	
	private static ConsumerGroup getGroup(KmeansConsumerGroup group, int no, HashMap<Integer, EX_Consumer> exConsumers)
	{
		ArrayList<EX_Consumer> list = new ArrayList<>();
		int[] assign = group.getAssign();
		int freqAvg = 0;
		int priceAvg = 0;
		
		for(int i = 0; i < assign.length; ++i)
		{
			if(assign[i] == no)
			{
				EX_Consumer c = exConsumers.get(group.consumerList.get(i).ID);
				freqAvg += c.products.size();
				priceAvg += c.getAvgPrice();
				list.add(c);
			}
		}
		
		freqAvg /= list.size();
		priceAvg /= list.size();
		
		return new ConsumerGroup(freqAvg, priceAvg, list);
	}
}

