package com.example.viewtemplate.xml;

/**
 * XML解析的回调，用于外界观察解析的过程
 * Created by chenyang.coder@gmail.com on 13-10-24 下午9:53.
 */
public interface IParseCallback {
	void onStartDocument();
	void onEndDocument();
	void onStartNode(XmlNode node);
	void onEndNode(XmlNode node);
}
