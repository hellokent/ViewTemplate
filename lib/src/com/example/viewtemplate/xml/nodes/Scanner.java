package com.example.viewtemplate.xml.nodes;

import com.example.viewtemplate.Utils;
import com.example.viewtemplate.classscanner.ScannerListener;
import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;

import java.util.ArrayList;

@Xml("scanner")
public class Scanner extends XmlNode {

    @Xml("package")
    public ArrayList<XmlNode> packageNames = new ArrayList<XmlNode>();

    @Xml("processor")
    String processor;

    Class<?> mProcessorClass;

    @Override
    protected void onFinishMapAttr() {
        super.onFinishMapAttr();
        final String name = ((Scanners)getParentNode()).name;
        final String processorClassName = (processor.startsWith(".") ? name : "") + processor;

        try {
            mProcessorClass = Class.forName(processorClassName);
            if (!Utils.Reflect.isSubclassOf(mProcessorClass, ScannerListener.class)){
                mProcessorClass = null;
            }
        } catch (ClassNotFoundException e) {
            mProcessorClass = null;
            e.printStackTrace();
        }
    }

    public ScannerListener getIScanner(){
        if (mProcessorClass == null){
            return null;
        }

        try {
            return (ScannerListener)mProcessorClass.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
