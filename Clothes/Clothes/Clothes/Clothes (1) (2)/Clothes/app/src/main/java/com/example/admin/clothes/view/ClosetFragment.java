package com.example.admin.clothes.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.admin.clothes.CropImage;
import com.example.admin.clothes.R;
import com.example.admin.clothes.SomeView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ClosetFragment extends Fragment {
    Button picture, delete, wear;
    Gallery g;
    final int REQ_CODE_SELECT_IMAGE = 100;
    final int REQ_CODE_CROP_IMAGE = 101;
    LinearLayout linear;
    ArrayList<Bitmap> imgList = new ArrayList<>();
    MyAdapter adapter;
    ImageView[] images;
    private Context context;
    ImageView iv;
    Bitmap image_bitmap;
    Bitmap bitmap1;
    int i = 0;
    int currentPosition = 0;
    Boolean crop = false;
    Bitmap resultingImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.closet, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        context = getActivity();

        bitmap1 = BitmapFactory.decodeResource(getView().getResources(), R.drawable.background);

//        for(int i=0; i<4; i++) imgList.add(bitmap1);


        registerEvent();

        iv = (ImageView) getView().findViewById(R.id.imageView1);

        adapter = new MyAdapter(context, R.layout.inflaterlayout, imgList);    // 데이터

        g = (Gallery) getView().findViewById(R.id.gallery1);
        g.setAdapter(adapter);

        if(imgList.size()<=2){
        origineImage();}


        g.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) { // 선택되었을 때 콜백메서드

//                iv.setImageBitmap((Bitmap) adapter.getItem(position));
//                ByteArrayOutputStream stream;
//                stream = new ByteArrayOutputStream();
//                adapter.getItem(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
//                Glide.with(context).load(stream.toByteArray()).asBitmap().into(iv);
//                Glide.with(context).load(stream.toByteArray()).into(iv);

                iv.setImageBitmap(adapter.getItem(position));
                currentPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedialog(currentPosition);
            }
        });

        wear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cropdialog(currentPosition);
                //getCropImage(position);
                // imgList.set(position, cropbitmap1);
                //iv.setImageBitmap(resultingImage);
            }
        });
    }


    void registerEvent() {
        picture = (Button) getView().findViewById(R.id.picture);
        delete = (Button) getView().findViewById(R.id.delete);
        wear = (Button) getView().findViewById(R.id.wear);
        //사진첩 접근
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (imgList.size() == 4) {
                   // Informdialog();

               // } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                //}
            }
        });

    }


    //삭제 다이얼로그
    private void deletedialog(final int position) {
        if (imgList.size() != 0) {
            final AlertDialog.Builder alt = new AlertDialog.Builder(getActivity());
            alt.setMessage("선택된 사진을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int p = position;
//                        for(int a = p; a< imgList.size()-1; a++) {
//                            Bitmap b = imgList.get(a + 1);
//                            imgList.set(a, b);
//                        }
                            if (imgList.size() >= position)
                                imgList.remove(position);
//                        imgList.add(bitmap1);

                            adapter.notifyDataSetChanged();
                            if (position == 0 && imgList.size() == 0)
                                iv.setImageBitmap(bitmap1);
                            else if (position == 0)
                                iv.setImageBitmap(adapter.getItem(position));
                            else
                                iv.setImageBitmap(adapter.getItem(position - 1));

                            Log.i("Closet", i + " + / position = " + position);
//                        imgList.set(i, bitmap1);
                            i--;
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alt.create();
            alert.setTitle("삭제하기");
            alert.setIcon(R.drawable.icon);
            alert.show();
        }
    }

    //알림
    private void Informdialog() {

        final AlertDialog.Builder alt = new AlertDialog.Builder(getActivity());
        alt.setMessage("더이상 사진을 추가할 수 없습니다.").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt.create();
        alert.setTitle("알림");
        alert.setIcon(R.drawable.icon);
        alert.show();
    }

    //사진첩에서 선택한 이미지 받아오기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    int height = image_bitmap.getHeight();
                    int width = image_bitmap.getWidth();
                   // Toast.makeText(getActivity(), width + " , " + height, Toast.LENGTH_SHORT).show();
                    Bitmap resized = null;
                    while (height > 700) {
                        resized = Bitmap.createScaledBitmap(image_bitmap, (width * 700) / height, 700, true);
                        height = resized.getHeight();
                        width = resized.getWidth();
                    }
                    if(resized != null)
                        image_bitmap = resized;
                    //배치해놓은 ImageView에 set
                    imgList.add(image_bitmap);
                    adapter.notifyDataSetChanged();


                    i++;
                    Log.i("closet", i + "");

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQ_CODE_CROP_IMAGE) {
                if (requestCode == Activity.RESULT_OK) {
                    Bundle extras = getActivity().getIntent().getExtras();
                    if (extras != null) {
                        crop = extras.getBoolean("crop");
                    }
                    byte[] arr = getActivity().getIntent().getByteArrayExtra("image");
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(arr, 0, arr.length);

                    int widthOfscreen = 0;
                    int heightOfScreen = 0;

                    DisplayMetrics dm = new DisplayMetrics();
                    try {
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                    } catch (Exception ex) {
                    }
                    widthOfscreen = dm.widthPixels;
                    heightOfScreen = dm.heightPixels;

                    //compositeImageView = (ImageView) findViewById(R.id.our_imageview);
///////////////////////
                    //Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
                    //      R.drawable.cat);

                    resultingImage = Bitmap.createBitmap(widthOfscreen, heightOfScreen, bitmap2.getConfig());

                    Canvas canvas = new Canvas(resultingImage);
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);

                    Path path = new Path();
                    for (int i = 0; i < SomeView.points.size(); i++) {
                        path.lineTo(SomeView.points.get(i).x, SomeView.points.get(i).y);
                    }
                    canvas.drawPath(path, paint);
                    if (crop) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                    } else {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                    }
                    canvas.drawBitmap(bitmap2, 0, 0, paint);
                    // compositeImageView.setImageBitmap(resultingImage);

                }
            }
        }
    }

    //자르기 대화상자
    private void cropdialog(final int position) {
        if (imgList.size() != 0) {
            final AlertDialog.Builder alt = new AlertDialog.Builder(getActivity());
            alt.setMessage("선택된 사진을 자르시겠습니까?").setCancelable(false).setPositiveButton("자르기",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int p = position;
                            Intent intent = new Intent(getView().getContext(), CropImage.class);
                            //intent.putExtra("bitmap","bitmap");
                            Bitmap cropImage = imgList.get(position);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            cropImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            intent.putExtra("image", byteArray);
                            startActivity(intent);
                            //startActivityForResult(intent, REQ_CODE_CROP_IMAGE);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alt.create();
            alert.setTitle("이미지 자르기");
            alert.setIcon(R.drawable.icon);
            alert.show();
        }
    }

//


    //갤러리 어댑터
    class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        ArrayList<Bitmap> img;
        LayoutInflater inf;
        ByteArrayOutputStream stream;


        public MyAdapter(Context context, int layout, ArrayList<Bitmap> img) {
            this.context = context;
            this.layout = layout;
            this.img = img;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();
        }

        @Override
        public int getCount() {
            return img.size();
        }

        @Override
        public Bitmap getItem(int position) {
            return img.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image1 = null;
            if (convertView == null)
                convertView = inf.inflate(layout, null);
            image1 = (ImageView) convertView.findViewById(R.id.image1);
            image1.setImageBitmap((Bitmap) getItem(position));

//            stream = new ByteArrayOutputStream();
//            getItem(position).compress(Bitmap.CompressFormat.PNG, 100, stream);
//            Glide.with(context).load(stream.toByteArray()).asBitmap().into(image1);

            //Toast.makeText(context, "inflaterLayout",Toast.LENGTH_SHORT).show();

            return convertView;
        }
    }
    public void origineImage(){
        BitmapDrawable drawable1 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth1);
        Bitmap cloth1 = drawable1.getBitmap();
        BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth2);
        Bitmap cloth2 = drawable2.getBitmap();
        BitmapDrawable drawable3 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth3);
        Bitmap cloth3 = drawable3.getBitmap();
        BitmapDrawable drawable5 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth5);
        Bitmap cloth5 = drawable5.getBitmap();
        BitmapDrawable drawable6 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth6);
        Bitmap cloth6 = drawable6.getBitmap();
        BitmapDrawable drawable7 = (BitmapDrawable) getResources().getDrawable(R.drawable.cloth7);
        Bitmap cloth7 = drawable7.getBitmap();
        int height1 = cloth1.getHeight();
        int width1 = cloth1.getWidth();
        Bitmap resized1 = null;

        while (height1 > 700) {
            resized1 = Bitmap.createScaledBitmap(cloth1, (width1 * 700) / height1, 700, true);
            height1 = resized1.getHeight();
            width1 = resized1.getWidth();
        }
        int height2 = cloth2.getHeight();
        int width2 = cloth2.getWidth();
        Bitmap resized2 = null;
        while (height2 > 700) {
            resized2 = Bitmap.createScaledBitmap(cloth2, (width2 * 700) / height2, 700, true);
            height2 = resized2.getHeight();
            width2= resized2.getWidth();
        }
        int height3 = cloth3.getHeight();
        int width3 = cloth3.getWidth();
        Bitmap resized3 = null;
        while (height3 > 700) {
            resized3 = Bitmap.createScaledBitmap(cloth3, (width3 * 700) / height3, 700, true);
            height3 = resized3.getHeight();
            width3 = resized3.getWidth();
        }

        int height5 = cloth5.getHeight();
        int width5 = cloth5.getWidth();
        Bitmap resized5 = null;
        while (height5 > 700) {
            resized5 = Bitmap.createScaledBitmap(cloth5, (width5 * 700) / height5, 700, true);
            height5 = resized5.getHeight();
            width5 = resized5.getWidth();
        }
        int height6 = cloth6.getHeight();
        int width6 = cloth6.getWidth();
        Bitmap resized6 = null;
        while (height6 > 700) {
            resized6 = Bitmap.createScaledBitmap(cloth6, (width6 * 700) / height6, 700, true);
            height6 = resized6.getHeight();
            width6 = resized6.getWidth();
        }
        int height7 = cloth7.getHeight();
        int width7 = cloth7.getWidth();
        Bitmap resized7 = null;
        while (height7 > 700) {
            resized7 = Bitmap.createScaledBitmap(cloth7, (width7 * 700) / height7, 700, true);
            height7 = resized7.getHeight();
            width7 = resized7.getWidth();
        }
        if(resized1 != null)
            cloth1 = resized1;
        if(resized2 != null)
            cloth2 = resized2;
        if(resized3 != null)
            cloth3 = resized3;
        if(resized5 != null)
            cloth5 = resized5;
        if(resized6 != null)
            cloth6 = resized6;
        if(resized7 != null)
            cloth7 = resized7;



        imgList.add(cloth1); i++;
        imgList.add(cloth2); i++;
        imgList.add(cloth3); i++;
        imgList.add(cloth5); i++;
        imgList.add(cloth6); i++;
        imgList.add(cloth7); i++;

    }
}

