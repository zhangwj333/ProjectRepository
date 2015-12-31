package util;

import org.junit.Test;

import junstech.util.AESEncryption;

public class AESEncryptionTest {
	
	@Test
	public void Test1() throws Exception{
	String content = "81711484";  
	String password = "JunstechCompany";  

	System.out.println("加密前" + content);  
	String encryptResult = AESEncryption.encrypt(content, password);  
	System.out.println("加密后" + encryptResult);  

	String decryptResult = AESEncryption.decrypt(encryptResult,password);  
	System.out.println("解密后" + new String(decryptResult));
	
	}
}
