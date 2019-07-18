package com.wusd.spring.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlUtils {
    public static void main(String[] args) throws DocumentException {
        XmlUtils xmlUtils = new XmlUtils();
        xmlUtils.test();
    }
    public void test() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(classPath("students.xml"));
        Element rootElement = document.getRootElement();
        getNodes(rootElement);
    }
    public InputStream classPath(String classPath) {
        return this.getClass().getClassLoader().getResourceAsStream(classPath);
    }
    public void getNodes(Element rootElement) {
        String name = rootElement.getName();
        System.out.println("name:" + name);
        List<Attribute> attributes = rootElement.attributes();
        for (Attribute attribute : attributes) {
            System.out.println(attribute.getName() + "---" +attribute.getValue());
        }
        String value = rootElement.getTextTrim();
        if (StringUtils.isNotEmpty(value)) {
            System.out.println("value:" + value);
        }
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            getNodes(element);
        }
    }
}
