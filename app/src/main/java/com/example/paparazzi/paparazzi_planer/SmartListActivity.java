package com.example.paparazzi.paparazzi_planer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LeeJinKyu on 2018-05-27.
 */

public class SmartListActivity extends AppCompatActivity{


    @BindView(R.id.list_count)
    TextView list_count;
    @BindView(R.id.list_layout)
    LinearLayout list_layout;
    Long sm1, sm2, sm3, sm4, sm5, sm6, sm7, sm8;
    TextView list_count1,list_count2,list_count3,list_count4,list_count5,list_count6,list_count7,list_count8;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_list);
        //actionBar 객체를 가져옴
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        ButterKnife.bind(this);

        list_count1 = (TextView) findViewById(R.id.list_count1);
        list_count2 = (TextView) findViewById(R.id.list_count2);
        list_count3 = (TextView) findViewById(R.id.list_count3);
        list_count4 = (TextView) findViewById(R.id.list_count4);
        list_count5 = (TextView) findViewById(R.id.list_count5);
        list_count6 = (TextView) findViewById(R.id.list_count6);
        list_count7 = (TextView) findViewById(R.id.list_count7);
        list_count8 = (TextView) findViewById(R.id.list_count8);
        LinearLayout list_layout2 = (LinearLayout) findViewById(R.id.list_layout2);

        LinearLayout sm_layout1 = (LinearLayout) findViewById(R.id.sm_layout1);
        LinearLayout sm_layout2 = (LinearLayout) findViewById(R.id.sm_layout2);
        LinearLayout sm_layout3 = (LinearLayout) findViewById(R.id.sm_layout3);
        LinearLayout sm_layout4 = (LinearLayout) findViewById(R.id.sm_layout4);
        LinearLayout sm_layout5 = (LinearLayout) findViewById(R.id.sm_layout5);
        LinearLayout sm_layout6 = (LinearLayout) findViewById(R.id.sm_layout6);
        LinearLayout sm_layout7 = (LinearLayout) findViewById(R.id.sm_layout7);
        LinearLayout sm_layout8 = (LinearLayout) findViewById(R.id.sm_layout8);





        //체크한 갯수구하기

        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
            Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

            sm1 = memoDao.queryBuilder().where().eq("helper", "구매하기").countOf();
            sm2 = memoDao.queryBuilder().where().eq("helper", "찾아가기").countOf();
            sm3 = memoDao.queryBuilder().where().eq("helper", "예약하기").countOf();
            sm4 = memoDao.queryBuilder().where().eq("helper", "전화하기").countOf();
            sm5 = memoDao.queryBuilder().where().eq("helper", "문자하기").countOf();
            sm6 = memoDao.queryBuilder().where().eq("helper", "영화보기").countOf();
            sm7 = memoDao.queryBuilder().where().eq("helper", "송금하기").countOf();
            sm8 = memoDao.queryBuilder().where().eq("helper", "알아보기").countOf();
            Log.i("Check", "--------------------------------------" +sm6);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //구매


        if (sm1!=null){
            sm_layout1.setVisibility(View.VISIBLE);
            if (sm1==null){
                sm_layout1.setVisibility(View.INVISIBLE);
            }
        }
        if(sm2!=null){
            sm_layout2.setVisibility(View.VISIBLE);
            if (sm2==null){
                sm_layout2.setVisibility(View.INVISIBLE);
            }
        }

        if(sm3!=null){
            sm_layout3.setVisibility(View.VISIBLE);
            if (sm3==null){
                sm_layout3.setVisibility(View.INVISIBLE);
            }
        }
        if(sm4!=null){
            sm_layout4.setVisibility(View.VISIBLE);
            if (sm4==null){
                sm_layout4.setVisibility(View.INVISIBLE);
            }
        }
        if(sm5!=null){
            sm_layout5.setVisibility(View.VISIBLE);
            if (sm5==null){
                sm_layout5.setVisibility(View.INVISIBLE);
            }
        }

        if(sm6!=null){
            sm_layout6.setVisibility(View.VISIBLE);

        }else if (sm6==0){
            sm_layout6.setVisibility(View.GONE);
            return;
        }

        if(sm7!=null){
            sm_layout7.setVisibility(View.VISIBLE);
            if (sm7==null){
                sm_layout7.setVisibility(View.INVISIBLE);
            }
        }
        if(sm8!=null){
            sm_layout8.setVisibility(View.VISIBLE);
             if (sm8==null){
                sm_layout8.setVisibility(View.INVISIBLE);
            }
        }

        //할일
       list_count.setText(Integer.toString(MemoAdapter.count));
        //구매
        list_count1.setText(Long.toString(sm1));
        //예약
        list_count2.setText(Long.toString(sm3));
        //문자
        list_count3.setText(Long.toString(sm5));
        //전화
        list_count4.setText(Long.toString(sm4));
        //영화
        list_count5.setText(Long.toString(sm6));
        //송금
        list_count6.setText(Long.toString(sm7));
        //알아보기
        list_count7.setText(Long.toString(sm8));
        //찾아가기
        list_count8.setText(Long.toString(sm2));



       list_layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(SmartListActivity.this,MainActivity.class);
               startActivity(intent);
           }
       });

        list_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmartListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }


}
