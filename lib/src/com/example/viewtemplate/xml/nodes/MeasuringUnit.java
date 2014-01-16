package com.example.viewtemplate.xml.nodes;

import android.text.TextUtils;
import android.view.ViewGroup;

/**
 * 测量单位的枚举类
 * Created by chenyang.coder@gmail.com on 13-10-24 下午10:56.
 */
public enum MeasuringUnit implements BaseViewNode.ICalculateMeasuringUnit {
	PX {
		@Override
		public boolean check(final String str) {
			return str.endsWith("px") && TextUtils.isDigitsOnly(str.replaceAll("px", ""));
		}

		@Override
		public int getData(final String str) {
			return Integer.parseInt(str.replaceAll("px", ""));
		}
	},
	DP {
		@Override
		public boolean check(final String str) {
			return (str.endsWith("dp") && TextUtils.isDigitsOnly(str.replaceAll("dp", "")))
					|| (str.endsWith("dip") && TextUtils.isDigitsOnly(str.replaceAll("dip", "")));
		}

		@Override
		public int getData(final String str) {
			if (str.endsWith("dip")){
				return Integer.parseInt(str.replaceAll("dip", ""));
			} else {
				return Integer.parseInt(str.replaceAll("dp", ""));
			}
		}
	},
	PRECENTAGE {
		@Override
		public boolean check(final String str) {
			return str.length() > 2
					&& str.charAt(str.length() - 1) == '%'
					&& TextUtils.isDigitsOnly(str.substring(0, str.length() - 1).replace('.', '0'));
		}

		@Override
		public int getData(final String str) {
			return (int) Double.parseDouble(str.substring(0, str.length() - 2));
		}
	},
	MATCH {
		@Override
		public boolean check(final String str) {
			return str.startsWith("match");
		}

		@Override
		public int getData(final String str) {
			return ViewGroup.LayoutParams.MATCH_PARENT;
		}
	},
	WRAP {
		@Override
		public boolean check(final String str) {
			return str.startsWith("wrap");
		}

		@Override
		public int getData(final String str) {
			return ViewGroup.LayoutParams.WRAP_CONTENT;
		}
	};

    @Override
    public boolean check(final String str) {
        return MeasuringUnit.valueOf(name()).check(str);
    }

    @Override
    public int getData(final String str) {
        return MeasuringUnit.valueOf(name()).getData(str);
    }

}
