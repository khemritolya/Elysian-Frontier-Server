// Java program to illustrate Server side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
 
//import networking.udpBaseClient;
 
public class UdpServer
{
    public static void run()
    {
        try {
            listenAt(1234);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
	
	public static void listenAt(int port) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		// Step 1 : Create a socket to listen at port `port`
        DatagramSocket ds = new DatagramSocket(port);
        byte[] receive = new byte[65535];
 
        DatagramPacket dpReceive = null;
        while (true)
        {
 
            // Step 2 : create a DatagramPacket to receive the data.
            dpReceive = new DatagramPacket(receive, receive.length);
 
            // Step 3 : receive the data in byte buffer.
            ds.receive(dpReceive);
 
            System.out.println("Client:-" + Utilities.bufToStringBuilder(receive));
 
            // Exit the server if the client sends "bye"
            if (Utilities.bufToStringBuilder(receive).toString().equals("bye"))
            {
                System.out.println("Client sent bye.....EXITING");
                break;
            }
 
            // Clear the buffer after every message.
            receive = new byte[65535];
			
			System.out.print(">");
			String inp = sc.nextLine();
			byte[] buf = inp.getBytes();

			dpReceive.setData(buf);
			ds.send(dpReceive);

			if (inp.equals("bye"))
			    break;
        }
	}
}