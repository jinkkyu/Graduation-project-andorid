package com.example.paparazzi.paparazzi_planer.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.example.paparazzi.paparazzi_planer.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeJinKyu on 2018-06-04.
 */

public class SettingOptionActivity3 extends AppCompatActivity {


    private List<Memo> datas = new ArrayList<>();
    RecyclerView recyclerView1, recyclerView2;
    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_option2);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //중요한일
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        try {
            loadData1();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new MemoAdapter(this,datas);
        recyclerView1.setAdapter(adapter);

        //완료한일
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        try {
            loadData2();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new MemoAdapter(this,datas);
        recyclerView2.setAdapter(adapter);

    }




    /*@Override
    public void delete(int position) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        Memo memo = datas.get(position);
        memoDao.delete(memo);
        loadData();
        refreshList();
    }*/





    /*public void saveToList(Memo memo) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        memoDao.create(memo);
        loadData();
        refreshList();
    }*/

    public void loadData1() throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        datas = memoDao.queryBuilder().where().eq("isSelected", true).query();
    }

    public void loadData2() throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        datas = memoDao.queryBuilder().where().eq("isClicked", true).query();
    }

/*    public void updateToLIst(Memo memo) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        memoDao.update(memo);
        loadData();
        refreshList();
        super.onBackPressed();
    }*/

    private void refreshList() {
        adapter = new MemoAdapter(this,datas);
        recyclerView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}