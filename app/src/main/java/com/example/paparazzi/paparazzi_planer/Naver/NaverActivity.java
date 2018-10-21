package com.example.paparazzi.paparazzi_planer.Naver;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.Contact;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.DataLoader;
import com.example.paparazzi.paparazzi_planer.Helper_Fragment.RecyclerAdapter;
import com.example.paparazzi.paparazzi_planer.R;
import com.example.paparazzi.paparazzi_planer.Naver_Adapter.ListTypeAdapter;
import com.example.paparazzi.paparazzi_planer.Naver_Util.CustomLayoutManager;
import com.example.paparazzi.paparazzi_planer.Naver_Util.DBHelper;
import com.example.paparazzi.paparazzi_planer.Naver_Util.Items;
import com.example.paparazzi.paparazzi_planer.Naver_Util.NaverShoppingSearchService;
import com.example.paparazzi.paparazzi_planer.Naver_Util.SearchDataList;
import com.example.paparazzi.paparazzi_planer.ViewActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.paparazzi.paparazzi_planer.Naver_Util.DBHelper.DATABASE_NAME;
import static com.example.paparazzi.paparazzi_planer.Naver_Util.DBHelper.DATABASE_VERSION;

public class NaverActivity extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{


    public static String NAVER_URL = "https://openapi.naver.com/v1/search/";

    TextView textLowPrice;
    ImageButton btnSearch;
    EditText query;
    String queryString;
    List<Items> itemList;
    ProgressBar progressBar;
    RadioButton radioBtnSim, radioBtnPrice;
    RadioGroup radioGroupSort;

    RecyclerView recyclerView;
    ListTypeAdapter listTypeAdapter;
    CustomLayoutManager customLayoutManager;

    SwipeRefreshLayout swipeRefreshLayout;

    SQLiteDatabase database;

    RequestManager requestManager;

    long pressedTime = 0;
    long seconds = 0;
    int lprice;
    int displayValue = 100;
    int startValue = 1;
    String sortType = "sim";
    public NaverActivity(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.naver_main,container,false);

        requestManager = Glide.with(this);

