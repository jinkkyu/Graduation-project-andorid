package com.example.paparazzi.paparazzi_planer;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by LeeJinKyu on 2018-04-12.
 */

public class UpdateActivity extends AppCompatActivity {
    //DB ID값
    private int memoid;


    static EditText edtTitle,edtContent;
    TimePicker timePicker;
    TextView info;

    //푸시알림
    final static int RQS_1 = 1;
    private List<Memo> datas = new ArrayList<>();
    MemoAdapter adapter;
    @BindView(R.id.txt_Month) //날짜 월 출력
            TextView txt_Month;
    @BindView(R.id.txt_day) //날짜 일 출력
            TextView txt_day;
    @BindView(R.id.Select_Date)
    LinearLayout select_date;

    long mNow;
    Date mDate;
    SimpleDateFormat mmonth = new SimpleDateFormat("M");
    SimpleDateFormat mday = new SimpleDateFormat("dd");
    SimpleDateFormat myear = new SimpleDateFormat("yyyy");
    ArrayList arraylist;

    int year, month, day;

    public static String select_item="";
    public static String select_year="";
    public static String select_month="";
    public static String select_day="";
    public String title, content, month1, day1, helper;

    public String up_title, up_content, up_month, up_day, up_helper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addactivity_add);


        //Memo Adapter 인텐트값 가져오기
        Intent intent = getIntent();

        memoid = intent.getExtras().getInt("id");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        month1 = intent.getExtras().getString("month");
        day1 = intent.getExtras().getString("day");
        helper = intent.getExtras().getString("helper");



        ButterKnife.bind(this);
        //actionBar 객체를 가져옴
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //메뉴바에 '<' 버튼이 생긴다
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        GregorianCalendar calendar = new GregorianCalendar();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);


        //스피너 아이템추가
        arraylist = new ArrayList();
        arraylist.add("구매하기");
        arraylist.add("찾아가기");
        arraylist.add("알아보기");
        arraylist.add("예약하기");
        arraylist.add("전화하기");
        arraylist.add("문자하기");
        arraylist.add("영화보기");
        arraylist.add("송금하기");

        // 스피너 속성과 선택값 얻어오기
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        Spinner sp = (Spinner)findViewById(R.id.add_spinner);
        sp.setPrompt("Helper를 선택하세요");
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(), arraylist.get(arg2)+"이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                select_item = String.valueOf(arraylist.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub

            }
        });


        edtTitle = (EditText) findViewById(R.id.add_edtTitle);
        edtContent = (EditText) findViewById(R.id.add_edtContent);

        //DB에 저장된 값 가져오기
        edtTitle.setText(title);
        edtContent.setText(content);
        txt_Month.setText(month1);
        txt_day.setText(day1);
        timePicker=(TimePicker) findViewById(R.id.timepicker);
        info=findViewById(R.id.info);



        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateActivity.this, dateSetListener, year, month, day).show();

            }
        });


    }




    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addactivity_menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //시간설정하기
        Calendar current = Calendar.getInstance(); // 현재시간

        Calendar cal = Calendar.getInstance(); //내가 설정한 시간




                Toast.makeText(getApplicationContext(),"날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                if (id == R.id.save) {

                    if (select_month == null && select_day == null){

                    }else{


                        //DB수정
                        try {
                            up_title = edtTitle.getText().toString();
                            up_content = edtContent.getText().toString();
                            up_month = txt_Month.getText().toString();
                            up_day = txt_day.getText().toString();
                            up_helper = select_item;
                            update(new Memo());
                            refreshList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        cal.set(

                                //캘린더뷰에서 선택한 날짜 가져오기
                                Integer.parseInt(select_year),
                                Integer.parseInt(select_month)-1,
                                Integer.parseInt(select_day),
                                //타임피커에서 설정한 시간 가져오기
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute(),00);

                        //캘린더뷰에서 선택한 날짜 가져오기
                        Intent intent1 = new Intent(UpdateActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }


                    //현재시간 보다 낮으면 Toast창 출력
                    if (cal.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getApplicationContext(),
                                "시간을 다시 설정해주세요.",
                                Toast.LENGTH_LONG).show();

                    } else {
                        setAlarm(cal);
                        Toast.makeText(getApplicationContext(),
                                "설정한시간"+select_year+"년"+
                                        select_month+"월"+
                                        select_day+"일",
                                Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(UpdateActivity.this,MainActivity.class);
                        startActivity(intent1);
                    }

                }




        return super.onOptionsItemSelected(item);
    }




    //시간 설정 함수
    private void setAlarm(Calendar targetCal) {
        info.setText("\n\n***\n"
                + "설정된 시간은 : " + targetCal.getTime() + "\n"
                + "***\n");

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }



    //수정하기
    private void update(Memo memo) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

        // 1. 수정할 데이터를 먼저 불러와서
        Memo memoTemp = memoDao.queryForId(memoid);
        // 2. 변경한 후
        memoTemp.setTitle(up_title);
        memoTemp.setMemo(up_content);
        memoTemp.setMonth(up_month);
        memoTemp.setDay(up_day);
        memoTemp.setHelper(up_helper);

        // 3. 최종적으로 update를 통해 수정처리
        memoDao.update(memoTemp);
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

    //데이터피커 다이얼로그
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            select_year = String.format("%d",(year));
            select_month = String.format("%d",(monthOfYear+1));
            select_day = String.format("%d",(dayOfMonth));

            txt_Month.setText(select_month);
            txt_day.setText(select_day);

            String msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            Toast.makeText(UpdateActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };



}