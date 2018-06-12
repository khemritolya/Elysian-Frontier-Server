package org.vtt.utilities;

import java.net.InetAddress;

import static org.vtt.utilities.Logger.LogLevel.WARN;

public class BufferUtilities {
    public static String convertBuf(byte[] in, int start) throws NullPointerException {
        if (in == null) {
            Logger.log(WARN, "Byte buffer is null");
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

    public static byte[] convertString(byte opcode, InetAddress from, String s) {
        byte[] ex1 = from.toString().getBytes();
        byte[] ex2 = s.getBytes();

        byte[] out = new byte[ex2.length + ex1.length + 1];
        out[0] = opcode;

        for (int i = 0; i < ex1.length; i++) {
            out[i + 1] = ex1[i];
        }

        for (int i = 0; i < ex2.length; i++) {
            out[ex1.length + 1] = ex2[i];
        }

        return out;
    }
}
