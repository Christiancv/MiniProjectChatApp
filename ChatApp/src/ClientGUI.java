
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGUI extends Application{
	
	private TextArea messages = new TextArea();
	private ChatClient connection = createClient();
	
	private Parent chat() {
		messages.setPrefHeight(500);
		TextField input = new TextField();
		
		input.setOnAction(event ->{
		String message =  "Client: ";
		message += input.getText();
		input.clear();
		
		messages.appendText(message + "\n");
		try {
			connection.send(message);
			
		}catch(Exception e) {
			messages.appendText("Failed to send");
		}
		});
		
		VBox root = new VBox(20, messages, input);
		root.setPrefSize(600,600);
		return root;
		
	}
	
	@Override public void init() throws Exception{
		connection.startConnection();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setScene(new Scene(chat()));
		primaryStage.show();
		
	}
	
	public void stop() throws Exception{
		connection.closeConnection();
	}
	
	private ChatClient createClient() {
		return new ChatClient("127.0.0.1", 56789, data ->{
			Platform.runLater(() ->{
				messages.appendText(data.toString() + "\n");
			});
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}