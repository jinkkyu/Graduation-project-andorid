package com.example.paparazzi.paparazzi_planer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.example.paparazzi.paparazzi_planer.Naver.NaverActivity;
import com.github.clans.fab.FloatingActionMenu;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by LeeJinKyu on 2018-04-12.
 */

public class TodayFragment extends android.support.v4.app.Fragment {

    public TodayFragment(){
    }

    // 카메라 요청 코드
    private final int REQ_CAMERA = 101;
    // 겔러리 요청 코드
    private final int REQ_GALLERY = 102;
    // 권한 요청 코드
    private final int REQ_PERMISSION = 100;

    private static final int REQUEST_WRITE = 1000;
    public static long check_count;
    private List<Memo> datas = new ArrayList<>();
    private List<Memo> search = new ArrayList<>();
    static RecyclerView recyclerView;
    MemoAdapter adapter;
    AlertDialog dialog;
    Uri fileUri = null;
    FloatingActionMenu quickFloatingMenu;
    com.github.clans.fab.FloatingActionButton FloatBtnQuick_memo,FloatBtnQuick_photo,FloatBtnQuick_voice,FloatBtnQuick_camera;
    EditText et_Search;
    ArrayList arraylist;
    String select_item;


    //오늘날짜가져오기
    SimpleDateFormat mmonth = new SimpleDateFormat("M");
    SimpleDateFormat mday = new SimpleDateFormat("dd");
    long mNow;
    Date mDate;
    String m,dd;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.today_fragment,container,false);
        // 권한 처리
        quickFloatingMenu = (FloatingActionMenu) view.findViewById(R.id.quick_floating_action_menu);
        FloatBtnQuick_memo = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.quick_floating_action_menu_add);
        FloatBtnQuick_photo = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.quick_floating_action_menu_photo);
        FloatBtnQuick_voice = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.quick_floating_action_menu_voice);
        FloatBtnQuick_camera = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.quick_floating_action_menu_camera);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //정렬

        //스피너 아이템추가
        arraylist = new ArrayList();
        arraylist.add("날짜순 정렬");
        //arraylist.add("등록순 정렬");
        arraylist.add("헬퍼순 정렬");
        arraylist.add("오늘의 할 일");

        //현재 날짜 출력하기
        m = getMonth();
        dd = getDay();


        // 스피너 속성과 선택값 얻어오기
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, arraylist);
        Spinner sp = (Spinner)view.findViewById(R.id.spinner_sort);
        sp.setPrompt("Helper를 선택하세요");
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                select_item = String.valueOf(arraylist.get(arg2));

                switch (select_item){

                    case "등록순 정렬" :
                        // 리스트를 갱신한다.
                        try {
                            read();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "날짜순 정렬" :
                        // 리스트를 갱신한다.
                        try {
                            datesort();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "헬퍼순 정렬" :
                        // 리스트를 갱신한다.
                        try {
                            worksort();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "오늘의 할 일" :
                        // 리스트를 갱신한다.
                        try {
                            today();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {
                // TODO Auto-generated method stub

            }
        });

        //DB검색

        et_Search = (EditText) view.findViewById(R.id.et_search);

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")) {
                    try {
                        read();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    searchDate(editable.toString());
                }
            }
        });

        // 목록에서 항목을 왼쪽, 오른쪽 방향으로 스와이프 하는 항목을 처리
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // 사용하지 않는다.
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 스와이프된 아이템의 아이디를 가져온다.
                final int position = viewHolder.getAdapterPosition();
                // DB 에서 해당 아이디를 가진 레코드를 삭제한다.
                try {
                    try {
                        delete(position);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } catch(IndexOutOfBoundsException ex) {

                    ex.printStackTrace();

                }
            }
        }).attachToRecyclerView(recyclerView);




        //메모하기버튼
        FloatBtnQuick_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickFloatingMenu.close(true);
                Intent save = new Intent(getActivity(), AddAtivity.class);
                startActivity(save);
            }
        });





        //사진
        FloatBtnQuick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickFloatingMenu.close(true);
                Toast.makeText(getActivity(),"아직 개발중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        //음성
        FloatBtnQuick_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickFloatingMenu.close(true);
                Toast.makeText(getActivity(),"아직 개발중입니다.",Toast.LENGTH_SHORT).show();
            }
        });


        //앨범
        FloatBtnQuick_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickFloatingMenu.close(true);
                Toast.makeText(getActivity(),"아직 개발중입니다.",Toast.LENGTH_SHORT).show();

            }
        });


        /*// 리스트를 불러온다.
        try {
            read();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        return view;

    }


    public void getCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 롤리팝 이상 버전에서는 코드를 반영해야 한다.
        // --- 카메라 촬영 후 미디어 컨텐트 uri 를 생성해서 외부저장소에 저장한다 ---
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }*/
        // --- 여기 까지 컨텐트 uri 강제세팅 ---
        startActivityForResult(intent, REQ_CAMERA);
    }

    public void getPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//외부 저장소에 있는 이미지만 가져오기 위한 필터리
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_GALLERY);
    }


    //DB관련 함수

    //DB 불러오기
    private void read() throws SQLException{
        // 1. DB연결
        DBHelper dbHelper = new DBHelper(getActivity());
        // 2. Table 연결
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        datas = memoDao.queryForAll();
        //datas = memoDao.queryBuilder().where().eq("helper", "구매하기").query();
        adapter = new MemoAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //등록순으로 정렬하기

    private void datesort() throws SQLException{
        // 1. DB연결
        DBHelper dbHelper = new DBHelper(getActivity());
        // 2. Table 연결
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        datas = memoDao.queryBuilder().orderBy("month",true).orderBy("day",true).query();
        //datas = memoDao.queryBuilder().where().eq("helper", "구매하기").query();
        adapter = new MemoAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //오늘의 할일 만 가져오기

    private void today() throws SQLException{
        // 1. DB연결
        DBHelper dbHelper = new DBHelper(getActivity());
        // 2. Table 연결
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        datas = memoDao.queryBuilder().where().eq("month", m).and().eq("day",dd).query();
        //datas = memoDao.queryBuilder().where().eq("helper", "구매하기").query();
        adapter = new MemoAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    //할일 완료한순으로 정렬하기


    private void worksort() throws SQLException{
        // 1. DB연결
        DBHelper dbHelper = new DBHelper(getActivity());
        // 2. Table 연결
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        datas = memoDao.queryBuilder().orderBy("helper",true).query();
        //datas = memoDao.queryBuilder().where().eq("helper", "구매하기").query();
        adapter = new MemoAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //DB 삭제하기

    private void delete(int position) throws SQLException{
        DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(),DBHelper.class);
        Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();
        Memo memo = datas.get(position);
        memoDao.delete(memo);
        adapter.notifyItemRemoved(position); // Adapter에 삭제
        read();


    }


    public void searchDate(String searchText) {
        search.clear();
        for(int i = 0; i<datas.size();i++) {
            if(datas.get(i).getTitle().matches(".*"+searchText+".*")) {
                search.add(datas.get(i));
            }else if(datas.get(i).getMemo().matches(".*"+searchText+".*")){
                search.add(datas.get(i));
            }else if(datas.get(i).getHelper().matches(".*"+searchText+".*")){
                search.add(datas.get(i));
            }
        }
        adapter = new MemoAdapter(getActivity(),search);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
