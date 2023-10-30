package com.alibaba.nacos.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description <br/>
 */

public class PasswordEncoderUtil {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("nacos"));
    }

    public static Boolean matches(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }

    public static String encode(String raw) {
        return new BCryptPasswordEncoder().encode(raw);
    }

}
