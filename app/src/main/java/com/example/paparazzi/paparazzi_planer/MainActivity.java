package com.example.paparazzi.paparazzi_planer;

import android.*;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.Memo.Memo;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    public static String username;
    private Fragment fragment;
    public Memo memo= new Memo();
    private FragmentManager fragmentManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.navigation_home:
                    fragment = new TodayFragment();
                    break;

                case R.id.navigation_dashboard:
                    fragment = new ProgressFragment();
                    break;

                case R.id.navigation_notifications:
                    fragment = new SettingFragment();
                    /*Intent i=new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(i);*/
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.addToBackStack(null); 스택안쌓게하기
            transaction.setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
            transaction.replace(R.id.main_container, fragment).commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 뒤로가기 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = new TodayFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        //메인엑티비티 첫화면을 프로래먼트를 오늘할일으로 시작하겠다.
        transaction.replace(R.id.main_container, fragment).commit();

        //DB값 받아오기
        //Intent intent = getIntent();
        //username = "이진규님";
                //intent.getExtras().getString("NICKNAME");

        //버전체크해서 마시멜로우 보다 낮으면 런타임 권한 체크를 하지 않는다
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            checkPermission();
        }else {
            loadData();
        }
    }

    private final int REQ_CODE = 100;
    //1. 권한 체크
    @TargetApi(Build.VERSION_CODES.M) //target 지정 annotation
    private void checkPermission() {
        // 1.1 런타임 권한 체크
        if( checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //1.2 요청할 권한 목록 작성
            String permArr[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_CONTACTS};
            //1.3 시스템에 권한 요청
            requestPermissions(permArr, REQ_CODE);
        }else {
            loadData();
        }
    }

    //2. 권한 체크 후 call back < 사용자가 확인 후 시스템이 호출하는 함수

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE) {
            //배열에 넘긴 런타임권한을 체크해서 승인이 됐으면
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                //프로그램 실행
                loadData();
            } else {
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                //선택 1 . 종료  / 2. 권한체크 다시 물어보기
                //finish();
            }
        }
    }


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.my_information) {

            //사용자정보 보내주기
            Intent intent = new Intent(MainActivity.this, MyInformationActivity.class);
            intent.putExtra("NICKNAME",getIntent().getStringExtra(KakaoSignupActivity.NICKNAME));
            intent.putExtra("PROFILE_IMG",getIntent().getStringExtra(KakaoSignupActivity.PROFILE_IMG));
            startActivity(intent);
        }

        switch (id = item.getItemId()){
            case R.id.my_information:
                //사용자정보 보내주기
                Intent intent = new Intent(MainActivity.this, MyInformationActivity.class);
                intent.putExtra("NICKNAME",getIntent().getStringExtra(KakaoSignupActivity.NICKNAME));
                intent.putExtra("PROFILE_IMG",getIntent().getStringExtra(KakaoSignupActivity.PROFILE_IMG));
                startActivity(intent);
                break;
            case R.id.smart_list_menu:
                Intent intent1 = new Intent(MainActivity.this, SmartListActivity.class);
                startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }

    //3. data 읽어오기 (시스템 실행)
    private void loadData() {


    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


}
