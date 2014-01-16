package com.example.viewtemplate.xml;

import android.text.TextUtils;
import com.example.viewtemplate.L;
import com.example.viewtemplate.Utils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;

import static com.example.viewtemplate.Utils.Reflect.isSubclassOf;

/**
 * XML基础节点，所有的XML节点类均继承该节点
 * Created by chenyang.coder@gmail.com on 13-10-14 上午12:27.
 */
public class XmlNode implements Cloneable{

	private static class FieldMap extends HashMap<String, Field>{

		public void putField(final Field field){
			field.setAccessible(true);
            final String key = toFieldMapKey(field);
            if (key == null){
                return;
            }
            put(key, field);
		}

		public void scanClass(final Class<?> nodeClass){
            Class $class = nodeClass;
            do {
                for (Field field : $class.getDeclaredFields()){
                    putField(field);
                }
                $class = $class.getSuperclass();
            }while ($class != null);
		}

		public FieldMap(Class<?> nodeClss){
			super();
			scanClass(nodeClss);
		}
	}

	static final HashMap<Class, FieldMap> NODE_MAP_CACHE =
			new HashMap<Class, FieldMap>() {
				@Override
				public FieldMap get(final Object key) {
					if (key == null){
						return null;
					}
					FieldMap result = super.get(key);
					if (result == null || key instanceof Class){
						final Class clazz = (Class<?>) key;
						result = new FieldMap(clazz);
						put(clazz, result);
					}
					return result;
				}
			};

	private static HashMap<Class<?>, XmlNode> XMLNODE_CACHE = new HashMap<Class<?>, XmlNode>();

    private transient FieldMap mFieldMap;
	private String mLocalName;
	private String mValue;
	private XmlNode mParentNode;
	private String mComment;
    private ArrayList<XmlNode> mChilds = new ArrayList<XmlNode>();

    public XmlNode(){
        init(getClass().getAnnotation(Xml.class).value());
    }

    public XmlNode(final String localName){
        init(localName);
    }

    private void init(String local){
        mLocalName = local;
        mFieldMap = NODE_MAP_CACHE.get(getClass());
        //假如XMLNode的子类初始化完还没有localName，那一定是有问题的
        if (TextUtils.isEmpty(mLocalName) && getClass() == XmlNode.class){
            throw new RuntimeException(String.format("2B，这个类(%s)没有设localName，Fuck!!!", getClass().getName()));
        }
    }

	public final String getLocalName() {
		return mLocalName;
	}

	protected final void setLocalName(final String localName) {
		mLocalName = localName;
	}

	public final String getValue() {
		return mValue;
	}

	public final void setValue(final String value) {
		mValue = value;
	}

	protected final XmlNode getParentNode() {
		return mParentNode;
	}

    private void setParentNode(final XmlNode parentNode){
        assert parentNode != null;
        mParentNode = parentNode;
        parentNode.mChilds.add(this);
    }

	public final String getComment() {
		return mComment;
	}

	protected final void setComment(final String comment) {
		mComment = comment;
	}

	private FieldMap getFieldMap() {
		if (mFieldMap == null){
			mFieldMap = NODE_MAP_CACHE.get(getClass());
		}
		return mFieldMap;
	}

