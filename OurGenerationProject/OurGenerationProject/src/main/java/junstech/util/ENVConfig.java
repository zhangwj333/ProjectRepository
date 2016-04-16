package junstech.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import junstech.collab.wechat.MultipleChoiceGame;

public class ENVConfig {
	public static String encryptKey = "JunstechCompany";

	public static String multipleChoiceGame;

	public static void initializing() throws Exception {
		File dir = new File(multipleChoiceGame);
		if (dir.isDirectory()) {
			File[] tempList = dir.listFiles();
			for (int i = 0; i < tempList.length; i++) {
				System.out.println(tempList[i].getAbsolutePath());
				RedisUtil.setString(tempList[i].getName(),
						FileUtil.getFileAsString(tempList[i].getAbsolutePath()));
			}
		}
	}

	static {

		Properties prop = new Properties();
		System.out.println(MultipleChoiceGame.class.getClassLoader().getResource("config.properties").getPath());
		try {
			// 读取属性文件a.properties
			InputStream in = MultipleChoiceGame.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(in); /// 加载属性列表
			multipleChoiceGame = prop.getProperty("multipleChoiceGame");
			MetaData.cargoPath = prop.getProperty("cargo");
			MetaData.transactionPath = prop.getProperty("transaction");
			MetaData.reportPath = prop.getProperty("report");
			MetaData.logPath = prop.getProperty("log");
			in.close();
			initializing();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
