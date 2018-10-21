package com.example.paparazzi.paparazzi_planer.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.paparazzi.paparazzi_planer.R;

import java.util.ArrayList;

/**
 * Created by LeeJinKyu on 2018-06-04.
 */

public class SettingAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Settingitem> data;
    private int layout;
    public SettingAdapter(Context context, int layout, ArrayList<Settingitem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getName();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Settingitem settingitem=data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.setting_txt);
        name.setText(settingitem.getName());
        return convertView;
    }
}