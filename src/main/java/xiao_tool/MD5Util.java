package xiao_tool;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author shawn
 * @create 2018-11-06 15:21
 **/
public class MD5Util {

    public static String getUpcaseMd5(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] md = md5.digest();
            char[] ch = new char[md.length * 2];
            int k = 0;
            for (int i = 0; i < md.length; i++) {
                byte b = md[i];
                ch[k++] = hexDigits[b >>> 4 & 0xf];
                ch[k++] = hexDigits[b & 0xf];
            }
            return new String(ch);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
