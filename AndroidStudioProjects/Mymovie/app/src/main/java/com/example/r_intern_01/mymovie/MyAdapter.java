package com.example.r_intern_01.mymovie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<movieVO> movieLIsts=new ArrayList<>();

    @Override
    public int getCount() {
        return movieLIsts.size();
    }

    @Override
    public movieVO getItem(int position) {
        return movieLIsts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ArrayList<movieVO> getMovieList(){
        return movieLIsts;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView imageview = (ImageView) convertView.findViewById(R.id.imageview) ;
        TextView title = (TextView) convertView.findViewById(R.id.title) ;
        ImageView stars = (ImageView) convertView.findViewById(R.id.stars) ;
        TextView year=(TextView) convertView.findViewById(R.id.year);
        TextView director=(TextView) convertView.findViewById(R.id.director);
        TextView actor =(TextView) convertView.findViewById(R.id.actor);


        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        movieVO movie = getItem(position);
        final String imageUrl=movie.getImage();
        final Bitmap[] bitmap = new Bitmap[1];
        Thread th=new Thread(){
            public void run(){
                try {
                    URL url=new URL(imageUrl);

                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is=conn.getInputStream();
                    bitmap[0] =BitmapFactory.decodeStream(is);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        th.start();
        try {
            th.join();
            imageview.setImageBitmap(bitmap[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //Log.d("error1",movie.getStars());
        double numstar=Double.parseDouble(movie.getStars());
       // Log.d("error1", numstar+"");
        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        title.setText(movie.getTitle());
     // stars.setText(movie.getStars());
        if(numstar<=1){
            stars.setImageResource(R.drawable.star0_5);
        }else if(numstar<=2){
            stars.setImageResource(R.drawable.star1);
        }else if(numstar<=3){
            stars.setImageResource(R.drawable.star1_5);
        }else if(numstar<=4){
            stars.setImageResource(R.drawable.star2);
        }else if(numstar<=5){
            stars.setImageResource(R.drawable.star2_5);
        }else if(numstar<=6){
            stars.setImageResource(R.drawable.star3);
        }else if(numstar<=7){
            stars.setImageResource(R.drawable.star3_5);
        }else if(numstar<=8){
            stars.setImageResource(R.drawable.star4);
        }else if(numstar<=9){
            stars.setImageResource(R.drawable.star4_5);
        }else if(numstar<=10){
            stars.setImageResource(R.drawable.star5);
        }

        year.setText(movie.getYear());
        director.setText(movie.getDirector());
        actor.setText(movie.getActor());


        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

    public void addMovie(String image, String title, String stars, String year, String director, String actor, String link){
        movieVO movie=new movieVO();
        movie.setImage(image);
        movie.setTitle(title);
        movie.setStars(stars);
        movie.setYear(year);
        movie.setActor(actor);
        movie.setDirector(director);
        movie.setLink(link);

        movieLIsts.add(movie);


    }

    public void clearList(){
        movieLIsts.clear();
    }


}
