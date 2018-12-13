package kr.dja.javaFinalExam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConsumerAnalyzerCore extends Application
{
	public static void main(String[] args)
	{
		float[][] indat = new float[2880][6]; // CSV ������ �а� ������ �迭 ���� , arraylist�� ���� ���� �ٸ� ���� �����ص� �������

		try
		{
			// csv ������ ����
			InputStream is = ConsumerAnalyzerCore.class.getResourceAsStream("/gui/main.fxml");
			

		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("/gui/main.fxml"));
		primaryStage.setTitle("FXML Welcome");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
