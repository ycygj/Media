/**
 * @Title: SecurityUtil.java
 * @Package com.uroad.cxy.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-6-15 下午2:07:25
 * @version V1.0
 */
package com.uroad.util;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Administrator
 *
 */
public class SecurityUtil {

	public static String toHMACSHA256String(String publicKey, String privateKey) {
		String mykey = privateKey;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret = new SecretKeySpec(mykey.getBytes(),
					"HmacSHA256");
			mac.init(secret);
			byte[] digest = mac.doFinal(publicKey.getBytes());
			return Hex.encodeHexStr(digest);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}

	/**
	 * 将字符串转换为MD5加密（32位小写）
	 *
	 * **/
	public static String EncoderByMd5(String str) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("md5");// 返回实现指定摘要算法的
		// MessageDigest
		// 对象。
		md5.update(str.getBytes());// 先将字符串转换成byte数组，再用byte 数组更新摘要
		byte[] nStr = md5.digest();// 哈希计算，即加密
		return bytes2Hex(nStr);// 加密的结果是byte数组，将byte数组转换成字符串
	}

	/**
	 * 将字符串转换为SHA-1加密
	 *
	 * **/
	public static String EncoderBySHA1(String str) throws Exception {
		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");// 返回实现指定摘要算法的
		// MessageDigest
		// 对象。
		sha1.update(str.getBytes());// 先将字符串转换成byte数组，再用byte 数组更新摘要
		byte[] nStr = sha1.digest();// 哈希计算，即加密
		return bytes2Hex(nStr);// 加密的结果是byte数组，将byte数组转换成字符串
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}

	/**
	 * 将byte数组转换为字符串
	 * **/
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}
