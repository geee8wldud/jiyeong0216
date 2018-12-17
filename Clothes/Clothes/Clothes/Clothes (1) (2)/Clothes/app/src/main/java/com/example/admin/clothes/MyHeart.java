package com.example.admin.clothes;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

public class MyHeart extends Activity {
    MyAdapter2 adapter;
    static public Vector<Bitmap> Heartimg = new Vector<Bitmap>();
    Bitmap bitmap1;
    GridView gv;
    Button closeheart;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heart);

        //  bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        //커스텀 어댑터 설정
        adapter = new MyAdapter2(this, R.layout.heartview, Heartimg);
        adapter.notifyDataSetChanged();

        gv = (GridView) findViewById(R.id.gridView1);
        gv.setAdapter(adapter);  // 커스텀 어댑터를 GridView 에 적용


        // GridView 아이템을 클릭
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showcustomDialog(position);
            }
        });

        closeheart = (Button) findViewById(R.id.closeheart);
        closeheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showcustomDialog(final int position) {
        final LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.heartcustomerdialog, null);
        final Dialog myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(dialogLayout);
        myDialog.show();
        Button btn_ok = (Button) dialogLayout.findViewById(R.id.btn_ok);
        //Button insertphoto = (Button) dialogLayout.findViewById(R.id.insertphoto);
        ImageView fullImage = (ImageView) dialogLayout.findViewById(R.id.fullImage);
        fullImage.setImageBitmap(Heartimg.get(position));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
    }
        /*insertphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream out = null;
                Bitmap bitmap=Heartimg.get(position);

                String imagesaveuri= MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "사진저장", "저장되었습니다.");
                Uri uri=Uri.parse(imagesaveuri);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                //  out=new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/01/heart.png");
                // bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                //Toast.makeText(getApplicationContext(), "앨범에 저장되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
/*
    public void SaveImage(int position) {

        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = sd + "/cameratest.jpg";

        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(MyHeart.this, "파일 저장 중 에러 발생: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }


        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse("file://" + path);
        intent.setData(uri);
        sendBroadcast(intent);

        Toast.makeText(MyHeart.this, "사진 저장 완료 : " + path, Toast.LENGTH_LONG).show();

    }*/

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MyHeart Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    class MyAdapter2 extends BaseAdapter {
        Activity context;
        int layout;
        Vector<Bitmap> Heartimg;
        LayoutInflater inf;
        ByteArrayOutputStream stream;

        public MyAdapter2(Activity context, int layout, Vector<Bitmap> Heartimg) {
            this.context = context;
            this.layout = layout;
            this.Heartimg = Heartimg;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();
        }

        @Override
        public int getCount() {
            return Heartimg.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return Heartimg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inf.inflate(layout, null);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView2);

            stream = new ByteArrayOutputStream();
            getItem(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(context).load(stream.toByteArray()).asBitmap().into(iv);
//        Glide.with(context).load();
//        iv.setImageBitmap((Bitmap) getItem(position));

            return convertView;
        }
    }
}

