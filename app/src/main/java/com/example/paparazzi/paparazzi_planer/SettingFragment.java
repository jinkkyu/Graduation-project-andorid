package com.example.paparazzi.paparazzi_planer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.example.paparazzi.paparazzi_planer.Setting.SettingAdapter;
import com.example.paparazzi.paparazzi_planer.Setting.SettingOptionActivity1;
import com.example.paparazzi.paparazzi_planer.Setting.SettingOptionActivity2;
import com.example.paparazzi.paparazzi_planer.Setting.SettingOptionActivity3;
import com.example.paparazzi.paparazzi_planer.Setting.SettingOptionActivity4;
import com.example.paparazzi.paparazzi_planer.Setting.Settingitem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by LeeJinKyu on 2018-04-12.
 */

public class SettingFragment extends Fragment {
    public SettingFragment(){
    }


    SimpleDateFormat mmonth = new SimpleDateFormat("M");
    SimpleDateFormat mday = new SimpleDateFormat("dd");
    long mNow;
    Date mDate;

    String m,dd;
    Long check_count, today, important_count;
    Button btn_login, btn_logout;
    public TextView hi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.setting_fragment,container,false);
        ListView listView=(ListView)view.findViewById(R.id.settinglist);
        TextView txt_total=(TextView)view.findViewById(R.id.txt_total) ;
        TextView txt_finish = (TextView)view.findViewById(R.id.txt_finish);
        TextView txt_todaywork = (TextView)view.findViewById(R.id.txt_todaywork);
        TextView txt_work = (TextView)view.findViewById(R.id.txt_important);
        hi = (TextView)view.findViewById(R.id.login_userName);
        btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_logout = (Button)view.findViewById(R.id.logout);

        //현재 날짜 출력하기
        m = getMonth();
        dd = getDay();
        txt_work.setText("0");



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);*/
                hi.setText(MyInformationActivity.nickName);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.requestLogout(new LogoutResponseCallback() {

                    @Override

                    public void onCompleteLogout() {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(),MyInformationActivity.nickName+"님 로그아웃 되었습니다." , Toast.LENGTH_SHORT).show();
                        hi.setText("로그인 후 확인 가능합니다.");

                    }

                });
            }
        });





        //체크한 갯수구하기

        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(),DBHelper.class);
            Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

            check_count = memoDao.queryBuilder().where().eq("isSelected", true).countOf();
            important_count = memoDao.queryBuilder().where().eq("isClicked", true).countOf();
            Log.i("Check", "--------------------------------------" +check_count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //전체 할 일
        txt_total.setText(Integer.toString(MemoAdapter.count));

        //완료한 할 일
        txt_finish.setText(Long.toString(check_count));
        //중요한 일
        txt_work.setText(Long.toString(important_count));
        ArrayList<Settingitem> data=new ArrayList<>();
        Settingitem a=new Settingitem("완료·중요 목록");
        Settingitem b=new Settingitem("관심품목 목록");
        Settingitem c=new Settingitem("알림설정");
        Settingitem d=new Settingitem("사용한 라이센스");
        //Settingitem e=new Settingitem("참고한 API");

        data.add(a);
        data.add(b);
        data.add(c);
        data.add(d);
        //data.add(e);

        //오늘 할일 갯수
        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(),DBHelper.class);
            Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

            today = memoDao.queryBuilder().where().eq("day", dd).and().eq("month",m).countOf();
            Log.i("Check", "--------------------------------------" +today);

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        txt_todaywork.setText(Long.toString(today));

        SettingAdapter adapter=new SettingAdapter(getActivity(),R.layout.settingitem,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent0 = new Intent(getActivity(), SettingOptionActivity3.class);
                        startActivity(intent0);
                        break;

                    case 1:
                        Intent intent1 = new Intent(getActivity(), SettingOptionActivity2.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(getActivity(),SettingOptionActivity1.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3 = new Intent(getActivity(),SettingOptionActivity4.class);
                        startActivity(intent3);
                        break;

                }
            }
        });

        return view;
    }

    //현재 월 가져오기
    private String getMonth(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mmonth.format(mDate);
    }

    //현재 일 가져오기
    private String getDay(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mday.format(mDate);
    }
}
