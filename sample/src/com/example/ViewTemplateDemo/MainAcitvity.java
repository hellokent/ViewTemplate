package com.example.ViewTemplateDemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.viewtemplate.L;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenyang.coder@gmail.com on 13-11-24 下午4:04.
 */
public class MainAcitvity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        long time = System.currentTimeMillis();
        SimpleDateFormat sbf = new SimpleDateFormat("MM.dd EEEE");
        L.v("date:%s", sbf.format(new Date(time)));

    }

    public void showLua(View view) {
        startActivity(new Intent(this, LuaDemoActivity.class));
    }

    public void showView(View view) {
        startActivity(new Intent(this, ViewTemplateActivity.class));
    }
}