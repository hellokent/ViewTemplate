package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;

import java.util.ArrayList;

@Xml("scanners")
public class Scanners extends XmlNode {

    @Xml("name")
    public String name;

    @Xml()
    public ArrayList<Scanner> scanners = new ArrayList<Scanner>();
}
