package com.hanil.fluxus.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Base64;

public class StringUtil {

    public static String stringNull(Object object){
        String result = "";
        if(object == null || String.valueOf(object).equals("null") || String.valueOf(object).equals("") ){
            result   =   "";
        }else {
            result = String.valueOf(object);
        }
        return result;
    }

    public static String stringNullChg(Object object, String res){
        String result = "";
        if(object == null || String.valueOf(object).equals("null") || String.valueOf(object).equals("") ){
            result   =   "";
            if(res != null) {
                result = res;
            }
        }else {
            result = String.valueOf(object);
        }
        return result;
    }

    //SHA256암호화
    public String encryptionSHA256(String str) {
        String sha = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<byteData.length;i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();
        }catch(Exception e) {
            e.printStackTrace();
            sha = null;
        }
        return sha;
    }
    //base64인코더
    public String base64Encode(String str) throws UnsupportedEncodingException {
        String result = "";

        byte[] targetBytes = str.getBytes("UTF-8");

        // Base64 인코딩 ///////////////////////////////////////////////////
        Base64.Encoder encoder = Base64.getEncoder();

        // Encoder#encode(byte[] src) :: 바이트배열로 반환
//	        byte[] encodedBytes = encoder.encode(targetBytes);
//	        System.out.println(new String(encodedBytes));

        // Encoder#encodeToString(byte[] src) :: 문자열로 반환
        String encodedString = encoder.encodeToString(targetBytes);

        result = encodedString;

        return result;
    }
    //base64디코더
    public String base64Decode(String str) throws UnsupportedEncodingException{
        String result = "";

        // Base64 디코딩 ///////////////////////////////////////////////////
        Base64.Decoder decoder = Base64.getDecoder();

        // Decoder#decode(bytes[] src)
//	        byte[] decodedBytes1 = decoder.decode(str);
        // Decoder#decode(String src)
        byte[] decodedBytes2 = decoder.decode(str);

        // 디코딩한 문자열을 표시
//	        String decodedString = new String(decodedBytes1, "UTF-8");
//	        System.out.println(decodedString);

        result = new String(decodedBytes2, "UTF-8");

        return result;
    }
    public static boolean byteCheck(String txt, int standardByte) {
        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;

        char[] txtChar = txt.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            } else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }

        int txtByte = en + ko + etc;
        if (txtByte > standardByte) {
            return false;
        } else {
            return true;
        }
    }

}