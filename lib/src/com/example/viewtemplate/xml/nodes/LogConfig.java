package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;

import java.util.ArrayList;
import java.util.List;

@Xml("log_config")
public class LogConfig extends XmlNode {

    @Xml()
    public List<Item> items = new ArrayList<Item>();

}
