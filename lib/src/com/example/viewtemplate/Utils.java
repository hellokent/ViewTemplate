package com.example.viewtemplate;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.viewtemplate.classscanner.ScannerUtil;
import com.example.viewtemplate.xml.IParseCallback;
import com.example.viewtemplate.xml.NodeScanner;
import com.example.viewtemplate.xml.XmlNode;
import com.example.viewtemplate.xml.XmlParser;

import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

/**
 * 工具类
 * 原则：
 * 1. 工具类本身尽量少处理异常，异常由调用方处理
 * Created by chenyang.coder@gmail.com on 13-10-14 上午12:33.
 */
public final class Utils {

    private static Application sApp;

    public static Application getApp() {
        return sApp;
    }

    public static void initUtils(final Application app) {
        if (sApp != null){
            throw new IllegalStateException("sApp should init once");
        }
        sApp = app;
        try {
            ScannerUtil.INSTANCE.scanClass(sApp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DisplayMetrics metric = new DisplayMetrics();

        ((WindowManager)(sApp.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(metric);
        App.sDensity = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5 / 2.0）
        App.sScreenWidth = metric.widthPixels;
        App.sScreenHeight = metric.heightPixels;

    }

    public static final class Reflect{

		/**
		 * 判断type是否是superType的子类型
		 * @param type 子类型
		 * @param superClass 父类型
		 * @return 假如有父子关系，或者子类型实现了父类型的接口，返回true，否则返回false
		 */
		public static boolean isSubclassOf(Class<?> type, Class<?> superClass) {
			if (type == null){
				return false;
			}
			if (type.equals(superClass)){
				return true;
			}
			Class[] interfaces = type.getInterfaces();
			for (Class i : interfaces){
				if (isSubclassOf(i, superClass)){
					return true;
				}
			}
			Class superType = type.getSuperclass();
			return superType != null && isSubclassOf(superType, superClass);
		}

        public static <T> T newInstance(final Class<T> clazz, Object... arg) throws
                IllegalAccessException,
                InstantiationException,
                NoSuchMethodException,
                InvocationTargetException {
            if (clazz == null){
                return null;
            }

            if(arg == null){
                return clazz.newInstance();
            }

            Class[] argumentClasses = new Class[arg.length];

            for (int i = 0, n = arg.length; i < n; ++i){
                argumentClasses[i] = arg[i].getClass();
            }

            return clazz.getDeclaredConstructor(argumentClasses).newInstance(arg);
        }

        public static Class getCollectionType(final Field field){
            Type t = field.getGenericType();
            if (t instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType)t;
                return (Class) pt.getActualTypeArguments()[0];
            }
            return null;
        }

        public static String getLocalNameFromClz(Class<?> nodeClz){
            if (NodeScanner.LOCAL_NODE_MAP.containsValue(nodeClz)){
                return NodeScanner.LOCAL_NODE_MAP.getKey(nodeClz);
            }
            if (nodeClz == null
                    || !Reflect.isSubclassOf(nodeClz, XmlNode.class)
                    || nodeClz.getAnnotation(com.example.viewtemplate.xml.Xml.class) == null){
                return null;
            }

            String result = nodeClz.getAnnotation(com.example.viewtemplate.xml.Xml.class).value();
            NodeScanner.LOCAL_NODE_MAP.put(result, nodeClz);
            return result;
        }

        public static String getLocalNameFromField(Field field){
            Class<?> nodeClz = field.getType();

            if (isSubclassOf(nodeClz, Collection.class)){
                nodeClz = getCollectionType(field);
            }

            if (nodeClz == String.class || nodeClz == XmlNode.class){
                return field.getAnnotation(com.example.viewtemplate.xml.Xml.class).value();
            }
            return getLocalNameFromClz(nodeClz);
        }



		/**
		 * 1.将字符串的数据，根据Field里面的不同Type，解析成相应的对象
		 * 2. 通过反射，将解析后的数据塞入obj里面
		 * Type目前支持Java原生类型
		 */
		public static Object setField(final Field f, final Object obj, String str) throws IllegalAccessException {
			assert f != null;
			assert !TextUtils.isEmpty(str);

			final Class type = f.getType();
			IParseObj iParseObj = PARSE_MAP.get(type);
			final Object dstObj;
			if (iParseObj != null){
				try {
					dstObj = iParseObj.parse(str);
				} catch (Throwable throwable){
					throwable.printStackTrace();
					return null;
				}
			} else if (isSubclassOf(type, Enum.class)){
				dstObj = Enum.valueOf((Class<? extends Enum>) type, str);
			} else {
				dstObj = f.get(obj);
			}

			f.set(obj, dstObj);
            return dstObj;
		}


		private static interface IParseObj {
			Object parse(String src);
		}

		static final HashMap<Class, IParseObj> PARSE_MAP = new HashMap<Class, IParseObj>(){
			{
				put(Byte.class, new ParseByte());
				put(byte.class, new ParseByte());
				put(Short.class, new ParseShort());
				put(short.class, new ParseShort());
				put(Integer.class, new ParaseInteger());
				put(int.class, new ParaseInteger());
				put(Float.class, new ParseFloat());
				put(float.class, new ParseFloat());
				put(Double.class, new ParseDouble());
				put(double.class, new ParseDouble());
				put(String.class, new ParseString());
			}
		};

		private static class ParseString implements IParseObj {

			@Override
			public Object parse(String src) {
				return src;
			}
		}

		private static class ParseDouble implements IParseObj {

			@Override
			public Object parse(String src) {
				return Double.parseDouble(src);
			}
		}

		private static class ParseFloat implements IParseObj {

			@Override
			public Object parse(String src) {
				return Float.parseFloat(src);
			}
		}

		private static class ParaseInteger implements IParseObj {

			@Override
			public Object parse(String src) {
				return Integer.parseInt(src);
			}
		}

		private static class ParseShort implements IParseObj {

			@Override
			public Object parse(String src) {
				return Short.parseShort(src);
			}
		}

		private static class ParseByte implements IParseObj {

			@Override
			public Object parse(String src) {
				return Byte.decode(src);
			}
		}


	}

	public static final class Text {

		public static String multiText(final CharSequence chars, final int count){
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < count; ++i){
				result.append(chars);
			}
			return result.toString();
		}

        public static String layoutSizeToString(int size) {
            if (size == ViewGroup.LayoutParams.WRAP_CONTENT) {
                return "wrap-content";
            }
            if (size == ViewGroup.LayoutParams.MATCH_PARENT) {
                return "match-parent";
            }
            return String.valueOf(size);
        }

        public static String readStream2String(final InputStream is) throws IOException {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            StringBuilder result = new StringBuilder();
            while ((str = br.readLine())!= null){
                result.append(str).append('\n');
            }
            return result.toString();
        }

	}

	public static final class Xml{

		final static SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

		public static <T extends XmlNode> T parseString(final CharSequence str, final Class<T> nodeClass){
			return parseString(str, nodeClass, null);
		}

        public static <T extends XmlNode> T parseStream(final InputStream stream, final Class<T> nodeClass){
            return parseStream(stream, nodeClass, null);
        }


        public static <T extends XmlNode> T parseStream(final InputStream stream, final Class<T> nodeClass, IParseCallback parseCallback){
            final XmlParser<T> parser = new XmlParser<T>(nodeClass, null);
            try {
                SAX_PARSER_FACTORY.newSAXParser().parse(stream, parser);
                return parser.getRootNode();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

		public static <T extends XmlNode> T parseString(final CharSequence str, final Class<T> nodeClass, IParseCallback parseCallback){
            if( str == null){
                return null;
            }
            return parseStream(new ByteArrayInputStream(str.toString().getBytes()), nodeClass, parseCallback);
		}

	}

    public static final class App{

        public static float sDensity = 0f;
        public static int sScreenWidth = 0;
        public static int sScreenHeight = 0;

        public static String getMetaString(String name) {
            try {
                final ApplicationInfo ai = sApp.getPackageManager().getApplicationInfo(sApp.getPackageName(),
                        PackageManager.GET_META_DATA);
                if (ai == null || ai.metaData == null){
                    return null;
                }
                return ai.metaData.getString(name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static int getPXFromDP(int dp){
            return (int)(sDensity * dp);
        }
    }
}
