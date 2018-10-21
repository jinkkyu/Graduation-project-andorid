package com.example.paparazzi.paparazzi_planer.Setting;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.paparazzi.paparazzi_planer.AlarmReceiver;
import com.example.paparazzi.paparazzi_planer.R;

/**
 * Created by LeeJinKyu on 2018-06-04.
 */

public class SettingOptionActivity1 extends AppCompatActivity {

    AudioManager audioManager;
    Switch pushmain,swiSound,swiVibrate;
    LinearLayout linear1,linear2;
    View view1,view2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_option1);
        //actionBar 객체를 가져옴
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        swiSound=findViewById(R.id.swiSound);
        swiVibrate=findViewById(R.id.swiVibrate);
        linear1=findViewById(R.id.linear1);
        linear2=findViewById(R.id.linear2);
        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);
        pushmain=findViewById(R.id.pushmain);

        pushmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushmain.isChecked() == true) {
                    linear1.setVisibility(View.VISIBLE);
                    linear2.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                } else {
                    recreate();
                }
            }
        });
        swiSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (swiSound.isChecked()==true) {
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }else {
                    //False일때

                }*/
            }
        });
        swiVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (swiVibrate.isChecked()==true){
                    audioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }else {
                    //False일때
                }*/
            }
        });
    }
}