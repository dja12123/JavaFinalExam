package kr.dja.javaFinalExam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConsumerAnalyzerCore extends Application
{
	public static void main(String[] args)
	{
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
