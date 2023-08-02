package kr.co.dk.sample.api.util;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import java.util.Base64;
import java.util.Base64.*;

/**
 * <PRE>
 * Base64유틸리티 클래스
 * <p>
 * 클래스 : Base64Util
 * 작성일 : 2008. 11. 16. 오후 07:05:58
 * 작성자 : 정의석
 * </PRE>
 */
public final class Base64Util {
    /**
     * 입력받은 문자열을 Base64인코딩 하여 반환.
     *
     * @param str str
     */
    public static String encode(String str) {
        String rtnVal = null;

        try {
            byte[] plainText = null;
            plainText = str.getBytes();

            Encoder encoder = Base64.getEncoder();
            rtnVal = encoder.encodeToString(plainText);
        } catch (Exception e) {
            return "";
        }

        return rtnVal;
    }

    public static String encode(byte[] str) {
        String rtnVal = null;

        try {
            byte[] plainText = null;
            plainText = str;

            Encoder encoder = Base64.getEncoder();
            rtnVal = encoder.encode(plainText).toString();
        } catch (Exception e) {
            return "";
        }

        return rtnVal;
    }

    /**
     * 입력받은 문자열을 Base64디코딩 하여 반환.
     *
     * @param str str
     */
    public static String decode(String str) {
        String rtnVal = null;

        try {
            byte[] plainText = null;

            Decoder decoder = Base64.getDecoder();
            plainText = decoder.decode(str);
            rtnVal = new String(plainText);
        } catch (Exception e) {
            return "";
        }

        return rtnVal;
    }

    public static byte[] base64Decode(String str) {
        byte[] plainText = null;
        String rtnVal = null;
        try {
            Decoder decoder = Base64.getDecoder();
            plainText = decoder.decode(str);
            rtnVal = new String(plainText);
        } catch (Exception e) {
            return plainText;
        }

        return plainText;
    }

    public static String base32Encode(String str) {
        String result = null;

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    public static void main(String[] args) {
//		System.out.println(encode("jdbc:mysql://192.168.0.198:3306/gmc_dfians?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false"));
//		System.out.println(encode("root"));
//		System.out.println(encode("UP!2jBMrn9wXDL5dwwk"));
//		System.out.println(decode("dPvyP2bhxMb1ofdH7YHJJC/3phUAJgPa6jiQs7aJM61hkh7yYyVje8NOp8d0LFMNTM9t5aO1KpRJepZu09+12DuRz72n7TVgXgi24y37SRTJq7GXhT/xKZ+sL8MsLLfiNO/9IVpkfb6C9rDEZBieHA=="));
//		System.out.println(decode("ewBNBu+y+krK10IRm2hPxw=="));
//		System.out.println(decode("3SSIdnsW9W0G8yQ3NBxDG5zqRHdJ8HMijUPDOPQ0iEk="));	//잘못된 계정비밀번호
//		System.out.println(decode("ODEwM3xqb2Nob2kzOHxhMjQ2OHwxMTExMTExMQ=="));	//잘못된 공인인증서 비밀번호
    }
}
