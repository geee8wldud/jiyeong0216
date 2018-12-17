package com.example.admin.clothes;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.clothes.model.SessionManager;
import com.example.admin.clothes.view.BoardFragment;
import com.example.admin.clothes.view.ListViewItem;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.admin.clothes.R.layout.commentslist;
public class Listdetail extends Activity{
    TextView listdetailtitle, listdetailcontents, userID;
    ImageView uploadedphoto,commentphoto;
    Button confirm, back, addPicture;
    String title, contents;
    Bitmap bitmap;
    EditText comments;
    String commentString=null;
    String nowDate;
    public Bitmap commentuploadBitmap;
    public Drawable commentuploadDrawable;

    public MyAdapter4 adapter;
    public  ArrayList<ListViewItem> commentList = new ArrayList<ListViewItem>() ;
    ListView commentListView;
    Bitmap basicicon;

    //DB
    private SQLiteHandler db;
    private SessionManager session;
    String username;
    String useremail;
    final int REQ_CODE_SELECT_IMAGE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdetaillayout);
        listdetailtitle=(TextView)findViewById(R.id.listdetailtitle);
        listdetailcontents=(TextView)findViewById(R.id.listdetailcontents);
        uploadedphoto=(ImageView)findViewById(R.id.uploadedphoto);
        confirm=(Button)findViewById(R.id.confirm);
        back=(Button)findViewById(R.id.back);
        comments=(EditText)findViewById(R.id.comments);
        userID=(TextView)findViewById(R.id.userID);
        commentListView=(ListView)findViewById(R.id.commentListView);
        commentphoto=(ImageView)findViewById(R.id.commentphoto);
        addPicture=(Button)findViewById(R.id.addPicture);

/////////////////////////////////////////////////////////////////////////////////////
        //커스텀 어댑터 설정
        adapter = new MyAdapter4(Listdetail.this, commentslist , commentList);
        adapter.notifyDataSetChanged();
        commentListView.setAdapter(adapter);  // 커스텀 어댑터를 ListView 에 적용


        //넘겨온 정보들 받아서 보여주기
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        contents=intent.getStringExtra("contents");
        //bitmap= (Bitmap)intent.getParcelableExtra("image");

        byte[] arr = intent.getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
        basicicon = drawable.getBitmap();


        if(!sameAs(bitmap, basicicon)) {
            uploadedphoto.setVisibility(View.VISIBLE);
            uploadedphoto.setImageBitmap(bitmap);
            listdetailtitle.setText(title);
            listdetailcontents.setText(contents);
        }else{
            listdetailtitle.setText(title);
            listdetailcontents.setText(contents);
        }


        //DB 정보 받아오기
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        username = user.get("name");
        useremail = user.get("email");
        userID.setText(username+" : "+useremail);


        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddphoto();
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentString = comments.getText().toString();
                if (commentString.length()==0) {
                    AlertDialog.Builder alt1 = new AlertDialog.Builder(Listdetail.this);
                    alt1.setMessage("내용을 입력해주세요!").setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alt1.create();
                    alert.setTitle("경고");
                    alert.setIcon(R.drawable.icon);
                    alert.show();
                }else{
                    commentString = comments.getText().toString();
                    Date now = new Date();
                    nowDate = now.toString();
                    nowDate=username+" :: "+nowDate;
                    if(commentString!=null){
                        if(commentuploadBitmap==null){
                            adapter.addItem(basicicon, commentString, nowDate) ;
                            commentuploadBitmap=null;
                        }else if(commentuploadBitmap!=null){

                            adapter.addItem(commentuploadBitmap, commentString, nowDate) ;
                            commentuploadBitmap=null;

                        }
                    }
                    AlertDialog.Builder alt1 = new AlertDialog.Builder(Listdetail.this);
                    alt1.setMessage("입력되었습니다.").setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    adapter.notifyDataSetChanged();
                                    commentphoto.setImageBitmap(null);
                                    comments.setText(null);
                                }
                            });
                    AlertDialog alert = alt1.create();
                    alert.setTitle("알림");
                    alert.setIcon(R.drawable.icon);
                    alert.show();
                }
            }

        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //등록된 댓글을 눌렀을때

    }


    //비트맵 비교
    private boolean sameAs(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);
        return Arrays.equals(buffer1.array(), buffer2.array());
    }





    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
    }

    //댓글에 사진 첨부
    private void setAddphoto(){
        final AlertDialog.Builder alt=new AlertDialog.Builder(this);
        alt.setMessage("사진을 첨부하시겠습니까?").setCancelable(false).setPositiveButton("예",
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
        alert.setTitle("사진 첨부");
        alert.setIcon(R.drawable.icon);
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    commentuploadBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    commentuploadDrawable = getDrawableFromBitmap(commentuploadBitmap);
                    commentphoto.setVisibility(View.VISIBLE);
                    commentphoto.setImageBitmap(commentuploadBitmap);
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

        //bitmap--> drawable
    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        return d;
    }


    class MyAdapter4 extends BaseAdapter {
        Activity context;
        int layout;
        LayoutInflater inf;
        ByteArrayOutputStream stream;

        private ArrayList<ListViewItem> commentList = new ArrayList<ListViewItem>() ;

        // ListViewAdapter의 생성자
        public MyAdapter4(Activity context, int layout, ArrayList<ListViewItem> commentList) {
            this.context = context;
            this.layout = layout;
            this.commentList=commentList;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            stream = new ByteArrayOutputStream();

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return commentList.size() ;
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(commentslist, parent, false);
            }

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
            TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
            TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            ListViewItem listViewItem = commentList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            //baicicicon과 같지 않으면 visible
            if(!sameAs(listViewItem.getIcon(), basicicon)){
                iconImageView.setVisibility(View.VISIBLE);}
            else{ iconImageView.setVisibility(View.GONE);}
            iconImageView.setImageBitmap(listViewItem.getIcon());
            titleTextView.setText(listViewItem.getTitle());
            descTextView.setText(listViewItem.getDesc());

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return commentList.get(position) ;
        }


        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(Bitmap icon, String title, String desc) {
            ListViewItem item = new ListViewItem();

            item.setIcon(icon);
            item.setTitle(title);
            item.setDesc(desc);

            commentList.add(item);
        }


    }
}
