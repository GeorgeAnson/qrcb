package com.qrcb.common.core.assemble.constant;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 公共常量 <br/>
 */

public interface CommonConstants {

    /**
     * header 中租户ID
     */
    String TENANT_ID = "TENANT-ID";

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 租户ID
     */
    Integer TENANT_ID_1 = 1;

    /**
     * header 中版本信息
     */
    String VERSION = "Version";

    /**
     * 滑块验证码
     */
    String IMAGE_CODE_TYPE = "blockPuzzle";

    /**
     * 验证码开关
     */
    String CAPTCHA_FLAG = "captcha_flag";

    /**
     * 密码传输是否加密
     */
    String ENC_FLAG = "enc_flag";
}
