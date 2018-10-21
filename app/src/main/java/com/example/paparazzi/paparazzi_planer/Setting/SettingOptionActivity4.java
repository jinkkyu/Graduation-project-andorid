package com.example.paparazzi.paparazzi_planer.Setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.paparazzi.paparazzi_planer.R;

/**
 * Created by LeeJinKyu on 2018-06-04.
 */

public class SettingOptionActivity4 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_option3);
        //actionBar 객체를 가져옴
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
