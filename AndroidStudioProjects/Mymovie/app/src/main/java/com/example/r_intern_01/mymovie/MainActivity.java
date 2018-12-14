package com.example.r_intern_01.mymovie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private Button search_btn;
    private EditText editText;
    private ListView movieList;
    private MyAdapter myAdapter=new MyAdapter();
    private int createAlert=0;
    public static Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcontext=this;


        search_btn=(Button)findViewById(R.id.search_btn);
        editText=(EditText)findViewById(R.id.moviename);
        movieList=(ListView)findViewById(R.id.movieList);

        Log.d("error1", "여기까지 실행됨1");

        final Context context=getApplicationContext();

        search_btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                        ArrayList<movieVO> myList=myAdapter.getMovieList();
                         myList.clear();
                        String moviename=editText.getText().toString();


                        //movieList.setAdapter(myAdapter);

                        callNaverAPI callapi=new callNaverAPI(moviename, myAdapter, createAlert, getApplicationContext());
                        Log.d("error1","실행됨");
                        callapi.getList();




                        movieList.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();

                        onResume();


                        /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);*/


            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String link=myAdapter.getItem(position).getLink();
               Log.d("error1", link);

                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(link);
                i.setData(u);
                startActivity(i);

            }
        });

/*
        Log.d("error1", ""+createAlert);
        if(createAlert==1){
            Toast.makeText(context,"잘못된 검색어입니다.", Toast.LENGTH_LONG);
        }*/

    }
    @Override
    public void onResume() {
        super.onResume();

        myAdapter.notifyDataSetChanged();
        movieList.setAdapter(myAdapter);
        movieList.invalidateViews();
        myAdapter.clearList();


    }

    public void callAlert(int createAlert){
        Log.d("error1","createAlert : "+createAlert);
       // Toast.makeText(this,"잘못된 검색어입니다.",Toast.LENGTH_LONG).show();

       /* if(createAlert==1){
            // Alert을 이용해 종료시키기
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog  .setTitle("잘못된 정보")
                    .setMessage("잘못된 검색어입니다. ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create().show();
        }*/

    }


}
