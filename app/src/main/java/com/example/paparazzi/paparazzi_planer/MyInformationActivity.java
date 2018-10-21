package com.example.paparazzi.paparazzi_planer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.squareup.picasso.Picasso;

/**
 * Created by LeeJinKyu on 2018-04-18.
 */

public class MyInformationActivity extends AppCompatActivity {

    private TextView login_userName;
    private ImageView login_profile;
    private Button btn_logout;
    public static String nickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_my_information);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        //사용자정보를 메인엑티비티로부터 가져오기
        nickName = getIntent().getStringExtra("NICKNAME");
        String pImage = getIntent().getStringExtra("PROFILE_IMG");


        //카카오로그인 사용자이름 출력하기
        login_userName = (TextView)findViewById(R.id.login_userName);
        login_userName.setText(nickName);

        //카카오로그인 프로필사진 출력하기
        /*login_profile = (ImageView)findViewById(R.id.login_profile);
        Picasso.with(this)
                .load(pImage)
                .fit()
                .into(login_profile);*/

        btn_logout = (Button)findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.requestLogout(new LogoutResponseCallback() {

                    @Override

                    public void onCompleteLogout() {

                        Intent intent = new Intent(MyInformationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),nickName+"님 로그아웃 되었습니다." , Toast.LENGTH_SHORT).show();


                    }

                });
            }
        });

    }
}
