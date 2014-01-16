package com.example.viewtemplate.classscanner;

/**
 * 扫描器的回调接口，Scanner的Begin和End默认可以不实现，所以用了抽象类
 * Created by chenyang.coder@gmail.com on 13-11-19 下午3:41.
 */
public abstract class ScannerListener {

    public ScannerListener(){
    }

    public void onScanBegin(){}

    public abstract void onScan(Class<?> clazz);

    public void onScanEnd(){}
}
