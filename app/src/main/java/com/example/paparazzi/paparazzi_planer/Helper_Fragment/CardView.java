package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.paparazzi.paparazzi_planer.R;

public class CardView extends AppCompatActivity {

    TextView textName, textNumber;
    ImageButton BtnCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_call_listitem);

        textName = (TextView) findViewById(R.id.textName);
        textNumber = (TextView) findViewById(R.id.textNumber);
        BtnCall = (ImageButton) findViewById(R.id.BtnCall);

    }
}
