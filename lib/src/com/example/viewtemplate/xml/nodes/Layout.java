package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.xml.Tags;
import com.example.viewtemplate.xml.Xml;

import java.util.LinkedList;

@Xml("layout")
public class Layout extends BaseViewNode {

    @Xml("orientation")
    public String orientation;

    @Xml(tag = Tags.VIEW)
    public LinkedList<BaseViewNode> texts = new LinkedList<BaseViewNode>();

}
