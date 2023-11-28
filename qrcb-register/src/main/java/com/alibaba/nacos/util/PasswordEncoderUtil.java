package com.alibaba.nacos.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
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

        System.out.println(new BCryptPasswordEncoder().encode("nacos"));
        System.out.println(uiEncrypt("123456"));
        System.out.println(uiDecrypt("ekIkskF9/LB5PreVOe6Jcg=="));
    }

    /**
     * nacos 用户密码匹配
     * @param raw 明文
     * @param encoded 密文
     * @return 是否匹配
     */
    public static Boolean matches(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }

    /**
     * nacos 用户密码加密
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
}
