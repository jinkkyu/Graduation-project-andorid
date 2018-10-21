package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.paparazzi.paparazzi_planer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeJinKyu on 2018-05-21.
 */

public class Helper_Call_Fragment extends android.app.Fragment {
    private List<Contact> search = new ArrayList<>();
    public Helper_Call_Fragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.helper_call_fragment,container,false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        DataLoader loader = new DataLoader(getActivity());
        loader.load();
        ArrayList<Contact> datas = loader.get();

        RecyclerAdapter rca = new RecyclerAdapter(datas, getActivity());
        recyclerView.setAdapter(rca);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //버전체크해서 마시멜로우 보다 낮으면 런타임 권한 체크를 하지 않는다
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            checkPermission();
        }else {
            loadData();
        }
        return view;
    }

    private final int REQ_CODE = 100;
    //1. 권한 체크
    @TargetApi(Build.VERSION_CODES.M) //target 지정 annotation
    private void checkPermission() {
        // 1.1 런타임 권한 체크
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS};
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
                //loadData();

            } else {
                Toast.makeText(getActivity(), "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                //선택 1 . 종료  / 2. 권한체크 다시 물어보기
            }
        }
    }

    //3. data 읽어오기 (시스템 실행)
    private void loadData() {
        Toast.makeText(getActivity(), "프로그램을 실행합니다.", Toast.LENGTH_SHORT).show();


    }


}

