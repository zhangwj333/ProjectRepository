package util;

import org.junit.Test;

import junstech.util.AESEncryption;

public class AESEncryptionTest {
	
	@Test
	public void Test1() throws Exception{
	String content = "81711484";  
	String password = "JunstechCompany";  

	System.out.println("����ǰ" + content);  
	String encryptResult = AESEncryption.encrypt(content, password);  
	System.out.println("���ܺ�" + encryptResult);  

	String decryptResult = AESEncryption.decrypt(encryptResult,password);  
	System.out.println("���ܺ�" + new String(decryptResult));
	
	}
}
