package com.sc.core.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * ApplicationUtils : 程序工具类，提供大量的便捷方法
 *
 * @author StarZou
 * @since 2014-09-28 22:31
 */
public class ApplicationUtils {

    /**
     * 产生一个36个字符的UUID
     *
     * @return UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * md5加密
     *
     * @param value 要加密的值
     * @return md5加密后的值
     */
    public static String md5Hex(String value) {
        return DigestUtils.md5Hex(value);
    }

    /**
     * sha1加密
     *
     * @param value 要加密的值
     * @return sha1加密后的值
     */
    public static String sha1Hex(String value) {
        return DigestUtils.sha1Hex(value);
    }

    /**
     * sha256加密
     *
     * @param value 要加密的值
     * @return sha256加密后的值
     */
    public static String sha256Hex(String value) {
        return DigestUtils.sha256Hex(value);
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        // md5加密测试
        String md5_1 = md5("123456");
        String md5_2 = md5("hugo");
        System.out.println(md5_1 + "\n" + md5_2 + "\n-------------------");
        // sha加密测试
        String sha_1 = sha("123456");
        String sha_2 = sha("hugo");
        System.out.println(sha_1 + "\n" + sha_2 + "\n-------------------");

        // md5AndSha双重加密
        String md5_sha_1 = md5AndSha("123456");
        String md5_sha_2 = md5AndSha("hugo");
        System.out.println(md5_sha_1 + "\n" + md5_sha_2 + "\n-------------------");
    }

    /**
     * 加密
     *
     * @param inputText
     * @return
     */
    public static String e(String inputText) {
        return md5(inputText);
    }

    /**
     * 二次加密，应该破解不了了吧？
     *
     * @param inputText
     * @return
     */
    public static String md5AndSha(String inputText) {
        return sha(md5(inputText));
    }

    /**
     * md5加密
     *
     * @param inputText
     * @return
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    /**
     * sha加密
     *
     * @param inputText
     * @return
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText
     *            要加密的内容
     * @param algorithmName
     *            加密算法名称：md5或者sha-1，不区分大小写
     * @return
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

    /**
     * 返回十六进制字符串
     *
     * @param arr
     * @return
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

}
