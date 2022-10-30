package org.support.generator.plugins;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.List;

public class XmlFilter {

    public static void filterElements(XmlElement rootElement) {
        Iterator<Element> elementIterator = rootElement.getElements().iterator();
        while (elementIterator.hasNext()) {
            XmlElement xmlElement = (XmlElement) elementIterator.next();
            System.out.println(xmlElement.getName());
            List<Element> elementList = xmlElement.getElements();
            if ("resultMap".equals(xmlElement.getName())) {
                for (Element subElement : elementList) {
                    XmlElement subXmlElement = (XmlElement) subElement;
                    Iterator<Attribute> iterator = subXmlElement.getAttributes().iterator();
                    while (iterator.hasNext()) {
                        Attribute attribute = iterator.next();
                        if ("jdbcType".equals(attribute.getName())) {
                            iterator.remove();
                        }
                    }
                }
                System.out.println(JSONObject.toJSONString(elementList));
            } else {
                elementIterator.remove();
            }
        }
    }
}
