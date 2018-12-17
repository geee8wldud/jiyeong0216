package com.example.admin.clothes.view;
//Board

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.clothes.Boardwrite;
import com.example.admin.clothes.Listdetail;
import com.example.admin.clothes.R;
import com.example.admin.clothes.SQLiteHandlerBoard;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.example.admin.clothes.R.layout.listview;

public class BoardFragment extends Fragment {
    public  MyAdapter3 adapter;
    public static ArrayList<ListViewItem> boardlist = new ArrayList<ListViewItem>() ;
    ListView boardlistview;
    Button write, delete;

    private static final String TABLE_BOARD = "boardtable";
    Cursor c2=null;
    private SharedPreferences appData;
    private boolean saveLoginData;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.board, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        write=(Button)getView().findViewById(R.id.write);
      //  delete=(Button)getView().findViewById(R.id.delete);
        boardlistview=(ListView)getView().findViewById(R.id.boardlistview);
        checkBox = (CheckBox) getView().findViewById(R.id.checkBox);

       // BoardDB = new SQLiteHandlerBoard(getActivity().getApplicationContext());

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //  bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        //커스텀 어댑터 설정
        adapter = new MyAdapter3(getActivity(), listview, boardlist);
        adapter.notifyDataSetChanged();

        boardlistview.setAdapter(adapter);  // 커스텀 어댑터를 ListView 에 적용

        Eventregister();

        //selectAllDB();
    }


    void Eventregister(){
        //글쓰기 버튼
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), Boardwrite.class);
                startActivity(intent);
            }
        });

    if(Boardwrite.contents!=null){
        if(Boardwrite.uploadbitmap==null){
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
            Bitmap basicicon = drawable.getBitmap();
            adapter.addItem(basicicon, Boardwrite.title, Boardwrite.contents) ;
            Boardwrite.uploadbitmap=null;
            Boardwrite.title=null;
            Boardwrite.contents=null;
        }else{
            adapter.addItem(Boardwrite.uploadbitmap, Boardwrite.title, Boardwrite.contents ) ;
           // Boardwrite.uploadbitmap=null;
            Boardwrite.title=null;
            Boardwrite.contents=null;
            Boardwrite.uploadbitmap=null;
        }
    }

        //adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.icon), "Box", "Account Box Black 36dp") ;

////////////////////////////////////////////////////////////////////////////////////////////////////

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        boardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String titleStr = item.getTitle();
                String descStr = item.getDesc();
                Bitmap iconBitmap = item.getIcon();

                //Toast.makeText(getActivity(), titleStr+"", Toast.LENGTH_LONG).show();

                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
                Bitmap basicicon = drawable.getBitmap();

                //Listdetail에 정보 넘겨주기
                Intent intent = new Intent(getContext().getApplicationContext(), Listdetail.class);

                intent.putExtra("title", titleStr);
                intent.putExtra("contents",descStr);

                //Bitmap iconBitmap = drawableToBitmap(iconDrawable);

              //  BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(iconDrawable);
              // Bitmap iconBitmap = drawable.getBitmap();

                //Bitmap iconBitmap = ((BitmapDrawable)iconDrawable).getBitmap();

               // BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
               // Bitmap basicicon = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);

                startActivity(intent);

                // TODO : use item data.
            }
        });


        //길게 눌렀을 경우
        boardlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                final AlertDialog.Builder alt=new AlertDialog.Builder(getActivity());

                alt.setMessage("정말 삭제하시겠습니까?").setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeIndex(position);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert=alt.create();
                alert.setTitle("게시글 삭제");
                alert.setIcon(R.drawable.icon);
                alert.show();

                return true;
            }
        });

    }

    class MyAdapter3 extends BaseAdapter {
        Activity context;
        int layout;
        LayoutInflater inf;
        ByteArrayOutputStream stream;

        private ArrayList<ListViewItem> boardlist = new ArrayList<ListViewItem>();

        // ListViewAdapter의 생성자
        public MyAdapter3(Activity context, int layout, ArrayList<ListViewItem> boardlist) {
            this.context = context;
            this.layout = layout;
            this.boardlist = boardlist;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return boardlist.size();
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(listview, parent, false);
            }

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
            TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            ListViewItem listViewItem = boardlist.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            iconImageView.setImageBitmap(listViewItem.getIcon());
            titleTextView.setText(listViewItem.getTitle());
            descTextView.setText(listViewItem.getDesc());

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
            return boardlist.get(position);
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(Bitmap icon, String title, String desc) {
            ListViewItem item = new ListViewItem();

            item.setIcon(icon);
            item.setTitle(title);
            item.setDesc(desc);

            boardlist.add(item);
        }

        public void removeItem(String content) {
            boardlist.remove(content);
        }

        public void removeIndex(int i) {
            boardlist.remove(i);
        }
    }


    //Drawable--> Bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }



    //데이터베이스 안에 있는 값들을 가져와서 화면 갱신
    public Cursor selectAllDB() {
        String username, title, board_context, board_date;
        c2 = SQLiteHandlerBoard.BoardDB.rawQuery("select username, title, board_context, board_date " + TABLE_BOARD, null);
        for (int i = 0; i < c2.getCount(); i++) {
            c2.moveToNext();
            username = c2.getString(0);
            title = c2.getString(1);
            board_context = c2.getString(2);
            board_date = c2.getString(3);
            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
            Bitmap basicicon = drawable.getBitmap();
            adapter.addItem(basicicon, title, board_context) ;

        }

        c2.close();
        return c2;
    }

}

