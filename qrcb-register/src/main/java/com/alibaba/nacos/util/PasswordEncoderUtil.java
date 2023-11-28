package com.alibaba.nacos.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description <br/>
 */

public class PasswordEncoderUtil {

    private final static String encodeKey = "qrcbqrcbqrcbqrcb";
    private static final String KEY_ALGORITHM = "AES";

    public static void main(String[] args) {

        //nacos 用户密码加密解密
        System.out.println(encode("nacos"));
        System.out.println(matches("nacos","$2a$10$.cxYtbX4qRuLw6h/eeLNSO/6RAOywvCZDAJ0yDtFMbw2zNhv33vMy"));


        //swagger文档和前端传密码加密解密
        System.out.println(uiEncrypt("Qjnx456!@#"));
        System.out.println(uiDecrypt("Ig6zkCAypBOqPbQ4KRwtXw=="));


        //请求头Basic加密解密
        System.out.println(basicEncode("admin", "admin"));
        System.out.println(basicDecode("YWRtaW46YWRtaW4="));


        //配置文件加解密
        String rootPwdSalt = "qrcb";//根秘钥
        System.out.println(configEncrypt("admin",rootPwdSalt));
        System.out.println(configDecrypt("0c4bdypk16OC3zYZIgsEFA==",rootPwdSalt));
    }


    /**
     * 配置文件加密
     * @param plaintext 明文
     * @param rootPwdSalt 根秘钥：qrcb
     * @return 密文
     */
    public static String configEncrypt(String plaintext, String rootPwdSalt) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");//加密方式
        config.setPassword(rootPwdSalt);//加密所需的salt(盐)
        encryptor.setConfig(config); //应用配置

        return encryptor.encrypt(plaintext);
    }


    /**
     * 配置文件解密
     * @param ciphertext 密文
     * @param rootPwdSalt 根秘钥：qrcb
     * @return 明文
     */
    public static String configDecrypt(String ciphertext, String rootPwdSalt) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");//加密方式
        config.setPassword(rootPwdSalt);//加密所需的salt(盐)
        encryptor.setConfig(config); //应用配置

        return encryptor.decrypt(ciphertext);
    }

    /**
     * nacos 用户密码匹配
     *
     * @param raw     明文
     * @param encoded 密文
     * @return 是否匹配
     */
    public static Boolean matches(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }

    /**
     * nacos 用户密码加密
     *
     * @param raw 明文
     * @return 密文
     */
    public static String encode(String raw) {
        return new BCryptPasswordEncoder().encode(raw);
    }

    /**
     * 前端传输加密，Swagger聚合文档登录加密
     *
     * @param text 加密明文
     * @return 加密结果
     */
    public static String uiEncrypt(String text) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(encodeKey.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(encodeKey.getBytes()));

        if (StrUtil.isEmpty(text)) {
            return StrUtil.EMPTY;
        }

        int len = StrUtil.trimToEmpty(text).length();
        //密码明文长度超过16为，不符合要求
        if (len > 16) {
            return StrUtil.EMPTY;
        }

        //补齐长度
        StringBuilder textBuilder = new StringBuilder(text);
        for (int i = 0; i < (16 - len); i++) {
            textBuilder.append(StrUtil.SPACE);
        }
        text = textBuilder.toString();

        return aes.encryptBase64(text, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 前端传输解密，Swagger 聚合文档登录解码，对照 gateway 的 PasswordDecoderFilter
     *
     * @param content 加密密文
     * @return 解密明文
     */
    public static String uiDecrypt(String content) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(encodeKey.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(encodeKey.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(content.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }


    /**
     * Basic 请求头加密
     *
     * @param clientId     客户端ID
     * @param clientSecret 客户端秘钥
     * @return 加密结果
     */
    public static String basicEncode(String clientId, String clientSecret) {
        String content = clientId + StrUtil.COLON + clientSecret;
        return Base64.encode(content, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 请求头 Basic 解密
     *
     * @param content 请求头加密字符串
     * @return 解密结果
     */
    public static String basicDecode(String content) {
        String decodeStr = Base64.decodeStr(content, CharsetUtil.CHARSET_UTF_8);
        return "client_id:" + decodeStr.split(StrUtil.COLON)[0] + ";client_secret:" + decodeStr.split(StrUtil.COLON)[1];
    }
}
