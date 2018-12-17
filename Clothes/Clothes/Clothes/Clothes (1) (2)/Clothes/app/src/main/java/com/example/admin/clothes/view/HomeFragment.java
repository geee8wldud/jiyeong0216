package com.example.admin.clothes.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.clothes.MyHeart;
import com.example.admin.clothes.MyModel;
import com.example.admin.clothes.R;
import com.example.admin.clothes.StickerImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import static com.example.admin.clothes.MyInfo.normalweight;
import static com.example.admin.clothes.MyInfo.normalweight1;
import static com.example.admin.clothes.MyInfo.weight;
import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment {
    ImageView modelImage;
    Button savebtn, returnbtn, sharebtn, removeallclothes, myface;
    Bitmap captureView;
    View container;
    Context context;
    private ViewGroup rootLayout;
    DrawerLayout drawerLayout;
    View drawerView;
    ImageView img;
    RadioButton topbtn, bottombtn;

    GridView simpleList;
    public static Vector<Bitmap> clothesList = new Vector<Bitmap>();
    public static Vector<Bitmap> topList = new Vector<Bitmap>();
    public static Vector<Bitmap> bottomList = new Vector<Bitmap>();
    MyAdapter myAdapter;
    private int _xDelta;
    private int _yDelta;
    ImageView registermodel;
    RelativeLayout canvas;
    StickerImageView iv_sticker, bottom_sticker;


    ///
    ImageView capture;
    final int REQ_CODE_SELECT_IMAGE=100;
    Bitmap selfbitmap;
    File fileCacheItem;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        myface = (Button)getView().findViewById(R.id.myface);
        myface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changemyface();
            }
        });
        savebtn = (Button) getView().findViewById(R.id.savebtn);
        sharebtn = (Button) getView().findViewById(R.id.sharebtn);
        returnbtn=(Button)getView().findViewById(R.id.returnbtn);
        removeallclothes= (Button) getView().findViewById(R.id.removeallclothes);
        modelImage = (ImageView) getView().findViewById(R.id.modelpicture);
        img = (ImageView) getView().findViewById(R.id.img1);
        rootLayout = (ViewGroup) getView().findViewById(R.id.drawer_layout);
        registermodel=(ImageView)getView().findViewById(R.id.registermodel);
        canvas = (RelativeLayout) getView().findViewById(R.id.canvasView);
        topbtn=(RadioButton)getView().findViewById(R.id.topbtn);
        bottombtn=(RadioButton)getView().findViewById(R.id.bottombtn);
        iv_sticker = new StickerImageView(HomeFragment.this.getActivity());
        bottom_sticker = new StickerImageView(HomeFragment.this.getActivity());

        capture=(ImageView)getView().findViewById(R.id.capture);

        topbtn.setChecked(true);
        registerEvent();
        //공유하기
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
                //captureImage2();
            }
        });
        //관심에 모델 저장하기
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container = getActivity().getWindow().getDecorView();
                iv_sticker.iv_scale.setVisibility(View.INVISIBLE);
                iv_sticker.iv_delete.setVisibility(View.INVISIBLE);
                bottom_sticker.iv_scale.setVisibility(View.INVISIBLE);
                bottom_sticker.iv_delete.setVisibility(View.INVISIBLE);
                container.buildDrawingCache();
                captureView = container.getDrawingCache();
                captureView = captureView.createBitmap(captureView, 10, 300, 900, 1400);
                MyHeart.Heartimg.add(captureView);
                Toast.makeText(getActivity().getApplicationContext(), "관심에 저장하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //되돌리기 버튼
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_sticker.iv_scale.setVisibility(View.VISIBLE);
                iv_sticker.iv_delete.setVisibility(View.VISIBLE);
                bottom_sticker.iv_scale.setVisibility(View.VISIBLE);
                bottom_sticker.iv_delete.setVisibility(View.VISIBLE);
            }
        });

        removeallclothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topbtn.isChecked()==true){
                    topList.removeAllElements();
                }else if(bottombtn.isChecked()==true){
                    bottomList.removeAllElements();
                }
                // clothesList.removeAllElements();
            }
        });

        //그리드뷰에 이미지 눌렀을때
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // imgList.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                //imgList.setImageBitmap(clothesList.get(position));
                //imgList.setOnTouchListener(new ChoiceTouchListener());
                //iv_sticker.setImageBitmap(clothesList.get(position));
                //canvas.addView(iv_sticker);

                if(topbtn.isChecked()==true){
                    iv_sticker.setImageBitmap(topList.get(position));
                    canvas.addView(iv_sticker);
                }else if(bottombtn.isChecked()==true){
                    bottom_sticker.setImageBitmap(bottomList.get(position));
                    canvas.addView(bottom_sticker);
                }
            }

        });


        if(MyModel.image_bitmap==null){
            Glide.with(getActivity()).load(R.drawable.body2).into(registermodel);
            if(normalweight != null) {
                if(Double.parseDouble(weight.getText().toString()) >= normalweight1 + (normalweight1*0.1)) {
                    Glide.with(getActivity()).load(R.drawable.body1).into(registermodel);
                }
                else if(Double.parseDouble(weight.getText().toString()) <= normalweight1 - (normalweight1*0.1)) {
                    Glide.with(getActivity()).load(R.drawable.body3).into(registermodel);
                }
                else {
                    Glide.with(getActivity()).load(R.drawable.body2).into(registermodel);
                }
            }
//            registermodel.setImageResource(R.drawable.model4);

        }else{
//            registermodel.setImageBitmap(BoardFragment.image_bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MyModel.image_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(getActivity()).load(stream.toByteArray()).asBitmap().into(registermodel);
            myface.setVisibility(View.INVISIBLE);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    selfbitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    Drawable selfdrawable=getDrawableFromBitmap(selfbitmap);
                    myface.setBackground(selfdrawable);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //이미지뷰 이동
    private final class ChoiceTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
    }

    //공유하기
    private void captureImage() {

        container = getActivity().getWindow().getDecorView();
        container.buildDrawingCache();
        captureView = container.getDrawingCache();
        captureView = captureView.createBitmap(captureView, 10, 300, 900, 1400);
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), captureView, "title", "descripton");



        Toast.makeText(getActivity().getApplicationContext(), "Captured!", Toast.LENGTH_LONG).show();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                + String.valueOf((new Date()).getTime()) + ".png";
        OutputStream out = null;
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            captureView.compress(Bitmap.CompressFormat.PNG, 100, out);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        path = file.getPath();
        Uri bmpUri = Uri.parse("file://" + path);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }


        //슬라이드바
    public void registerEvent() {
        drawerLayout = (DrawerLayout) getView().findViewById(R.id.drawer_layout);
        drawerView = (View) getView().findViewById(R.id.drawer);

        ImageButton buttonOpenDrawer = (ImageButton) getView().findViewById(R.id.opendrawer);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button buttonCloseDrawer = (Button) getView().findViewById(R.id.closedrawer);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawerLayout.closeDrawers();
            }
        });

        drawerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        myAdapter = new MyAdapter(getActivity(), R.layout.topview1, topList);

        // myAdapter.clear();
        myAdapter.notifyDataSetChanged();

        //  imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
        simpleList = (GridView) getView().findViewById(R.id.simpleGridView);
        simpleList.setAdapter(myAdapter);

        topbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter(getActivity(), R.layout.topview1, topList);
                // myAdapter.clear();
                myAdapter.notifyDataSetChanged();
                //  imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
                simpleList = (GridView) getView().findViewById(R.id.simpleGridView);
                simpleList.setAdapter(myAdapter);
            }
        });
      /*  myAdapter = new MyAdapter(getActivity(), R.layout.topView, clothesList);

        // myAdapter.clear();
        myAdapter.notifyDataSetChanged();

        //  imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
        simpleList = (GridView) getView().findViewById(R.id.simpleGridView);
        simpleList.setAdapter(myAdapter);*/

        bottombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter = new MyAdapter(getActivity(), R.layout.bottomview, bottomList);

                // myAdapter.clear();
                myAdapter.notifyDataSetChanged();

                //  imageView2 = (ImageView) getView().findViewById(R.id.imageView2);
                simpleList = (GridView) getView().findViewById(R.id.simpleGridView);
                simpleList.setAdapter(myAdapter);
            }
        });


    }

    class MyAdapter extends BaseAdapter {
        Activity context;
        int layout;
        Vector<Bitmap> clothesList;
        LayoutInflater inflater;
        ByteArrayOutputStream stream;

        public MyAdapter(Activity context, int layout, Vector<Bitmap> clothesList) {
            this.context = context;
            this.layout = layout;
            this.clothesList = clothesList;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();
        }

        @Override
        public int getCount() {
            return clothesList.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return clothesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(layout, null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView1);

     //       getItem(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
   //         Glide.with(context).load(stream.toByteArray()).asBitmap().into(iv);
            iv.setImageBitmap((Bitmap) getItem(position));

            return convertView;
        }
    }
    private void changemyface(){
        final AlertDialog.Builder alt=new AlertDialog.Builder(getActivity());
        alt.setMessage("얼굴 사진을 설정하시겠습니까?").setCancelable(false).setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=alt.create();
        alert.setTitle("얼굴 사진 변경");
        alert.setIcon(R.drawable.icon);
        alert.show();
    }
    //bitmap--> drawable
    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        return d;
    }





}