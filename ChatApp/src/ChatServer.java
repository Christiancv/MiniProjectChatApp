import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public  class ChatServer {
	
	private int port;

	
	private ChatThread thread = new ChatThread();
	private Consumer<Serializable> callback;
	
	public ChatServer(Consumer<Serializable> callback) {
		this.callback = callback;
		thread.setDaemon(true);
	}
	
	public ChatServer(int port, Consumer<Serializable> callback) {
		this.callback = callback;
		this.port = port;
	}
	
	public void startConnection() throws Exception{
		thread.start();
	}
	
	public void send(Serializable data) throws Exception{
		thread.out.writeObject(data);
	}
	
	public void closeConnection() throws Exception{
		thread.socket.close();
	}
	
	
	
	private int getPort() {
		return port;
	}
	
	private class ChatThread extends Thread{

		
		private Socket socket;
		private ObjectOutputStream out;

		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(getPort());
				Socket socket = server.accept();
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				
				this.out = out;
				
				while(true) {
					Serializable data = (Serializable)in.readObject();
					callback.accept(data);
					
				}
				
				
				
			}catch(Exception e) {
				callback.accept("Connection closed");
			}
		}
		
	}

	

}