	@Override
	public XmlNode clone() {
		final XmlNode result;
		try {
			result = (XmlNode) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		if (mParentNode != null){
            result.setParentNode(mParentNode.clone());
		}
		for(Field field : getFieldMap().values()){
			try {
				if (field.get(this) == null){
					continue;
				}
				final Class fieldType = field.getType();
				final Object fieldObj = field.get(this);
				if (isSubclassOf(fieldType, XmlNode.class)){
					field.set(result, ((XmlNode) fieldObj).clone());
				} else if (isSubclassOf(fieldType, Collection.class)){
                    Collection nodes = (Collection) field.get(this).getClass().newInstance();
					nodes.addAll((Collection) fieldObj);
					field.set(result, nodes);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		result.mFieldMap = mFieldMap;
		return result;
	}

	@Override
	public String toString() {
		return toText();
	}

	//{{{private 工具方法.begin
	private static Xml getFieldXmlAnnotation(final Field field){
		if (field == null){
			return null;
		}
		return field.getAnnotation(Xml.class);
	}

	private static String toFieldMapKey(final Field field){
		final Class<?> type = field.getType();
		final Xml xml = getFieldXmlAnnotation(field);
        if (xml == null
                || (!Utils.Reflect.isSubclassOf(type, String.class)
                    && !Utils.Reflect.isSubclassOf(type, Collection.class)
                    && !Utils.Reflect.isSubclassOf(type, XmlNode.class))
                || (TextUtils.isEmpty(xml.value()) && Utils.Reflect.isSubclassOf(type, String.class))){
			return null;
		}

        final String localName = Utils.Reflect.getLocalNameFromField(field);

        if (isSubclassOf(type, Collection.class) || isSubclassOf(type, XmlNode.class)){
            return TextUtils.isEmpty(xml.tag()) ? getMapKey4Node(localName) : getMapKey4Tag(xml.tag());
        } else {
            return getMapKey4Attr(localName);
        }
	}

	private static String getMapKey4Attr(final String value){
		return value + "_attr";
	}

	private static String getMapKey4Node(final String value){
		return value + "_node";
	}

    private static String getMapKey4Tag(final String value){
        return value + "_tag";
    }

	static int sCloneCount = 0;
	static int sNewCount = 0;

	public static XmlNode newXMLNodeInstance(Class<?> nodeClass){
        if (nodeClass == null){
            return null;
        }

        if (!Utils.Reflect.isSubclassOf(nodeClass, XmlNode.class)){
            return null;
        }

        final Xml xml = nodeClass.getAnnotation(Xml.class);
        if (xml == null){
            return null;
        }

		XmlNode result = XMLNODE_CACHE.get(nodeClass);
		if (result != null){
			result = result.clone();
            result.mChilds.clear();
			++sCloneCount;
		}else {
			try {
                result = (XmlNode) nodeClass.newInstance();
				XMLNODE_CACHE.put(nodeClass, result.clone());
			} catch (Exception e) {
				e.printStackTrace();
			}
			++sNewCount;
		}
        return result;
	}
	//}}}工具方法.end

	public final String toText(){

        final StringWriter result = new StringWriter();
        final XmlSerializer serializer = android.util.Xml.newSerializer();
        try {
            serializer.setOutput(result);
            serializer.startDocument("utf-8", true);
            toText2(serializer);
            serializer.flush();
            serializer.endDocument();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
	}

    private void toText2(final XmlSerializer serializer) throws IOException, IllegalAccessException {

        serializer.startTag(null, getLocalName());
        //attr
        for (Map.Entry<String, Field> entry: getFieldMap().entrySet()){
            final Field field = entry.getValue();
            final Class type = entry.getValue().getType();
            try {
                if (entry.getValue().get(this) == null
                        || type == XmlNode.class
                        || isSubclassOf(type, XmlNode.class)
                        || isSubclassOf(type, Collection.class)){
                    continue;
                }

                serializer.attribute(null, field.getAnnotation(Xml.class).value(), field.get(this).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        LinkedList<XmlNode> childNodes = new LinkedList<XmlNode>();
        for (Map.Entry<String, Field> entry: getFieldMap().entrySet()){
            if (entry.getValue().get(this) == null){
                continue;
            }
            final Field field = entry.getValue();
            final Class type = entry.getValue().getType();

            if (type == XmlNode.class){
                XmlNode node = (XmlNode) field.get(this);
                node.setLocalName(entry.getKey());
                childNodes.add(node);
            } else if (isSubclassOf(type, XmlNode.class)){
                childNodes.add((XmlNode) field.get(this));
            } else if (isSubclassOf(type, Collection.class)){
                childNodes.addAll((Collection) field.get(this));
            }
        }
        if (!TextUtils.isEmpty(mValue)){
            serializer.text(mValue);
        }
        if (childNodes.isEmpty()){
            serializer.endTag(null, getLocalName());
        }else {
            for(XmlNode node : childNodes){
                node.toText2(serializer);
            }
        }
    }


    final void mapAttr(final String key, final String value){
		L.v("node(%s) map attr(%s:%s)", getClass().getSimpleName(), key, value);

		Field f = getFieldMap().get(getMapKey4Attr(key));
		if (f == null){
			return;
		}
		try {
			Utils.Reflect.setField(f, this, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

    /**
     * 根据local获取一个XmlNode的实例，要求这个实例是在当前节点的属性中存在的
     * @param childLocalName 子localName
     * @return 生成的XmlNode
     */
	protected final XmlNode getChildNodeInstance(String childLocalName){
		Class<?> nodeClz = NodeScanner.LOCAL_NODE_MAP.getValue(childLocalName);

		Field childField = getFieldMap().get(getMapKey4Node(childLocalName));

        if (childField == null){ //假如用普通的防止找不到，就用tag去寻找
            XmlTags xmlTags = nodeClz.getAnnotation(XmlTags.class);
            if (xmlTags == null){
                return null;
            }
            String[] tagArray = xmlTags.value();
            for (String tag : tagArray){
                childField = getFieldMap().get(getMapKey4Tag(tag));
                if (childField != null){
                    break;
                }
            }

            if (childField == null){
                return null;
            }
		}

		XmlNode result = null;
		if (nodeClz == null || nodeClz == XmlNode.class){
			result = new XmlNode(childLocalName);
		} else {
			try {
				result = newXMLNodeInstance(nodeClz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        L.v("node(%s) add child(%s)", getClass().getSimpleName(), result.getClass().getSimpleName());

        try {
            childField.setAccessible(true);
            if (isSubclassOf(childField.getType(), Collection.class)){
                ((Collection) childField.get(this)).add(result);
            } else if (isSubclassOf(childField.getType(), XmlNode.class)){
                childField.set(this, result);
            }
            result.setParentNode(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
		return result;
	}

	protected void onFinishMapAttr(){
	}

    public ArrayList<XmlNode> getChilds() {
        return mChilds;
    }

}
