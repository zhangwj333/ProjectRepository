package junstech.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BackupUtil {

	public static void main(String args[]) throws Exception {
		InputStream is = BackupUtil.class.getClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		properties.load(is);
		BackupUtil.export(properties);// 这里简单点异常我就直接往上抛
		//BackupUtil.importSql(properties);
	}

	public static String export(Properties properties) throws Exception {
		String command = getExportCommand(properties);
		String fileName = properties.getProperty("jdbc.exportPath") + "DBbackup" + dateFormat.format(new Date()) + ".sql";
		Runtime rc = Runtime.getRuntime();
        try {  
        	Process p = rc.exec(command);  

            if (p.waitFor() != 0) {  
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束  
                    System.err.println("命令执行失败!");  
            }
            return fileName;
        } catch (Exception e) {  
            return null;
        }
		
	}

	public static void importSql(Properties properties) throws IOException {
		Runtime runtime = Runtime.getRuntime();

		String cmdarray[] = getImportCommand(properties);
		// runtime.exec(cmdarray);//这里也是简单的直接抛出异常
		Process process = runtime.exec(cmdarray[0]);

		OutputStream os = process.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(os);

		writer.write(cmdarray[1] + "\r\n" + cmdarray[2]);
		writer.flush();
		writer.close();
		os.close();
	}

	static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");

	private static String getExportCommand(Properties properties) throws Exception {
		StringBuffer command = new StringBuffer();
		String username = properties.getProperty("jdbc.user");
		String password = properties.getProperty("jdbc.pass");
		String database = properties.getProperty("jdbc.database");
		String exportPath = properties.getProperty("jdbc.exportPath");

		command.append("cmd /c mysqldump -u ")//
				.append(username)//
				.append(" -p").append(AESEncryption.decrypt(password, ENVConfig.encryptKey).toString())//
				.append(" --databases ").append(database)//
				.append(" > ").append(exportPath).append("DBbackup" + dateFormat.format(new Date()) + ".sql");

		return command.toString();
	}

	/**
	 * 根据属性文件的配置，分三步走获取从目标文件导入数据到目标数据库所需的命令 如果在登录的时候指定了数据库名则会
	 * 直接转向该数据库，这样就可以跳过第二步，直接第三步；
	 * 
	 * @param properties
	 * @return
	 */
	private static String[] getImportCommand(Properties properties) {
		String username = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");
		String host = properties.getProperty("jdbc.host");// 导入的目标数据库所在的主机
		String port = properties.getProperty("jdbc.port");// 使用的端口号
		String importDatabaseName = properties.getProperty("jdbc.importDatabaseName");// 导入的目标数据库的名称
		String importPath = properties.getProperty("jdbc.importPath");// 导入的目标文件所在的位置
		// 第一步，获取登录命令语句
		String loginCommand = new StringBuffer().append("mysql -u").append(username).append(" -p").append(password)
				.append(" -h").append(host).append(" -P").append(port).toString();
		// 第二步，获取切换数据库到目标数据库的命令语句
		String switchCommand = new StringBuffer("use ").append(importDatabaseName).toString();
		// 第三步，获取导入的命令语句
		String importCommand = new StringBuffer("source ").append(importPath).toString();
		// 需要返回的命令语句数组
		String[] commands = new String[] { loginCommand, switchCommand, importCommand };
		return commands;
	}

}