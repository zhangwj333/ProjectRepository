package junstech.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

import junstech.collab.wechat.MultipleChoiceGame;

public class FileUtil {

	public static void save(MultipartFile file, String path) throws IOException {

		BufferedInputStream ins = new BufferedInputStream(new ByteArrayInputStream(file.getBytes()));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
		byte[] buffer = new byte[4096];
		int byteread;
		while ((byteread = ins.read(buffer)) != -1) {
			out.write(buffer, 0, byteread);
		}
		ins.close();
		out.close();
	}

	public static String getFileExtension(String fileName) throws IOException {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public static String getFileAsStringFromClassPath(String FileName) throws IOException {
		return getFileAsString(FileUtil.class.getClassLoader().getResource("/").getPath() + FileName);
	}
	
	public static String getFileAsStringFromConfigPath(String ConfigPath, String FileName) throws IOException {
		return getFileAsString(ConfigPath + "/" + FileName);
	}
	
	public static String getFileAsString(String FileName) throws IOException {
		StringBuilder result = new StringBuilder();
		File file = new File(FileName);
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
		BufferedReader reader = new BufferedReader(read);
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		read.close();

		return result.toString();
	}
	
	public static String multipleChoiceGame;
	
	static {

		Properties prop = new Properties();
		System.out.println(MultipleChoiceGame.class.getClassLoader().getResource("config.properties").getPath());
		try {
			// 读取属性文件a.properties
			InputStream in = MultipleChoiceGame.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(in); /// 加载属性列表
			multipleChoiceGame = prop.getProperty("multipleChoiceGame");
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
