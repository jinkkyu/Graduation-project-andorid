
package com.example.paparazzi.paparazzi_planer.Memo;


/**
 * Created by LeeJinKyu on 2018-04-20.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.paparazzi.paparazzi_planer.AddAtivity;
import com.example.paparazzi.paparazzi_planer.DB.DBHelper;
import com.example.paparazzi.paparazzi_planer.MainActivity;
import com.example.paparazzi.paparazzi_planer.TodayFragment;
import com.example.paparazzi.paparazzi_planer.UpdateActivity;
import com.example.paparazzi.paparazzi_planer.ViewActivity;
import com.example.paparazzi.paparazzi_planer.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{
    private static final int REQUEST_WRITE = 1000;
    Context context;
    List<Memo> datas;
    View view;
    int memoid;

    public static int count;

    public MemoAdapter(Context context, List<Memo> datas) {
        this.context = context;
        this.datas = datas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;
        public TextView tv_title;
        public TextView list_month;
        public TextView list_day;
        public CardView cardView;
        public ImageView list_img;
        public CheckBox list_check;
        public ImageView img_helper, img_check, img_important;
        public TextView tv_date;
        public View finish1, finish2;
        public int position;
        public ViewHolder(View view) {
            super(view);
            //tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            list_month = (TextView) view.findViewById(R.id.list_month);
            list_day = (TextView) view.findViewById(R.id.list_day);
            list_img = (ImageView) view.findViewById(R.id.list_img);
            list_check = (CheckBox) view.findViewById(R.id.list_check);
            img_helper = (ImageView) view.findViewById(R.id.img_helper);
            finish1 = (View) view.findViewById(R.id.finish1);
            finish2 = (View) view.findViewById(R.id.finish2);
            cardView = (CardView) view.findViewById(R.id.cardView);
            img_check = (ImageView) view.findViewById(R.id.img_check);
            img_important = (ImageView) view. findViewById(R.id.img_important);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view(position);
                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    update(position);

                    return false;
                }
            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_content.setText(datas.get(position).getMemo());
        holder.tv_title.setText(datas.get(position).getTitle());
        for(int i = 0; i< datas.size(); i++) {
            Log.i("MAIN", "0--------------------------------------" + datas.get(i).getMemo());
            Log.i("Check", "0--------------------------------------" + datas.get(i).getSelected());
            Log.i("Check", "0--------------------------------------" + datas.get(i).getClicked());
        }
        holder.position = position;
        holder.list_month.setText(datas.get(position).getMonth());
        holder.list_day.setText(datas.get(position).getDay());
        //holder.tv_date.setText(datas.get(position).getSelected().toString());

        switch (datas.get(position).getSelected().toString()){
            case "true" :
                holder.finish1.setVisibility(View.VISIBLE);
                holder.finish2.setVisibility(View.VISIBLE);
                holder.img_check.setVisibility(View.VISIBLE);


                break;

        }

        switch (datas.get(position).getClicked().toString()){
            case "true" :
                holder.img_important.setVisibility(View.VISIBLE);
                break;

        }

        switch (datas.get(position).getHelper()){
            case "구매하기":
                holder.img_helper.setImageResource(R.drawable.shopping);


                break;

            case "알아보기" :
                holder.img_helper.setImageResource(R.drawable.glass);

                break;

            case "찾아가기" :
                holder.img_helper.setImageResource(R.drawable.google);

                break;
            case "예약하기" :
                holder.img_helper.setImageResource(R.drawable.reserve);

                break;
            case "전화하기" :
                holder.img_helper.setImageResource(R.drawable.phone);

                break;
            case "문자하기" :
                holder.img_helper.setImageResource(R.drawable.email);

                break;
            case "영화보기" :
                holder.img_helper.setImageResource(R.drawable.movie);

                break;
            case "송금하기" :
                holder.img_helper.setImageResource(R.drawable.credit);

                break;

        }


    }

    @Override
    public int getItemCount() {

        count = datas.size();
        return count;

    }







    public void view(int position) {

        Intent intent = new Intent(context, ViewActivity.class);
        intent.putExtra("id",datas.get(position).getId());
        intent.putExtra("title",datas.get(position).getTitle());
        intent.putExtra("content",datas.get(position).getMemo());
        intent.putExtra("helper",datas.get(position).getHelper());
        intent.putExtra("month",datas.get(position).getMonth());
        intent.putExtra("day",datas.get(position).getDay());
        context.startActivity(intent);


    }

    public void update(int position) {

        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("id",datas.get(position).getId());
        intent.putExtra("title", datas.get(position).getTitle());
        intent.putExtra("content", datas.get(position).getMemo());
        intent.putExtra("helper", datas.get(position).getHelper());
        intent.putExtra("month", datas.get(position).getMonth());
        intent.putExtra("day", datas.get(position).getDay());
        context.startActivity(intent);
    }



}

