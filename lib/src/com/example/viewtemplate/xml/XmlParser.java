package com.example.viewtemplate.xml;

import com.example.viewtemplate.L;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * XML解析器回调
 * Created by chenyang.coder@gmail.com on 13-10-14 上午12:22.
 */
public class XmlParser<T extends XmlNode> extends DefaultHandler2{

	final Class<T> kNodeClass;
	T mRootNode;
	XmlNode mCurrentNode;
	int mLevel;
	StringBuilder mValueBuilder;
	StringBuilder mCommentBuilder;
	IParseCallback mParseCallback;

	public XmlParser(Class<T> nodeClass, IParseCallback parseCallback){
		kNodeClass = nodeClass;
		try {
			mRootNode = (T) XmlNode.newXMLNodeInstance(nodeClass);
		} catch (Exception e) {
			e.printStackTrace();
			mRootNode = null;
		}
		mParseCallback = parseCallback;
	}

	public XmlParser(Class<T> nodeClass){
		this(nodeClass, null);
	}

	public T getRootNode() {
		return mRootNode;
	}

	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		super.comment(ch, start, length);
        L.v("comment:%s", new String(ch, start, length));
		mCommentBuilder.append(ch, start, length);
	}



	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		mCurrentNode = mRootNode;
		mLevel = 0;
		mCommentBuilder = new StringBuilder();
		mValueBuilder = new StringBuilder();
		if (mParseCallback != null){
			mParseCallback.onStartDocument();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		mCurrentNode = null;
		mLevel = 0;
		if (mRootNode != null){
			mRootNode.setComment(mCommentBuilder.toString());
		}
		mCommentBuilder = null;
		mValueBuilder = null;
		if (mParseCallback != null){
			mParseCallback.onEndDocument();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		++mLevel;

		if (mCurrentNode == null){
			return;
		}

		if (mLevel > 1){
			mCurrentNode = mCurrentNode.getChildNodeInstance(localName);
		} else if (mLevel == 1 && !mCurrentNode.getLocalName().equalsIgnoreCase(localName)){
			mCurrentNode = null;
		}

		if (mCurrentNode == null){
			return;
		}

		L.v("start element, localName=%s, current(%s):%s",
				localName,
				mCurrentNode.getClass().getSimpleName(),
				mCurrentNode.getLocalName());


		for(int i = 0, n = attributes.getLength(); i < n; ++i){
			mCurrentNode.mapAttr(attributes.getLocalName(i), attributes.getValue(i));
		}
        mCurrentNode.onFinishMapAttr();
		if (mParseCallback != null){
			mParseCallback.onStartNode(mCurrentNode);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		--mLevel;
		if (mCurrentNode == null || !mCurrentNode.getLocalName().equalsIgnoreCase(localName)){
			return;
		}

		mCurrentNode.setValue(mValueBuilder.toString());
		mValueBuilder.delete(0, mValueBuilder.length());
		mCurrentNode = mCurrentNode.getParentNode();

		if (mParseCallback != null){
			mParseCallback.onEndNode(mCurrentNode);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		mValueBuilder.append(new String(ch, start, length).trim());
	}
}
