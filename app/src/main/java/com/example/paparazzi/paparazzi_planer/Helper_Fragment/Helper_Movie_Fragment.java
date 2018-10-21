package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.paparazzi.paparazzi_planer.R;
import com.example.paparazzi.paparazzi_planer.ViewActivity;

public class Helper_Movie_Fragment extends Fragment {

    WebView webview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.webview,container,false);


        webview = view.findViewById(R.id.webview);
        webview.loadUrl("https://play.google.com/store/search?q="+
                ViewActivity.item_title.getText().toString()+"&c=movies");
        // Enable Javascript
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webview.setWebViewClient(new WebViewClient());

        return view;

    }
}