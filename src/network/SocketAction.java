package network;

import java.io.*;
import java.net.*;

import views.MessageBox;

public class SocketAction extends Thread{
	
	private DataInputStream inStream = null;
	protected PrintStream outStream = null;
	private Socket socket = null;
	
	public SocketAction(Socket sock) {
		super("SocketAction");
		try {
			inStream = new DataInputStream(new BufferedInputStream(sock.getInputStream(), 7378));
			outStream = new PrintStream(new BufferedOutputStream(sock.getOutputStream(), 7378), true);
			socket = sock;
		}
		catch (IOException e) {
			System.out.println("Couldn’t initialize SocketAction:" + e);
			System.exit(1);
		}
	}
	public void send(String s) {
		outStream.println(s);
	}
	public void sendObject(Object o) {
		try{
			ObjectOutputStream objectStream = new ObjectOutputStream(socket.getOutputStream());
			objectStream.writeObject(o);
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public Object receiveObject() {
		try{
			ObjectInputStream objectStream = new ObjectInputStream(socket.getInputStream());
			return objectStream.readObject();
		} catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public String receive() throws IOException {
		return inStream.readLine();
		
	}
	public void closeConnections() {
		try {
			socket.close();
			socket = null;
		}
		catch (IOException e) {
			System.out.println("Couldn’t close socket:" + e);
		}
	}
	public boolean isConnected() {
		return ((inStream != null) && (outStream != null) && (socket != null));
	}
	protected void finalize () {
		if (socket != null) {
			try {
				socket.close();
			}
			catch (IOException e) {
				System.out.println("Couldn’t close socket:" + e);
			}
			socket = null;
		}
	}
	public Socket getSocket(){
		return socket;
	}
}
