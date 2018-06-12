package test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClient {
    static class BufferUtilities {
        public static String convertBuf(byte[] in, int start) throws NullPointerException {
            if (in == null) {
                throw new NullPointerException("Byte buffer is null");
            }

            StringBuilder b = new StringBuilder();

            for (int i = start; i < in.length; i++) {
                if (in[i] != 0) {
                    b.append((char)in[i]);
                }
            }

            return b.toString();
        }

        public static byte[] convertString(byte opcode, String s) {
            byte[] ex = s.getBytes();

            byte[] out = new byte[ex.length + 1];
            out[0] = opcode;

            for (int i = 0; i < ex.length; i++) {
                out[i + 1] = ex[i];
            }

            return out;
        }
    }

    static class Receiver extends Thread {
        @Override
        public void run() {
            byte[] inBuffer = new byte[1024];

            try {
                while (true) {
                    DatagramSocket ds = new DatagramSocket();
                    DatagramPacket dpReceive = new DatagramPacket(inBuffer, inBuffer.length);
                    ds.receive(dpReceive);
                    System.out.println("Server:-" + BufferUtilities.convertBuf(inBuffer, 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter server's ip: ");

        try {
            InetAddress addr = InetAddress.getByName(sc.nextLine());

            Receiver r = new Receiver();
            r.setDaemon(true);
            r.start();

            DatagramSocket ds = new DatagramSocket();
            byte[] buf;
            DatagramPacket send;

            while (true) {
                String text = sc.nextLine();

                if (text.equals("exit")) break;


                buf = BufferUtilities.convertString((byte)0x01, text);
                send = new DatagramPacket(buf, buf.length, addr,1337);

                ds.send(send);
                buf = new byte[1024];
            }

            System.out.println("Exiting...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}