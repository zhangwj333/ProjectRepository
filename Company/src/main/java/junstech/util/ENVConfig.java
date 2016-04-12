package junstech.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ENVConfig {

	public static void initializingLanguage() throws Exception {
		File xmlFile = new File(ENVConfig.class.getClassLoader().getResource("LanguageZHCN.xml").getPath());

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = builderFactory.newDocumentBuilder();

		Document doc = builder.parse(xmlFile);

		doc.getDocumentElement().normalize();

		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("String");

		for (int i = 0; i < nList.getLength(); i++) {

			Node node = nList.item(i);
			Element ele = (Element) node;
			RedisUtil.setString(ele.getAttribute("key"), ele.getTextContent());
			System.out.println("Node name: " + ele.getAttribute("key") + "  Element name: "+" "+ele.getTextContent());
		}
	}

	static{
		try {
			initializingLanguage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String encryptKey = "JunstechCompany";
	
}
