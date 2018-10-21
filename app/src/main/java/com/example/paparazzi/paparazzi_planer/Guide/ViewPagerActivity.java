package com.example.paparazzi.paparazzi_planer.Guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.paparazzi.paparazzi_planer.LoginActivity;
import com.example.paparazzi.paparazzi_planer.MainActivity;
import com.example.paparazzi.paparazzi_planer.R;

public class ViewPagerActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewPager mPager;
    private int[] layout = {R.layout.guide_first_slide,R.layout.guide_second_slide, R.layout.guide_thrid_slide, R.layout.guide_forth_slide, R.layout.guide_final_slide};
    private MpagerAdapter mpagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button btnSkip,btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if(Build.VERSION.SDK_INT >=19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/


        setContentView(R.layout.guide_activity_viewpager);

        mPager = (ViewPager)findViewById(R.id.viewPager);
        mpagerAdapter = new MpagerAdapter(layout,this);
        mPager.setAdapter(mpagerAdapter);

        Dots_Layout = (LinearLayout)findViewById(R.id.dotsLayout);
        btnSkip = (Button)findViewById(R.id.btnSkip);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        createDots(0);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            // 마지막페이지 NEXT를 START로 변경
            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if (position==layout.length-1){
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.INVISIBLE);
                }else {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



    // circle indicator생성
    private void createDots(int current_position){
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[layout.length];

        for (int i = 0; i<layout.length; i++){

            dots[i] = new ImageView(this);
            if (i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.defult_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNext:
                    loadNext();
                    break;
            case R.id.btnSkip:
                loadHome();
                break;
        }


    }

    //SKIP버튼 클릭시 호출 함수
    private void loadHome(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    //NEXT버튼 클릭시 호출 함수
    private void loadNext(){
        int next_slide = mPager.getCurrentItem()+1;

        if (next_slide<layout.length){
            mPager.setCurrentItem(next_slide);
        }else {
            loadHome();
        }
    }

}
