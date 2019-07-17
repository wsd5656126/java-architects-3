package com.wusd.spring.core;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class ClassPathXmlApplicationContext {
    private String xmlPath;

    public ClassPathXmlApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Object getBean(String id) throws Exception {
        List<Element> elements = readXml();

        String className = findXmlByIDClass(elements, id);
        if (StringUtils.isEmpty(className)) {
            throw new Exception("未找到该类");
        }

        Class<?> clazz = Class.forName(className);

        Object o = clazz.newInstance();
        return o;
    }

    public InputStream getClassXmlInputStream(String xmlPath) {
        return this.getClass().getClassLoader().getResourceAsStream(xmlPath);
    }

    public String findXmlByIDClass(List<Element> elements, String id) {
        for (Element element : elements) {
            Attribute attribute = element.attribute("id");
            String value = attribute.getValue();
            if (StringUtils.equals(id, value)) {
                return element.attributeValue("class");
            }
        }
        return null;
    }

    private List<Element> readXml() throws Exception {
        SAXReader saxReader = new SAXReader();
        if (StringUtils.isEmpty(xmlPath))
            throw new Exception("路径不能为空");
        Document document = saxReader.read(getClassXmlInputStream(xmlPath));
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        if (elements == null || elements.isEmpty())
            return null;
        return elements;
    }
}
