//package com.alibaba.nacos.util;
//
//import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
//import lombok.experimental.UtilityClass;
//import org.apache.commons.codec.binary.Base64;
//import org.jasypt.encryption.StringEncryptor;
//import org.springframework.core.env.StandardEnvironment;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.PBEParameterSpec;
//import java.security.Key;
//import java.security.SecureRandom;
//
///**
// * @Author Anson
// * @Create 2023-11-07
// * @Description <br/>
// */
//
//@UtilityClass
//public class ConfigFilePasswordUtil {
//
//    public static void main(String[] args) throws Exception {
//        System.out.println("pwd: "+encrypt("qrcb","qrcb"));
//        System.out.println(decrypt("QMdLQFnPN5E=","qrcb","kCPb07Vq4eA="));
//
//        System.setProperty("JASYPT_ENCRYPTOR_PASSWORD", "lengleng");
//        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
//
//        //加密方法
//        System.out.println(stringEncryptor.encrypt("123456"));
//        //解密方法
//        System.out.println(stringEncryptor.decrypt("saRv7ZnXsNAfsl3AL9OpCQ=="));
//    }
//
//    /**
//     * 定义使用的算法为:PBEWITHMD5andDES算法
//     */
//    public static final String ALGORITHM = "PBEWITHMD5ANDDES";
//    /**
//     * 定义迭代次数为1000次,次数越多，运算越大，越不容易破解之类。
//     */
//    private static final int ITERATIONCOUNT = 100000;//orgin 1000
//
//    private  String saltStr;
//
//    /**
//     * 获取加密算法中使用的盐值,解密中使用的盐值必须与加密中使用的相同才能完成操作. 盐长度必须为8字节的倍数
//     *
//     * @return byte[] 盐值
//     * */
//    private  byte[] getSalt(String password) throws Exception {
//        // 实例化安全随机数  // 产出盐
//        SecureRandom random = new SecureRandom((password+password+password+password).getBytes());
//        return random.generateSeed(8);
//    }
//    /**
//     * 根据PBE密码生成一把密钥
//     *
//     * @param password
//     *            生成密钥时所使用的密码
//     * @return Key PBE算法密钥
//     * */
//
//    private Key getPBEKey(String password) throws Exception {
//        // 实例化使用的算法
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
//        // 设置PBE密钥参数
//        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
//        // 生成密钥
//        SecretKey secretKey = keyFactory.generateSecret(keySpec);
//
//        return secretKey;
//    }
//
//    /**
//     * 加密明文字符串
//     *
//     * @param plaintext
//     *            待加密的明文字符串
//     * @param password
//     *            生成密钥时所使用的密码
//     * @return 加密后的密文字符串
//     * @throws Exception
//     */
//    public  String encrypt(String plaintext, String password)
//            throws Exception {
//        //获取根据PBE口令生成的key
//        Key key = getPBEKey(password);
//        //获取8字节位的盐
//        byte[] salt = getSalt(password);
//        //将盐进行加密
//        saltStr = Base64.encodeBase64String(salt);
//        System.out.println(saltStr);
//        //设置PBE参数的盐和运算次数
//        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,ITERATIONCOUNT);
//        //构建实例化
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        //cipher对象使用之前还需要初始化，共三个参数("加密模式或者解密模式","密匙","向量")
//        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
//        //数据转换
//        byte encipheredData[] = cipher.doFinal(plaintext.getBytes("UTF-8"));
//
//        return Base64.encodeBase64String(encipheredData);
//    }
//    public String getSaltStr(){
//        return saltStr;
//    }
//    /**
//     * 解密密文字符串
//     *
//     * @param ciphertext
//     *            待解密的密文字符串
//     * @param password
//     *            生成密钥时所使用的密码(如需解密,该参数需要与加密时使用的一致)
//     * @param saltStr
//     *            盐值(如需解密,该参数需要与加密时使用的一致)
//     * @return 解密后的明文字符串
//     * @throws Exception
//     */
//    public  String decrypt(String ciphertext, String password, String saltStr)
//            throws Exception {
//        //转换密钥
//        Key key = getPBEKey(password);
//        //解密盐
//        byte[] salt = Base64.decodeBase64(saltStr);
//        //实例化PBE参数
//        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
//                ITERATIONCOUNT);
//        //实例化
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        //初始化
//        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
//        //数据转换
//        byte[] passDec = cipher.doFinal(Base64.decodeBase64(ciphertext));
//        //返回明文字符
//        return new String(passDec);
//    }
//
//}
