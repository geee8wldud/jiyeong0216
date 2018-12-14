package com.example.r_intern_01.mymovie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class callNaverAPI {
    String moviename;
    MyAdapter myAdapter;
    String movies="";
    int createAlert;
    Context context;

    public callNaverAPI(String moviename, MyAdapter myAdapter, int createAlert,  Context context){
        this.moviename=moviename;
        this.myAdapter=myAdapter;
        this.createAlert=createAlert;
        this.context=context;
    }

    public void getList(){


        new Thread(new Runnable() {
            @Override
            public void run() {
               callAPI();
            }
        }).start();



    }


    public void callAPI(){

        String clientId = "fBILn04jqzM84eHEdsga";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "tKKYIrvaDC";//애플리케이션 클라이언트 시크릿값";



        try {
            String text = URLEncoder.encode(moviename, "UTF-8");
            Log.d("error1","영화 이름"+moviename);
            String apiURL = "https://openapi.naver.com/v1/search/movie?query="+ text; // json 결과
            Log.d("error1","url"+apiURL);

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);


            int responseCode = con.getResponseCode();

            Log.d("error1", "responseCode "+responseCode);

            BufferedReader br;
            if(responseCode==200) { // 정상 호출

                br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            } else {  // 에러 발생
                Log.d("error1", "responseCode "+responseCode);
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            //System.out.println(response.toString());

            movies=response.toString();
            Log.d("error1",movies);
            //getList(movies);

        } catch (Exception e) {
            //System.out.println(e);
            Log.d("error1", e.toString());
        }


        try {
            //Log.d("error1", "실행됨 2");
            JSONObject jsonObject=new JSONObject(movies);
            //Log.d("error1", jsonObject.toString());

            JSONArray item=jsonObject.getJSONArray("items");
            //Log.d("error1", item.toString());

            if(item.length()<=0){
                createAlert=1;
                Log.d("error1", ""+createAlert);
                ((MainActivity)MainActivity.mcontext).callAlert(createAlert);


            }

                for(int i=0;i<item.length();i++){
                    JSONObject itemInfo = item.getJSONObject(i);
                    //Log.d("error1", itemInfo.toString());

                    String image=itemInfo.getString("image");
                    String title=itemInfo.getString("title");
                    String stars=itemInfo.getString("userRating");
                    String director = itemInfo.getString("director");
                    String actor = itemInfo.getString("actor");
                    String year=itemInfo.getString("pubDate");
                    String link=itemInfo.getString("link");


                    title=title.replaceAll("<b>", "");
                    title=title.replaceAll("<b/>","");
                    title=title.replaceAll("</b>","");


                    Log.d("error1", director);
                    Log.d("error1", actor);
                    Log.d("error1", link);

                    myAdapter.addMovie(image, title, stars, year, director, actor, link);


                   // myAdapter.notifyDataSetChanged();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
