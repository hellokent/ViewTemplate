package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;

@Xml("item")
public class Item extends XmlNode {

    @Xml("package")
    public String packageName;

    @Xml()
    public Tag tag;

}
