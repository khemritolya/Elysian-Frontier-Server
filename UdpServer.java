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
    public static void main(String[] args) throws IOException
    {
		listenAt(1234);
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
 
            // Step 3 : revieve the data in byte buffer.
            ds.receive(dpReceive);
 
            System.out.println("Client:-" + bufToStringBuilder(receive));
 
            // Exit the server if the client sends "bye"
            if (bufToStringBuilder(receive).toString().equals("bye"))
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
        }
	}
	
	
    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder bufToStringBuilder(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}