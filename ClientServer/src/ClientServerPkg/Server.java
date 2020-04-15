package ClientServerPkg;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*; 
import java.net.*; 
//
//Server class
//
//
public class Server 
{   // no class variables declared

	public static void main(String[] args) throws IOException 
	{ 
		// server is listening on port 5056 as previous example port changed from 3142
		
		ServerSocket ss = new ServerSocket(5056); 
		
		// running infinite loop to wait for client request 
	
		while (true) //infinite while loop
		{ 
			Socket s = null; //Declare a variable s of type socket and set it to null
			
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept(); 
				
				System.out.println("A new client is connected : " + s); 
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				
				System.out.println("Assigning new thread for this client"); 

				// create a new thread object 
				Thread t = new ClientHandler(s, dis, dos); //declare a new thread t of type ClientHandler

				// Invoking the start() method 
				t.start(); //Start the client handler
				
			} // End try part
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} // End catch
		} // End while
	} // End Main
} // End Server Class

//ClientHandler class 
class ClientHandler extends Thread 
{ 
	final DataInputStream dis; //Declare dis as DataInputStream
	final DataOutputStream dos; //Declare dos as DataOutputStream
	final Socket s; //Declare s as a Socket
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) 
	{ 
		this.s = s; 
		this.dis = dis; 
		this.dos = dos; 
	} 

	@Override
	public void run() 
	{ 
		//Declare string for receive#
		String received; 
		
		//Infinite loop setup
		
		while (true) 
		{ 
			try { 

				// Asks user what they want to do 
				dos.writeUTF("Would you like to start the quiz? Yes | No \n"+ 
							"Type Exit to terminate connection."); 
				
				// receive the answer from client 
				received = dis.readUTF(); 
				
				if(received.equals("Exit")) 
				{ 
					System.out.println("Client " + this.s + " sends exit..."); 
					System.out.println("Closing this connection."); 
					this.s.close(); 
					System.out.println("Connection closed"); 
					break; 
				}  
				
				// write on output stream based on the 
				// answer from the client 
				switch (received) { 
				
					case "Yes" : 
						String filePath = "C:\\Users\\thomas\\Documents\\soft carp assign, ST20131771, CIS5003_T1_19, WRIT1\\questionaire.xml";
				        File xmlFile = new File(filePath);
				        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder dBuilder = null;
				            try {
								dBuilder = dbFactory.newDocumentBuilder();
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				            Document doc = null;
							try {
								doc = dBuilder.parse(xmlFile);
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				            doc.getDocumentElement().normalize();
				            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				            NodeList nodeList = doc.getElementsByTagName("questionaire"); 
						
					case "No" : 
						System.out.println("Client " + this.s + " sends no...");
						System.out.println("Closing this connection."); 
						this.s.close(); 
						System.out.println("Connection closed"); 
						break; 
						
					default: 
						dos.writeUTF("Invalid input"); 
						break; 
				} 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
		} 
		
		try
		{ 
			// closing resources 
			this.dis.close(); 
			this.dos.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	
} 
}
