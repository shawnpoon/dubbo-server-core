package com.shawn.server.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 常用加密合集
 * 
 * @author ShawnPoon
 * @version 1.1
 * @since 2015/7/22 14:50
 * 
 */
public class EncryptUtil {
	public static final String KEY_SHA = "SHA-1";
	public static final String KEY_MD5 = "MD5";
	public static final String KEY_DES = "DES";
	public static final String KEY_AES = "AES";

	public static final String KEY_MAC_DEFAULT = "HmacMD5";
	public static final String KEY_MAC_MD5 = "HmacMD5";
	public static final String KEY_MAC_SHA1 = "HmacSHA1";
	public static final String KEY_MAC_SHA256 = "HmacSHA256";
	public static final String KEY_MAC_SHA384 = "HmacSHA384";
	public static final String KEY_MAC_SHA512 = "HmacSHA512";

	private static final String DES_DEFAULT_KEY = "123";
	private static final String AES_DEFAULT_KEY = "123456";

	/**
	 * AES加密
	 * 
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptAES(byte[] byteContent, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
		if (password == null) {
			password = AES_DEFAULT_KEY;
		}
		kgen.init(128, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_AES);
		Cipher cipher = Cipher.getInstance(KEY_AES);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(byteContent);
		return result;
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES(String content, String password) throws Exception {
		byte[] byteContent = content.getBytes("utf-8");
		byte[] byteResult = encryptAES(byteContent, password);
		return byte2hex(byteResult);
	}

	/**
	 * AES解密
	 * 
	 * @param content
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] decryptAES(byte[] content, String password) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);
		kgen.init(128, new SecureRandom(password.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_AES);
		Cipher cipher = Cipher.getInstance(KEY_AES);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return result;
	}

	/**
	 * AES解密
	 * 
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES(String content, String password) throws Exception {
		byte[] byteContent = hex2byte(content);
		byte[] byteResult = decryptAES(byteContent, password);
		return new String(byteResult, "utf-8");
	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptSHA(String data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data.getBytes());
		return byte2hex(sha.digest());
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptMD5(String data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data.getBytes());

		return byte2hex(md5.digest());
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey(String mac_key) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(mac_key);
		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key, String mac_key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), mac_key);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	/**
	 * DES加密
	 * 
	 * @param byteS
	 * @return
	 */
	private static byte[] encryptDES(byte[] byteS, Key key) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(KEY_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * DES解密
	 * 
	 * @param byteD
	 * @return
	 */
	private static byte[] decryptDES(byte[] byteD, Key key) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * DES加密
	 * 
	 * @param strMing
	 * @param strKey
	 * @return
	 */
	public static String encodeDES(String strMing, String strKey) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";

		if (strKey == null) {
			strKey = DES_DEFAULT_KEY;
		}
		Key key = generateKey(strKey);
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = encryptDES(byteMing, key);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * DES加密
	 * 
	 * @param strMing
	 * @param strKey
	 * @return
	 */
	public static byte[] encodeDESToArray(String strMing, String strKey) {
		byte[] byteMi = null;
		byte[] byteMing = null;

		if (strKey == null) {
			strKey = DES_DEFAULT_KEY;
		}
		Key key = generateKey(strKey);
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = encryptDES(byteMing, key);
			return byteMi;
		} catch (Exception e) {
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
		} finally {
			byteMing = null;
			byteMi = null;
		}
	}

	/**
	 * DES解密
	 * 
	 * @param strMi
	 * @param strKey
	 * @return
	 */
	public static String decodeDES(String strMi, String strKey) {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		if (strKey == null) {
			strKey = DES_DEFAULT_KEY;
		}
		Key key = generateKey(strKey);
		try {
			byteMi = base64De.decodeBuffer(strMi);
			byteMing = decryptDES(byteMi, key);
			strMing = new String(byteMing, "utf-8");
		} catch (Exception e) {
		} finally {
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * DES解密
	 * 
	 * @param byteMi
	 * @param strKey
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeDES(byte[] byteMi, String strKey) throws UnsupportedEncodingException {
		byte[] byteMing = null;
		String strMing = "";
		if (strKey == null) {
			strKey = DES_DEFAULT_KEY;
		}
		Key key = generateKey(strKey);
		try {
			byteMing = decryptDES(byteMi, key);
			strMing = new String(byteMing, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMing;
	}

	/**
	 * 字节转化为十六进制字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byte2hex(byte[] data) {
		if (data == null || data.length == 0) {
			return "";
		}
		int j = data.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = data[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	public static byte[] hex2byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static byte[] decryptBASE64(String key) throws IOException {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 根据参数生成KEY
	 */
	public static Key generateKey(String strKey) {
		Key key = null;
		try {
			key = new SecretKeySpec(strKey.getBytes(), KEY_DES);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return key;
	}

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	public static void main(String[] args) throws Exception {
	}

}
