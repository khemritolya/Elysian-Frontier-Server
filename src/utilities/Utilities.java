package utilities;

public class Utilities {
    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder bufToStringBuilder(byte[] a) {
        if (a == null) return null;
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
