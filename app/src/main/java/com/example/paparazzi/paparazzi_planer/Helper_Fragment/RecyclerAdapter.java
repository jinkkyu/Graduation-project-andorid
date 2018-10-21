package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.paparazzi.paparazzi_planer.R;
import com.example.paparazzi.paparazzi_planer.ViewActivity;

import java.util.ArrayList;

/**.
 * 1. 홀더에 사용하는 위젯을 모두 정의한다
 * 2. get itemcount에 데이터 개수 전달
 * 3. onCreateViewHolder에서 뷰 아이템 생성
 * 4.onBindViewHolder를 통해 로직을 구현
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder>{

    ArrayList<Contact> datas;
    Context context;

    public RecyclerAdapter(ArrayList<Contact> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_call_listitem, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //1. data를 행 단위로 꺼낸다
        final Contact contact = datas.get(position);
        //2. 홀더에 데이터를 세팅한다.
        holder.textName.setText(contact.getName());
        holder.textNumber.setText(contact.getNumberOne());
        //3. 액션을 정의한다. 리스너 종류를 세팅한다.


        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.cardView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {

        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textName, textNumber;
        ImageButton BtnCall;
        ImageButton Btnletter;
        CardView cardView;

        public Holder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textNumber = (TextView) itemView.findViewById(R.id.textNumber);
            BtnCall = (ImageButton) itemView.findViewById(R.id.BtnCall);
            Btnletter = (ImageButton) itemView.findViewById(R.id.Btnletter);
            BtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if( context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                || context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textNumber.getText().toString()));
                            context.startActivity(intent);
                        }
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textNumber.getText().toString()));
                        context.startActivity(intent);
                    }
                }
            });

            Btnletter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
