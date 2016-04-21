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
import java.nio.CharBuffer;
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

	public static String getFileAsStringFromJunstech(String path, String fileName) throws IOException {
		String data = null;
		if (RedisUtil.getJedis().isConnected()) {
			if (RedisUtil.contains(fileName)) {
				System.out.println("Get from Redis");
				data = RedisUtil.getString(fileName);
			}else{
				System.out.println("Get from Config");
				data = getFileAsStringFromConfigPath(path, fileName);
				//Cache data
				RedisUtil.setString(fileName, data);
			}
		} else {
			System.out.println("Get from Config");
			data = getFileAsStringFromConfigPath(path, fileName);
		}

		return data;
	}

	public static String getFileAsStringFromJunstech(String fileName) throws IOException {
		return getFileAsStringFromJunstech(null, fileName);
	}

	public static String getFileAsStringFromRedis(String FileName) throws IOException {
		return RedisUtil.getString(FileName);
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
		char[] data = new char[bufferSize];
		while ((reader.read(data)) != -1) {
			result.append(data);
		}
		read.close();
		return result.toString();
	}

	private static int bufferSize = 4096;
}
