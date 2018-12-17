package com.example.admin.clothes;

//이미지 사용자 정의 자르기

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.admin.clothes.view.HomeFragment;
import com.example.admin.clothes.view.TabActivity;

import static com.example.admin.clothes.CropImage.cropImageActivity;

public class CropActivity extends Activity {
    ImageView compositeImageView;
    boolean crop;
    Bitmap bitmap2;
    Button tocloset;
    RadioButton top, bottom;
    Bitmap resultingImage;
    int x1 = 999999999, x2 = 0;
    int y1 = 999999999, y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropview);
        tocloset=(Button)findViewById(R.id.tocloset);
        top=(RadioButton)findViewById(R.id.top);
        bottom=(RadioButton)findViewById(R.id.bottom);
        top.setChecked(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crop = extras.getBoolean("crop");
        }
        byte[] arr = getIntent().getByteArrayExtra("image");
        bitmap2 = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        int widthOfscreen = 0;
        int heightOfScreen = 0;

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);
        } catch (Exception ex) {
        }

        widthOfscreen = dm.widthPixels;
        heightOfScreen = dm.heightPixels;

        compositeImageView = (ImageView) findViewById(R.id.our_imageview);
///////////////////////
        //Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
        //      R.drawable.cat);

        resultingImage = Bitmap.createBitmap(widthOfscreen, heightOfScreen, bitmap2.getConfig());
        //resultingImage = Bitmap.createBitmap(resultingImage, 0,60,500,600);

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < SomeView.points.size(); i++) {
            path.lineTo(SomeView.points.get(i).x, SomeView.points.get(i).y);
        }

        for (int i = 0; i < SomeView.points.size(); i++) {
            if(x1 > SomeView.points.get(i).x)
                x1 = SomeView.points.get(i).x;
            if(x2 < SomeView.points.get(i).x)
                x2 = SomeView.points.get(i).x;
            if(y1 > SomeView.points.get(i).y)
                y1 = SomeView.points.get(i).y;
            if(y2 < SomeView.points.get(i).y)
                y2 = SomeView.points.get(i).y;
        }

        Log.i("aa", x1 + " / " + y1 + " / "  + x2 + " / " + y2);
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        int w = bitmap2.getWidth();
        int h = bitmap2.getHeight();
        Rect dst = new Rect(0, 200, 350+ w , 450+ h );
        canvas.drawBitmap(bitmap2, null, dst, paint);
       //canvas.drawBitmap(bitmap2, 0, 0, paint);
        //resultingImage=Bitmap.createScaledBitmap(resultingImage, w+200 , h+400, true);

        compositeImageView.setImageBitmap(Bitmap.createBitmap(resultingImage, x1, y1, x2-x1, y2-y1));

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tocloset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeFragment.topList.add(Bitmap.createBitmap(resultingImage, x1, y1, x2-x1, y2-y1));
                        Intent intent1=new Intent(getApplicationContext(), TabActivity.class);
                        startActivity(intent1);
                        cropImageActivity.finish();
                        finish();
                    }
                });
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tocloset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeFragment.bottomList.add(Bitmap.createBitmap(resultingImage, x1, y1, x2-x1, y2-y1));
                        Intent intent1=new Intent(getApplicationContext(), TabActivity.class);
                        startActivity(intent1);
                        cropImageActivity.finish();
                        finish();
                    }
                });
            }
        });


    }
}