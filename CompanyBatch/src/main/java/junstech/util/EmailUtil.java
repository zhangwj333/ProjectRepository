package junstech.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.AddressException;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtil {

	public static HashMap<String, String> EmailInfo = new HashMap<String, String>();

	// 收件箱
	final static String to = "zhangwj333@hotmail.com";

	public static void main(String[] args) {

		sendMail(to, "Backup for 2016-05-11", "Please find the backup in the attachment", new File("C:/DBbackup20160511.sql"));
	}

	public static boolean sendMailToOwnCompany(String subject, String content) {
		return sendMail(to, subject, content, new File[] {});
	}
	
	public static boolean sendMailToOwnCompany(String subject, String content, File attachment) {
		return sendMail(to, subject, content, new File[] { attachment });
	}
	
	public static boolean sendMailToOwnCompany(String subject, String content, File[] attachments) {
		return sendMail(to, subject, content, attachments);
	}
	
	public static boolean sendMail(String receiver, String subject, String content) {
		return sendMail(receiver, subject, content, new File[] {});
	}

	public static boolean sendMail(String receiver, String subject, String content, File attachment) {
		return sendMail(receiver, subject, content, new File[] { attachment });
	}

	public static boolean sendMail(String receiver, String subject, String content, File[] attachments) {
		try {
			Session session = init();
			if(session == null){
				throw new MessagingException("E-mail Process Fail to Initialize");
			}
			Message message = new MimeMessage(init());
			message.setFrom(new InternetAddress(EmailInfo.get("username")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

			message.setSubject(subject);

			// 添加附件的内容
            if (attachments.length > 0) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setContent(content, "text/html;charset=UTF-8");// 给BodyPart对象设置内容和格式/编码方式  
                Multipart mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放BodyPart对象  
                mm.addBodyPart(attachmentBodyPart);// 将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)  
                // 把mm作为消息对象的内容  
                MimeBodyPart filePart;  
                FileDataSource filedatasource;  
                // 逐个加入附件  
                for (int i = 0; i < attachments.length; i++) {  
                    filePart = new MimeBodyPart();  
                    filedatasource = new FileDataSource(attachments[i]);  
                    filePart.setDataHandler(new DataHandler(filedatasource));  
                    try {  
                        filePart.setFileName(MimeUtility.encodeText(filedatasource.getName()));  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                    mm.addBodyPart(filePart);  
                }  
                message.setContent(mm); 
            }else{
            	message.setText(content);
            }
            
            // 保存邮件
            message.saveChanges();
            
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Session init() {
		Properties props = new Properties();

		props.put("mail.smtp.auth", EmailInfo.get("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", EmailInfo.get("mail.smtp.starttls.enable"));

		// 不做服务器证书校验
		props.put("mail.smtp.ssl.checkserveridentity", EmailInfo.get("mail.smtp.ssl.checkserveridentity"));

		// 添加信任的服务器地址，多个地址之间用空格分开
		props.put("mail.smtp.ssl.trust", EmailInfo.get("mail.smtp.ssl.trust"));
		props.put("mail.smtp.host", EmailInfo.get("mail.smtp.host"));
		props.put("mail.smtp.port", EmailInfo.get("mail.smtp.port"));

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				try {
					return new PasswordAuthentication(EmailInfo.get("username"),
							AESEncryption.decrypt(EmailInfo.get("password"), ENVConfig.encryptKey));
				} catch (Exception e) {
					return null;
				}
			}
		});
		session.setDebug(true);
		return session;

	}

	static {
		try {
			InputStream is = BackupUtil.class.getClassLoader().getResourceAsStream("Email.properties");
			Properties properties = new Properties();
			properties.load(is);

			EmailInfo.put("username", properties.getProperty("username"));
			EmailInfo.put("password", properties.getProperty("password"));
			EmailInfo.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
			EmailInfo.put("mail.smtp.starttls.enable", properties.getProperty("mail.smtp.starttls.enable"));
			EmailInfo.put("mail.smtp.ssl.checkserveridentity",
					properties.getProperty("mail.smtp.ssl.checkserveridentity"));
			EmailInfo.put("mail.smtp.ssl.trust", properties.getProperty("mail.smtp.ssl.trust"));
			EmailInfo.put("mail.smtp.host", properties.getProperty("mail.smtp.host"));
			EmailInfo.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}