
package com.alibaba.cloud.ai.toolcalling.youdaotranslate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Yeaury
 */
public class AuthTools {

	public static String calculateSign(String appKey, String appSecret, String q, String salt, String curtime)
			throws NoSuchAlgorithmException {
		String strSrc = appKey + getInput(q) + salt + curtime + appSecret;
		return sha256(strSrc);
	}

	private static String sha256(String strSrc) throws NoSuchAlgorithmException {
		byte[] bt = strSrc.getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bt);
		byte[] bts = md.digest();
		StringBuilder des = new StringBuilder();
		for (byte b : bts) {
			String tmp = (Integer.toHexString(b & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	private static String getInput(String q) {
		if (q.length() > 20) {
			String firstPart = q.substring(0, 10);
			String lastPart = q.substring(q.length() - 10);
			return firstPart + q.length() + lastPart;
		}
		else {
			return q;
		}
	}

}
