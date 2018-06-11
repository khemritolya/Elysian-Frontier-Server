// Java program to illustrate Client side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
 
public class UdpClient
{
    private static Scanner sc = new Scanner(System.in);

    public static void run()
    {
        System.out.println("Enter server's ip address:");
        try {
            InetAddress ip = InetAddress.getByName(sc.nextLine());
            sendUserInputTo(1234, ip);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}
	
	public static void sendUserInputTo(int port, InetAddress ip) throws IOException {
		Scanner sc = new Scanner(System.in);
 
        // Step 1:Create the socket object for
        // carrying the data.
        DatagramSocket ds = new DatagramSocket();
 
        byte buf[];
 
        // loop while user not enters "bye"
        while (true)
        {
			System.out.print(">");
            String inp = sc.nextLine();
            // convert the String input into the byte array.
            buf = inp.getBytes();
 
            // Step 2 : Create the datagramPacket for sending
            // the data.
            DatagramPacket dpSend = new DatagramPacket(buf, buf.length, ip, port);
 
            // Step 3 : invoke the send call to actually send
            // the data.
            ds.send(dpSend);
			
			buf = new byte[65537];
			
			// break the loop if user enters "bye"
            if (inp.equals("bye"))
                break;
			
			DatagramPacket dpReceive = new DatagramPacket(buf, buf.length);
			ds.receive(dpReceive);
			System.out.println("Server:-" + Utilities.bufToStringBuilder(buf));

			if (Utilities.bufToStringBuilder(buf).toString().equals("bye")) {
			    System.out.println("Server closed connection.....EXITING");
			    break;
            }
        }
    }
	

}