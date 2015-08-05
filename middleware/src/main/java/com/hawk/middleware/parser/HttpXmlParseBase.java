package com.hawk.middleware.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class HttpXmlParseBase {

	
	protected abstract String getNodeString();
	
	protected abstract <T> List<T> getStoreList();
	
	protected abstract <T> T parseNode(Element element);
	
	protected <T> List<T> parseList(InputStream inputStream)
	{
		//返回结果列表
		List<T> resultList = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc = null;
		
		try {
			doc = db.parse(inputStream);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resultList = getStoreList();
		if(resultList == null)
			return null;
		
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getElementsByTagName(getNodeString());
		
		for(int i=0;i < nodeList.getLength();i++)
		{
			Element nodeElement = (Element)nodeList.item(i);
			T node = parseNode(nodeElement);
			
			if(node != null)
				resultList.add(node);
		}
		
		return resultList;
	}

}
