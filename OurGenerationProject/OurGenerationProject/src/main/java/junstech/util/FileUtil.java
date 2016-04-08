package junstech.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;


public class FileUtil {
	
	
	public static void save(MultipartFile file, String path) throws IOException {

		BufferedInputStream ins = new BufferedInputStream(new ByteArrayInputStream(file.getBytes()));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
		byte[] buffer = new byte[4096]; 
		int byteread;
		while ( (byteread = ins.read(buffer)) != -1) { 
			out.write(buffer, 0, byteread); 
		} 
		ins.close();
		out.close();
	}
	
	public static String getFileExtension(String fileName) throws IOException {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	public static String getFileAsString(String FileName) throws IOException{
		StringBuilder result = new StringBuilder();
		
		File file = new File(FileUtil.class.getClassLoader().getResource(FileName).getPath());
		System.out.println(file.getAbsolutePath());
		BufferedInputStream ins = new BufferedInputStream(new FileInputStream(file));
		byte[] buffer = new byte[4096]; 
		int byteread;
		while (ins.read(buffer) != -1) { 
			result.append(buffer);
		} 
		ins.close();
		return result.toString();
	}
}
