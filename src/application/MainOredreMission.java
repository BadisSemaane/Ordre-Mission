package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainOredreMission extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = new AnchorPane();
			root =  FXMLLoader.load(getClass().getResource("/Views/mainWindow.fxml"));
			Scene scene = new Scene(root,1360,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//primaryStage.setResizable(false);
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println("Erreur :"+e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}