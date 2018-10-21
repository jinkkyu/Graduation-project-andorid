package com.example.paparazzi.paparazzi_planer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.Helper_Find_Fragment;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.Helper_Call_Fragment;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.Helper_Movie_Fragment;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.Helper_Search_Fragment;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.example.paparazzi.paparazzi_planer.Naver.NaverActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeJinKyu on 2018-05-16.
 */

public class ViewActivity extends AppCompatActivity {

    public Memo memo= new Memo();
    public MemoAdapter memoAdapter;
    public static TextView item_title, item_content, item_month, item_day, item_helper;
    private List<Memo> datas = new ArrayList<>();
    DBHelper dbHelper;
    MemoAdapter adapter;
    private android.support.v4.app.Fragment fragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    int memoid;
    String title, content, month, day, helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helperactivity_item);
        //actionBar 객체를 가져옴
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //DB값 받아오기
        Intent intent = getIntent();

        memoid = intent.getExtras().getInt("id");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        month = intent.getExtras().getString("month");
        day = intent.getExtras().getString("day");
        helper = intent.getExtras().getString("helper");

        item_title = (TextView)findViewById(R.id.item_Title);
        item_content = (TextView)findViewById(R.id.item_content);
        item_helper = (TextView)findViewById(R.id.item_Helper);
        item_month = (TextView)findViewById(R.id.item_month);
        item_day = (TextView)findViewById(R.id.item_day);

        item_title.setText(title);
        item_content.setText(content);
        item_helper.setText(helper);
        item_month.setText(month);
        item_day.setText(day);



        ImageView imageView = (ImageView) findViewById(R.id.img_helper);

        switch (item_helper.getText().toString()){
            case "구매하기" :
                Toast.makeText(getApplicationContext(),"구매하기",Toast.LENGTH_SHORT).show();
                Fragment fr ;
                fr = new NaverActivity();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.helper_container,fr);
                fragmentTransaction.commit();

                break;
            case "알아보기" :
                Toast.makeText(getApplicationContext(),"알아보기",Toast.LENGTH_SHORT).show();
                Fragment fr3 ;
                fr3 = new Helper_Find_Fragment();
                FragmentManager fm3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3 = fm3.beginTransaction();
                fragmentTransaction3.replace(R.id.helper_container,fr3);
                fragmentTransaction3.commit();
                break;
            case "찾아가기" :
                Toast.makeText(getApplicationContext(),"찾아가기",Toast.LENGTH_SHORT).show();
                Fragment fr6 ;
                fr6 = new Helper_Search_Fragment();
                FragmentManager fm6 = getFragmentManager();
                FragmentTransaction fragmentTransaction6 = fm6.beginTransaction();
                fragmentTransaction6.replace(R.id.helper_container,fr6);
                fragmentTransaction6.commit();
                break;
            case "예약하기" :
                Toast.makeText(getApplicationContext(),"예약하기",Toast.LENGTH_SHORT).show();
                Fragment fr7 ;
                fr7 = new NaverActivity();
                FragmentManager fm7 = getFragmentManager();
                FragmentTransaction fragmentTransaction7 = fm7.beginTransaction();
                fragmentTransaction7.replace(R.id.helper_container,fr7);
                fragmentTransaction7.commit();
                break;
            case "전화하기" :
                Toast.makeText(getApplicationContext(),"전화하기",Toast.LENGTH_SHORT).show();
                Fragment fr1 ;
                fr1 = new Helper_Call_Fragment();
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fm1.beginTransaction();
                fragmentTransaction1.replace(R.id.helper_container,fr1);
                fragmentTransaction1.commit();

                break;
            case "문자하기" :
                Toast.makeText(getApplicationContext(),"문자하기",Toast.LENGTH_SHORT).show();
                Fragment fr2 ;
                fr2 = new Helper_Call_Fragment();
                FragmentManager fm2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fm2.beginTransaction();
                fragmentTransaction2.replace(R.id.helper_container,fr2);
                fragmentTransaction2.commit();
                break;
            case "영화보기" :
                Toast.makeText(getApplicationContext(),"영화보기",Toast.LENGTH_SHORT).show();
                Fragment fr5 ;
                fr5 = new Helper_Movie_Fragment();
                FragmentManager fm5 = getFragmentManager();
                FragmentTransaction fragmentTransaction5 = fm5.beginTransaction();
                fragmentTransaction5.replace(R.id.helper_container,fr5);
                fragmentTransaction5.commit();

                break;
            case "송금하기" :
                Toast.makeText(getApplicationContext(),"송금하기",Toast.LENGTH_SHORT).show();
                break;

        }


    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.helperactivity_menu, menu);

        return true;

    }



    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.update) {

            Intent intent = new Intent(this, UpdateActivity.class);
            intent.putExtra("id",memoid);
            intent.putExtra("title",title);
            intent.putExtra("content",content);
            intent.putExtra("helper",helper);
            intent.putExtra("month",month);
            intent.putExtra("day",day);
            startActivity(intent);
            return true;
        }else if (id == R.id.delete){


                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ViewActivity.this);
                alert_confirm.setMessage("정말 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                //DB삭제
                                try {

                                    delete(new Memo());
                                refreshList();
                                Intent delete = new Intent(ViewActivity.this,MainActivity.class);
                                startActivity(delete);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        }).setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();


        } else if (id == R.id.check){

                //체크추가
                try {
                    checktrue(new Memo());
                    Toast.makeText(ViewActivity.this, "할일완료", Toast.LENGTH_SHORT).show();
                    Intent check = new Intent(ViewActivity.this, MainActivity.class);
                    startActivity(check);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
           else if (id == R.id.important){

                //중요한일 추가
                try {
                    importanttrue(new Memo());
                    Toast.makeText(ViewActivity.this, "중요한일 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent check1 = new Intent(ViewActivity.this, MainActivity.class);
                    startActivity(check1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }


        return super.onOptionsItemSelected(item);
    }

    //할일완료 체크
    private void checktrue(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 수정할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);
        // 2. 변경한 후
        memoTemp.setSelected(true);

        // 3. 최종적으로 update를 통해 수정처리
        memoDao.update(memoTemp);
        loadData();
        refreshList();
    }

    //할일완료 체크해제
    private void checkfalse(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 수정할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);
        // 2. 변경한 후
        memoTemp.setSelected(false);

        // 3. 최종적으로 update를 통해 수정처리
        memoDao.update(memoTemp);
        loadData();
        refreshList();
    }

    //중요한일 체크
    private void importanttrue(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 수정할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);
        // 2. 변경한 후
        memoTemp.setClicked(true);

        // 3. 최종적으로 update를 통해 수정처리
        memoDao.update(memoTemp);
        loadData();
        refreshList();
    }

    //중요한일 체크
    private void importantfalse(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 수정할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);
        // 2. 변경한 후
        memoTemp.setClicked(false);

        // 3. 최종적으로 update를 통해 수정처리
        memoDao.update(memoTemp);
        loadData();
        refreshList();
    }

    //삭제하기
    private void delete(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 삭제할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);

        // 2. 최종적으로 delte를 통해 삭제처리
        memoDao.delete(memoTemp);
        loadData();
        refreshList();
    }


    public void loadData() throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        datas = memoDao.queryForAll();
    }

    private void refreshList() {
        adapter = new MemoAdapter(getApplicationContext(),datas);
        TodayFragment.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
