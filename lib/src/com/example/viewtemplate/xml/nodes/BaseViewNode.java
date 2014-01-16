package com.example.viewtemplate.xml.nodes;

import android.text.TextUtils;
import android.util.Pair;
import com.example.viewtemplate.xml.Tags;
import com.example.viewtemplate.xml.Xml;
import com.example.viewtemplate.xml.XmlNode;
import com.example.viewtemplate.xml.XmlTags;

/**
 * Created by chenyang.coder@gmail.com on 13-10-14 上午12:46.
 */
@XmlTags(Tags.VIEW)
public abstract class BaseViewNode extends XmlNode {

	private Pair<MeasuringUnit, Integer> mWidthInfo;
	private Pair<MeasuringUnit, Integer> mHeightInfo;

	@Xml("id")
	public String id;

	@Xml("bg")
	public String bg;

	@Xml("width")
	private String width;

	@Xml("height")
	private String height;

    @Xml("onclick")
    public String onClick;

    @Override
	protected void onFinishMapAttr() {
		super.onFinishMapAttr();
        MeasuringUnit unit;
		try {
			unit = calcUnit(width);
			if (unit != null) {
				mWidthInfo = new Pair<MeasuringUnit, Integer>(unit, unit.getData(width));
			}
		}catch (Throwable e){
            e.printStackTrace();
        }

		try {
			unit = calcUnit(height);
			if (unit != null) {
				mHeightInfo = new Pair<MeasuringUnit, Integer>(unit, unit.getData(height));
			}
		}catch (Throwable e) {
            e.printStackTrace();
        }
	}

	private MeasuringUnit calcUnit(String str){
		if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())){
			return null;
		}
		str = str.toLowerCase();
		for (MeasuringUnit unit : MeasuringUnit.values()){
			if (unit.check(str)){
				return unit;
			}
		}
		return null;
	}

	public static interface ICalculateMeasuringUnit{
		boolean check(String str);
		int getData(String str);
	}

	public Pair<MeasuringUnit, Integer> getWidthInfo() {
		return mWidthInfo;
	}

    @Override
    public BaseViewNode clone() {
        BaseViewNode result = (BaseViewNode) super.clone();
        if (mWidthInfo != null){
            result.mWidthInfo = Pair.create(mWidthInfo.first, mWidthInfo.second);
        }
        if (mHeightInfo != null){
            result.mHeightInfo = Pair.create(mHeightInfo.first, mHeightInfo.second);
        }
        return result;
    }

    public Pair<MeasuringUnit, Integer> getHeightInfo() {
		return mHeightInfo;
	}
}
