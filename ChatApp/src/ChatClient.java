

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public  class ChatClient {
	
	private int port;
	private String IP;

	
	private ChatThread thread = new ChatThread();
	private Consumer<Serializable> callback;
	
	public ChatClient(Consumer<Serializable> callback) {
		this.callback = callback;
		thread.setDaemon(true);
	}
	
	public ChatClient(String IP, int port, Consumer<Serializable> callback) {
		this.callback = callback;
		this.port = port;
		this.IP = IP;
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
	
	private String getIP() {
		return IP;
	}
	
	private class ChatThread extends Thread{

		
		private Socket socket;
		private ObjectOutputStream out;

		@Override
		public void run() {
			try {
				socket = new Socket(getIP(), getPort());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				
				this.out = out;
				this.socket =  socket;

				
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
