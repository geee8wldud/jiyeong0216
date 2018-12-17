package com.example.admin.clothes.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.clothes.Boardwrite;
import com.example.admin.clothes.MyHeart;
import com.example.admin.clothes.R;
import com.example.admin.clothes.SQLiteHandler;
import com.example.admin.clothes.model.SessionManager;
import com.like.LikeButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.admin.clothes.R.layout.ranklistview;

public class RankFragment extends Fragment {

    @Bind(R.id.heart_button)
    LikeButton likeButton;
    Button save1, save2, save3, save4, save5, padd;
    BitmapDrawable d1, d2, d3, d4, d5;
    Bitmap b1, b2, b3, b4, b5;
    MyAdapter4 adapter4;
    public ArrayList<ListViewItem> ranklist = new ArrayList<ListViewItem>();
    ListView rlistview;
    private SQLiteHandler db;
    private SessionManager session;
    String name;
    ImageView rimage;
    Bitmap rimagebitmap = null;
    final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rank, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.bind(getActivity());
        rlistview = (ListView) getView().findViewById(R.id.ranklistview);
        rimage = (ImageView) getView().findViewById(R.id.rankimage);

        //커스텀 어댑터 설정
        adapter4 = new MyAdapter4(getActivity(), ranklistview, ranklist);
        adapter4.notifyDataSetChanged();

        rlistview.setAdapter(adapter4);  // 커스텀 어댑터를 ListView 에 적용

        save1 = (Button) getView().findViewById(R.id.save1);
        save2 = (Button) getView().findViewById(R.id.save2);
        save3 = (Button) getView().findViewById(R.id.save3);
        save4 = (Button) getView().findViewById(R.id.save4);
        save5 = (Button) getView().findViewById(R.id.save5);
        padd = (Button) getView().findViewById(R.id.padd);

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d1 = (BitmapDrawable) ((ImageView) getView().findViewById(R.id.image1)).getDrawable();
                b1 = d1.getBitmap();
                MyHeart.Heartimg.add(b1);
                Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d2 = (BitmapDrawable) ((ImageView) getView().findViewById(R.id.image2)).getDrawable();
                b2 = d2.getBitmap();
                MyHeart.Heartimg.add(b2);
                Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        save3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d3 = (BitmapDrawable) ((ImageView) getView().findViewById(R.id.image3)).getDrawable();
                b3 = d3.getBitmap();
                MyHeart.Heartimg.add(b3);
                Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        save4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d4 = (BitmapDrawable) ((ImageView) getView().findViewById(R.id.image4)).getDrawable();
                b4 = d4.getBitmap();
                MyHeart.Heartimg.add(b5);
                Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        save5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d5 = (BitmapDrawable) ((ImageView) getView().findViewById(R.id.image5)).getDrawable();
                b5 = d5.getBitmap();
                MyHeart.Heartimg.add(b5);
                Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                //  adapter.addItem(basicicon, Boardwrite.title) ;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    rimagebitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    int height = rimagebitmap.getHeight();
                    int width = rimagebitmap.getWidth();
                    // Toast.makeText(getActivity(), width + " , " + height, Toast.LENGTH_SHORT).show();
                    Bitmap resized = null;
                    while (height > 700) {
                        resized = Bitmap.createScaledBitmap(rimagebitmap, (width * 700) / height, 700, true);
                        height = resized.getHeight();
                        width = resized.getWidth();
                    }
                    if (resized != null)
                        rimagebitmap = resized;
                    //배치해놓은 ImageView에 set
                    adapter4.addItem(rimagebitmap, Boardwrite.title);


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

    class MyAdapter4 extends BaseAdapter {
        Activity context;
        int layout;
        LayoutInflater inf;
        ByteArrayOutputStream stream;

        private ArrayList<ListViewItem> ranklist = new ArrayList<ListViewItem>();

        // ListViewAdapter의 생성자
        public MyAdapter4(Activity context, int layout, ArrayList<ListViewItem> ranklist) {
            this.context = context;
            this.layout = layout;
            this.ranklist = ranklist;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return ranklist.size();
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(ranklistview, parent, false);
            }

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            ImageView rankimageview = (ImageView) convertView.findViewById(R.id.rankimage);
            TextView ranktext = (TextView) convertView.findViewById(R.id.ranktext);
            Button savei=(Button) convertView.findViewById(R.id.savei);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final ListViewItem listViewItem = ranklist.get(position);
            // SqLite database handler
            db = new SQLiteHandler(getActivity());
            // session manager
            session = new SessionManager(getActivity());
            HashMap<String, String> user = db.getUserDetails();
            name = user.get("name");

            // 아이템 내 각 위젯에 데이터 반영
            rankimageview.setImageBitmap(listViewItem.getIcon());
            ranktext.setText(name);
            savei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyHeart.Heartimg.add(listViewItem.getIcon());
                    Toast.makeText(getActivity().getApplicationContext(), "내코디에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }


        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return ranklist.get(position);
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(Bitmap icon, String title) {
            ListViewItem item = new ListViewItem();

            item.setIcon(icon);
            item.setTitle(title);

            ranklist.add(item);
        }

        public void removeItem(String content) {
            ranklist.remove(content);
        }

        public void removeIndex(int i) {
            ranklist.remove(i);
        }
    }
}