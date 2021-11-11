package com.bas.core.security;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lucio on 2021/11/10.
 */
public class MD5Utils {

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char hexDigitsLower[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 校验MD5码是否相等
     *
     * @param message 待校验字符串
     * @param md5     md5值
     * @return true:校验通过
     */
    public static boolean checkEquals(@NotNull String message, @NotNull String md5) throws NoSuchAlgorithmException {
        String actual = MD5(message);
        return md5.equals(actual) || md5.equals(actual.toUpperCase());
    }

    /**
     * 校验MD5码是否相等
     *
     * @param message 待校验字符串
     * @param salt    加盐值
     * @param md5     md5值
     * @return true:校验通过
     */
    public static boolean checkEquals(@NotNull String message, @NotNull String salt, @NotNull String md5) throws NoSuchAlgorithmException {
        String actual = MD5Lowercase(message, salt);
        return md5.equals(actual) || md5.equals(actual.toUpperCase());
    }

    /**
     * MD5 加密
     *
     * @param message 待加密的字符串
     * @return 加密后字符串（32位小写字母+数字）
     */
    public static @NotNull String MD5Lowercase(@NotNull String message) throws NoSuchAlgorithmException {
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        md.update(message.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     * MD5 加密
     *
     * @param message 待加密的字符串
     * @return 加密后字符串（32位大写字母+数字）
     */
    public static @NotNull String MD5Uppercase(@NotNull String message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(message.getBytes());
        // 获得密文
        byte[] digestBytes = md.digest();
        // 把密文转换成十六进制的字符串形式
        int j = digestBytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte digestByte : digestBytes) {
            str[k++] = hexDigits[digestByte >>> 4 & 0xf];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[digestByte & 0xf]; // 取字节中低 4 位的数字转换
        }
        return new String(str);
    }

    /**
     * MD5 加密
     *
     * @param message 待加密的字符串
     * @param salt    加盐值
     * @return 加密后字符串（32位小写字母+数字）
     */
    @NotNull
    public static String MD5Lowercase(@NotNull String message, @NotNull String salt) throws NoSuchAlgorithmException {
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");

        // 使用指定的字节更新摘要
        md.update(message.getBytes());
        md.update(salt.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     * MD5 加密
     *
     * @param message 待加密的字符串
     * @param salt    加盐值
     * @return 加密后字符串（32位大写字母+数字）
     */
    @NotNull
    public static String MD5Uppercase(@NotNull String message, @NotNull String salt) throws NoSuchAlgorithmException {
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        md.update(message.getBytes());
        md.update(salt.getBytes());

        // 获得密文
        byte[] digestBytes = md.digest();
        // 把密文转换成十六进制的字符串形式
        int j = digestBytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : digestBytes) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * MD5 加密，与方法{@link #MD5Lowercase(String)}相同
     *
     * @param message 待加密的字符串
     * @return 加密后字符串（32位小写字母+数字）
     * @throws NoSuchAlgorithmException
     */
    @NotNull
    public static String MD5(@NotNull String message) throws NoSuchAlgorithmException {
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] digestBytes = mdTemp.digest();
        int j = digestBytes.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : digestBytes) {
            str[k++] = hexDigitsLower[byte0 >>> 4 & 0xf];
            str[k++] = hexDigitsLower[byte0 & 0xf];
        }
        return new String(str);
    }

}
