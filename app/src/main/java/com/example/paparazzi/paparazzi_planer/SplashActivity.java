package com.example.paparazzi.paparazzi_planer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.paparazzi.paparazzi_planer.Guide.ViewPagerActivity;


public class SplashActivity extends Activity {

    private ImageView img;
    private Animation anim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(),4000);
        iniView();
    }

    private void iniView(){
        img = (ImageView) findViewById(R.id.ani);
        anim = AnimationUtils.loadAnimation(this, R.anim.loading);
        img.setAnimation(anim);
    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(),ViewPagerActivity.class));
            SplashActivity.this.finish();
        }
    }
}
