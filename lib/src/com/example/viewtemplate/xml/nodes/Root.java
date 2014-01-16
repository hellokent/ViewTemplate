package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.xml.Tags;
import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;

@Xml("root")
public class Root extends XmlNode {

    @Xml(tag = Tags.VIEW)
    public BaseViewNode viewNode;

    @Xml()
    public Lua lua;
}
