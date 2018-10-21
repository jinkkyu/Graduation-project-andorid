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

public class AddAtivity extends AppCompatActivity {

    //전역변수 선언
    private TextView txtYear, txtMonth, txtDay;
    static EditText edtTitle,edtContent;
    TimePicker timePicker;
    TextView info;
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
    String y;
    String m;
    String d;

    int year, month, day, hour, minute;

    public static String select_item="";
    public static String select_year="";
    public static String select_month="";
    public static String select_day="";
    public String title, content, month1, day1, helper;

    //putlbic String mon, ddy;

    public String up_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addactivity_add);



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

        timePicker=(TimePicker) findViewById(R.id.timepicker);
        info=findViewById(R.id.info);

        //현재 날짜 출력하기
        y = getYear();
        m = getMonth();
        d = getDay();
        txt_Month.setText(m);
        txt_day.setText(d);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddAtivity.this, dateSetListener, year, month, day).show();

            }
        });

        String mon = txt_Month.getText().toString();
        String ddy = txt_day.getText().toString();

    }


    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addactivity_menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //시간설정하기
        Calendar current = Calendar.getInstance(); // 현재시간

        Calendar cal = Calendar.getInstance(); //내가 설정한 시간


                if (id == R.id.save) {

                    if (select_month == null && select_day == null) {
                        Toast.makeText(getApplicationContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                    } else {


                        cal.set(

                                //캘린더뷰에서 선택한 날짜 가져오기
                                Integer.parseInt(select_year),
                                Integer.parseInt(select_month) - 1,
                                Integer.parseInt(select_day),
                                //타임피커에서 설정한 시간 가져오기
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute(), 00);
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
                                "설정한시간" + select_year + "년" +
                                        select_month + "월" +
                                        select_day + "일",
                                Toast.LENGTH_LONG).show();

                        //DB저장
                        try {
                            saveToList(new Memo(select_month, select_day,
                                    edtTitle.getText().toString(), edtContent.getText().toString(), select_item, false, false));
                            refreshList();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Intent intent1 = new Intent(AddAtivity.this, MainActivity.class);
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

    //현재 년 가져오기
    private String getYear(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return myear.format(mDate);
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


    //저장하기
    public void saveToList(Memo memo) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this,DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        memoDao.create(memo);
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
            Toast.makeText(AddAtivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };



}

