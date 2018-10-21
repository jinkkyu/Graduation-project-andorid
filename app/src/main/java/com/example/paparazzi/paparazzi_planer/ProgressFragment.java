package com.example.paparazzi.paparazzi_planer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.Memo.Memo;
import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
//import android.widget.TextView;

//import com.example.paparazzi.paparazzi_planer.Memo.MemoAdapter;



/**
 * Created by LeeJinKyu on 2018-04-12.
 */

public class ProgressFragment extends Fragment implements CircleDisplay.SelectionListener{
    public ProgressFragment(){
    }
    private TextView list_size;
    private TextView check_size;
    private CircleDisplay mCircleDisplay;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.progress_fragment,container,false);
        list_size = (TextView)view.findViewById(R.id.list_size);
        check_size = (TextView)view.findViewById(R.id.list_check);
        ImageView img_run = (ImageView)view.findViewById(R.id.img_run);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(img_run);
        Glide.with(this).load(R.drawable.running).into(gifImage);

        //체크한 갯수구하기

        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(),DBHelper.class);
            Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

            Long check_count = memoDao.queryBuilder().where().eq("isSelected", true).countOf();
            Log.i("Check", "--------------------------------------" +check_count);

            int a = MemoAdapter.count;
            Long b = check_count;
            int percent = (int)( (double)b/ (double)a * 100.0 );

            mCircleDisplay = (CircleDisplay) view.findViewById(R.id.circleDisplay);

            mCircleDisplay.setAnimDuration(4000);
            mCircleDisplay.setValueWidthPercent(55f);
            mCircleDisplay.setFormatDigits(1);
            mCircleDisplay.setDimAlpha(80);
            mCircleDisplay.setSelectionListener(this);
            mCircleDisplay.setTouchEnabled(false);
            mCircleDisplay.setUnit("%");
            mCircleDisplay.setStepSize(0.5f);
             mCircleDisplay.showValue(percent, 100f, true);

            list_size.setText(Integer.toString(MemoAdapter.count));
            check_size.setText(Long.toString(check_count));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DBHelper dbHelper = OpenHelperManager.getHelper(getActivity(),DBHelper.class);
            Dao<Memo,Integer> memoDao = dbHelper.getMemoDao();

            Long check_count = memoDao.queryBuilder().where().eq("isSelected", true).countOf();
            Log.i("Check", "--------------------------------------" +check_count);

            int a = MemoAdapter.count;
            Long b = check_count;
            int percent = (int)( (double)b/ (double)a * 100.0 );

            mCircleDisplay = (CircleDisplay) view.findViewById(R.id.circleDisplay);

            mCircleDisplay.setAnimDuration(4000);
            mCircleDisplay.setValueWidthPercent(55f);
            mCircleDisplay.setFormatDigits(1);
            mCircleDisplay.setDimAlpha(80);
            mCircleDisplay.setSelectionListener(this);
            mCircleDisplay.setTouchEnabled(false);
            mCircleDisplay.setUnit("%");
            mCircleDisplay.setStepSize(0.5f);
            mCircleDisplay.showValue(percent, 100f, true);

            list_size.setText(Integer.toString(MemoAdapter.count));
            check_size.setText(Long.toString(check_count));

        } catch (SQLException e) {
            e.printStackTrace();
        }





        return view;
    }

    @Override
    public void onSelectionUpdate(float val, float maxval) {
        Log.i("Main", "Selection update: " + val + ", max: " + maxval);
    }

    @Override
    public void onValueSelected(float val, float maxval) {
        Log.i("Main", "Selection complete: " + val + ", max: " + maxval);
    }
}
