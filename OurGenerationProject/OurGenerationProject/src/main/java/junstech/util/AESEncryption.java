package junstech.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

	/**
	 * @param content
	 * @param password
	 * @return
	 */
	public static String encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = encodeBase64(content).getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return  parseByte2HexStr(result); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String content, String password) throws Exception {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal( parseHexStr2Byte(content));
			return decodeBase64(new String(result, "utf-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
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
	 * @param bstr
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeBase64(String bstr) throws UnsupportedEncodingException {
		return new sun.misc.BASE64Encoder().encode(bstr.getBytes("utf-8"));
	}

	/**
	 * @param str
	 * @return string
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeBase64(String str) throws UnsupportedEncodingException {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(bt, "utf-8");
	}
}