        //시작

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        textLowPrice = (TextView) view.findViewById(R.id.textLowPrice);
        query = (EditText) view.findViewById(R.id.query);
        query.setText(ViewActivity.item_title.getText().toString());
        radioBtnPrice = (RadioButton) view.findViewById(R.id.radioBtnPrice);
        radioBtnSim = (RadioButton) view.findViewById(R.id.radioBtnSim);
        radioBtnSim.setChecked(true);
        radioGroupSort = (RadioGroup) view.findViewById(R.id.radioGroupSort);
        radioGroupSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioBtnSim:
                        if(!query.getText().toString().isEmpty()) {
                            recyclerView.scrollToPosition(0);
                            Toast.makeText(getActivity(), "검색어와 유사한 물품을 검색합니다.", Toast.LENGTH_SHORT).show();
                            sortType = "sim";
                            clearData();
                            setRetrofit(queryString);
                        }
                        break;
                    case R.id.radioBtnPrice:
                        if(!query.getText().toString().isEmpty()) {
                            recyclerView.scrollToPosition(0);
                            Toast.makeText(getActivity(), "최저가 순으로 검색합니다.", Toast.LENGTH_SHORT).show();
                            sortType = "asc";
                            clearData();
                            setRetrofit(queryString);
                        }
                        break;
                }
            }
        });

        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!query.getText().toString().isEmpty()){
                    swipeRefreshLayout.setRefreshing(false);
                    clearData();
                    Toast.makeText(getActivity(), "다시 검색합니다.", Toast.LENGTH_SHORT).show();
                    setRetrofit(queryString);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //query.setOnEditorActionListener(this);
        query.setOnKeyListener(new View.OnKeyListener() {
            //OnKey would be called twice, one for a Down event and another one for an Up event. Please try to add a condition:
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP){
                    goSearch();
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        //끝

        //setSpinner();
        customLayoutManager = new CustomLayoutManager(getActivity());
        itemList = new ArrayList<>();
        listTypeAdapter = new ListTypeAdapter(getActivity(), itemList, requestManager);
        recyclerView.setAdapter(listTypeAdapter);
        recyclerView.setLayoutManager(customLayoutManager);
        //switcher();
        pressEnterkey();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("count", recyclerView.getLayoutManager().getItemCount()+"");
                Log.e("lastVisible", customLayoutManager.findLastVisibleItemPosition()+"");
                int lastVisible = customLayoutManager.findLastVisibleItemPosition();
                /*if(lastVisible +1 == itemList.size()){
                    startValue = startValue + 10;
                    setRetrofit(queryString);
                }*/
                if(lastVisible == itemList.size()-1){
                    startValue = startValue + 100;
                    setRetrofit(queryString);
                }
            }
        });

        return view;
    }

    public void pressEnterkey() {
        //setOnKeyListener가 키보드의 입력을 받음.

    }

    private void clearData(){
        itemList.clear();
        startValue = 1;
        lprice = 2147483647;
    }

    private HttpLoggingInterceptor loggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("okhttp : ", message+"");
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    public void setRetrofit(String queryString){
        progressBar.setVisibility(View.VISIBLE);
        textLowPrice.setVisibility(View.INVISIBLE);

        if(startValue == 1) {
            itemList.clear();
            lprice = 2147483647;
        }

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NAVER_URL).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();

        NaverShoppingSearchService naverShoppingSearchService = retrofit.create(NaverShoppingSearchService.class);

        Observable<SearchDataList> getSearchData = naverShoppingSearchService.getSearchDataList(queryString, displayValue, startValue, sortType);
        getSearchData.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<SearchDataList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchDataList searchDataList) {
                for(Items itemResult : searchDataList.getItems()){
                    itemResult.setTitle(itemResult.getTitle().replace("<b>", ""));
                    itemResult.setTitle(itemResult.getTitle().replace("</b>", ""));
                    if (lprice >= itemResult.getLprice()) {
                        lprice = itemResult.getLprice();
                    }
                    itemList.add(itemResult);

                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("error data", e.getLocalizedMessage()+"");
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                listTypeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                if (itemList.size() == 0) {
                    lprice = 2147483647;
                    progressBar.setVisibility(View.GONE);
                    textLowPrice.setText("검색 결과 없음");
                    Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                if (lprice != 2147483647) {
                    textLowPrice.setText(String.format("%,d", lprice) + "원");
                } else {
                    textLowPrice.setText("검색 결과 없음");
                    Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                textLowPrice.setVisibility(View.VISIBLE);
                Log.e("listItemPositon",
                        customLayoutManager.findLastCompletelyVisibleItemPosition() + "");
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    private boolean networkCheck(){
        boolean connect = true;
        ConnectivityManager connectivityManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            connect = false;
        }
        return connect;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSearch:
                clearData();
                goSearch();
                hideKeyboard();
                break;
        }
    }


    public void goSearch(){
        //검색데이터 가져오기!!
        queryString = query.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        if(queryString.equals("")){
            Toast.makeText(getActivity(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {
            if(networkCheck()) {
                //itemList.clear();
                clearData();
                listTypeAdapter.notifyDataSetChanged();
                setRetrofit(queryString);
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), "인터넷에 연결되어 있지 않아 검색할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //엔터키를 누르고 나면(혹은 상품 검색 버튼을 누르면) InputMethodManage를 통해 키보드를 숨김.
    public void hideKeyboard(){
        InputMethodManager immanager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            immanager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        immanager.hideSoftInputFromWindow(query.getWindowToken(), 0);
    }

   /* //back버튼을 3초 내에 두 번 누르면 종료되도록 함.
    @Override
    public void onBackPressed() {

        if(pressedTime == 0){
            Toast.makeText(getActivity(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            seconds = System.currentTimeMillis() - pressedTime;

            if ( seconds > 3000 ) {
                Toast.makeText(getActivity(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                *//*super.onBackPressed();
                finish();*//*
            }
        }
    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.goFavorite:
                createDatabase();
                Intent favoriteIntent = new Intent(NaverActivity.this, FavoriteActivity.class);
                if(favoriteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(favoriteIntent);
                } else {
                    Toast.makeText(this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.opensource:
                Intent openSourceIntent = new Intent(NaverActivity.this, OpenSourceActivity.class);
                if(openSourceIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openSourceIntent);
                } else {
                    Toast.makeText(this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
        }
        return true;
    }

    public void createDatabase(){
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        DBHelper helper = new DBHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        helper.getWritableDatabase();
    }*/

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(false);
        goSearch();
    }



}


