package com.qrcb.common.core.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 认证授权相关工具类 <br/>
 */

@Slf4j
@UtilityClass
public class AuthUtils {

    private final String BASIC_ = "Basic" + StrUtil.SPACE;

    /**
     * 从 header 请求中的 clientId / client secret
     *
     * @param header header 中的参数
     * @throws RuntimeException if the Basic header is not present or is not valid Base64
     */
    @SneakyThrows
    public String[] extractAndDecodeHeader(String header) {

        byte[] base64Token = header.substring(6).getBytes(CharsetUtil.CHARSET_UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharsetUtil.CHARSET_UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * *从header 请求中的 clientId / client secret
     *
     * @param request {@link HttpServletRequest}
     * @return String[]
     */
    @SneakyThrows
    public String[] extractAndDecodeHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BASIC_)) {
            throw new RuntimeException("请求头中 client 信息为空");
        }

        return extractAndDecodeHeader(header);
    }

}
