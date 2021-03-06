import java.io.*;
import java.net.*;
import java.util.*;

public class ServerSide
{
	ArrayList<PrintWriter> clientOutputStream;
	
	public class ClientHandler implements Runnable
	{
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket)
		{
			try
			{
				sock = clientSocket;
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		public void run()
		{
			String message;
			FileWriter fileWriter = null;
			try
			{
				fileWriter = new FileWriter("/home/yash/Activity-Recognition/Server/test.csv");
				while((message = reader.readLine()) != null)
				{
					fileWriter.append(message.toString());
					fileWriter.append("\n");
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
				System.out.println("File Created");
			}finally
			{
				try
				{
					fileWriter.flush();
					fileWriter.close();
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String [] arg)
	{
		new ServerSide().go();
	}
	
	void go()
	{
		clientOutputStream = new ArrayList<PrintWriter>();
		try
		{
			ServerSocket serverSock = new ServerSocket(5000);
			while(true)
			{
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStream.add(writer);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("Connection Made");
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Connection Not Possible");
		}
	}
}
