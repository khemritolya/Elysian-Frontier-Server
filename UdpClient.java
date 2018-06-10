// Java program to illustrate Client side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
 
public class UdpClient
{
    public static void main(String args[]) throws IOException
    {
        sendUserInputTo(1234, InetAddress.getLocalHost());
	}
	
	public static void sendUserInputTo(int port, InetAddress ip) throws IOException {
		Scanner sc = new Scanner(System.in);
 
        // Step 1:Create the socket object for
        // carrying the data.
        DatagramSocket ds = new DatagramSocket();
 
        byte buf[] = null;
 
        // loop while user not enters "bye"
        while (true)
        {
			System.out.print(">");
            String inp = sc.nextLine();
			System.out.println();
            // convert the String input into the byte array.
            buf = inp.getBytes();
 
            // Step 2 : Create the datagramPacket for sending
            // the data.
            DatagramPacket dpSend =
                  new DatagramPacket(buf, buf.length, ip, port);
 
            // Step 3 : invoke the send call to actually send
            // the data.
            ds.send(dpSend);
			
			buf = new byte[65537];
			
			// break the loop if user enters "bye"
            if (inp.equals("bye"))
                break;
			
			DatagramPacket dpReceive = new DatagramPacket(buf, buf.length);
			ds.receive(dpReceive);
			System.out.println("Server:-" + bufToStringBuilder(buf));
			
			// clear the buffer
			buf = new byte[65537];
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
        while (a[i] != 0 && i < a.length)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}