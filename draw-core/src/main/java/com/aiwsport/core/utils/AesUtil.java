package com.aiwsport.core.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class AesUtil {
    private static Log log = LogFactory.getLog(AesUtil.class);
    private static String key = "7hfsHSAI9021USADKg";
    private static byte[] keybytes = null;
    static {
        keybytes = shaEncode(key);
    }

    public static String encrypt(String content) {
        if (StringUtils.isNotBlank(content)) {
            String encryptContent = encrypt(content, keybytes).replaceAll("\r\n","");
            return encryptContent;
        }
        return content;
    }

    public static String encrypt(Long content) {
        return content == null ? null : encrypt(String.valueOf(content));
    }

    public static String encrypt(String content, byte[] keybytes) {
        String result = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = zeroPadding(content.getBytes(), blockSize);
            byte[] encrypted = cipher.doFinal(dataBytes);
            result = new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Encrypt error!" + e.getMessage());
        }
        return result;
    }

    public static String decrypt(String signsrc) {
        if (StringUtils.isNotBlank(signsrc)) {
            return decrypt(signsrc, keybytes);
        }

        return signsrc;
    }

    public static String decrypt(String signsrc, byte[] keybytes) {
        String result = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(signsrc);
            byte[] original = cipher.doFinal(encrypted1);
            result = new String(removeZeroPadding(original));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("decrypt result:" + result);
        return result;
    }

    public static byte[] shaEncode(String key) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes("UTF-8"));
            return hash;
        } catch (Exception e) {
            log.error("SHA encode error!" + e.getMessage());
        }
        return null;
    }

    private static byte[] removeZeroPadding(byte[] bytes) {
        int newLength = bytes.length;
        int length = bytes.length;
        for (int i = length - 1; i >= 0; i--) {
            byte b = bytes[i];
            if (b != 0) {
                break;
            }
            newLength--;
        }

        if (newLength == length) {
            return bytes;
        }

        byte[] newBytes = new byte[newLength];
        System.arraycopy(bytes, 0, newBytes, 0, newLength);
        return newBytes;
    }

    private static byte[] zeroPadding(byte[] org, int blockSize) {
        int plaintextLength = org.length;
        if (plaintextLength % blockSize == 0) {// 不需要填充
            return org;
        } else {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(org, 0, plaintext, 0, org.length);
            return plaintext;
        }
    }

}
