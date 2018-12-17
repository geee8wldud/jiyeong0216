package com.example.admin.clothes;

//이미지 사용자 정의 자르기

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

public class CropImage extends Activity{
    Bitmap bitmap;
    ImageView bitmapimage;
    static Activity cropImageActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropimage);
        cropImageActivity=this;
        bitmapimage=(ImageView)findViewById(R.id.bitmapimage);

        byte[] arr = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        bitmapimage.setImageBitmap(bitmap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(new SomeView(CropImage.this, bitmap));
    }

}
