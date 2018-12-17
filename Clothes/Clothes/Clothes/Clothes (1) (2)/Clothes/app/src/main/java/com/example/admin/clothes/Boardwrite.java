package com.example.admin.clothes;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.clothes.model.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Boardwrite extends Activity{
    private static final String TAG = Boardwrite.class.getSimpleName();

    Button cancelwrite, savewrite, addphoto;
    ImageView photo;
    public static Bitmap uploadbitmap=null;
    public static Drawable uploadDrawable=null;
    EditText writetitle, writecontent;
    TextView writerID;
    public static String title, contents;
    //DB
    private SQLiteHandler db;
    private SessionManager session;
    String username;
    String useremail;

   /* //////DB2
    Cursor c2=null;
    String tablename="boardtable";
    String tableid;
    DBHelper dbHelper;
    SQLiteDatabase db1;
    String username1, title1, board_context, board_date;
*/
    //BoardDB
    private ProgressDialog pDialog;
    private SQLiteHandlerBoard BoardDB;

    final int REQ_CODE_SELECT_IMAGE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeboard);

        cancelwrite = (Button) findViewById(R.id.cancelwrite);
        addphoto = (Button) findViewById(R.id.addphoto);
        savewrite = (Button) findViewById(R.id.savewrite);
        photo = (ImageView) findViewById(R.id.photo);
        writetitle = (EditText) findViewById(R.id.writetitle);
        writecontent = (EditText) findViewById(R.id.writecontent);
        writerID=(TextView) findViewById(R.id.writerID);


        //BoardDB
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //BoardDB = new SQLiteHandlerBoard(getApplicationContext());


        //DB 정보 받아오기
        // SqLite database handler
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
        writerID.setText(username+ " : "+ useremail + " :: ");


        cancelwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddphoto();
            }
        });


        title = writetitle.getText().toString();
        contents = writecontent.getText().toString();


        savewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writetitle.length()==0){
                    AlertDialog.Builder alt1=new AlertDialog.Builder(Boardwrite.this);
                    alt1.setMessage("제목을 입력해주세요!").setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert=alt1.create();
                    alert.setTitle("경고");
                    alert.setIcon(R.drawable.icon);
                    alert.show();

                }else if(writetitle.length()!=0){
                    title = writetitle.getText().toString();
                    contents = writecontent.getText().toString();
                    //BoardDB에 저장
                    //registerBoard(username, title , contents);
                    finish();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    uploadbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    uploadDrawable=getDrawableFromBitmap(uploadbitmap);
                    photo.setVisibility(View.VISIBLE);
                    photo.setImageBitmap(uploadbitmap);
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


    //게시글에 사진 첨부
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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        //btnLogout.setText("로그아웃");
        //txtName.setText(name);
        //txtEmail.setText(email);
    }
/*

///BoardDB에 저장
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerBoard(final String username, final String title, final String board_context) {
        // Tag used to cancel the request
        String tag_string_req = "req_boardregister";

       // pDialog.setMessage("게시글 저장 중 ...");
        showDialog();
        String board_date=new Date().toString();
      //  BoardDB.addBoard(username, title, board_context, board_date);
       //////////////////////// BoardDB.IteminsertRecord(username, title, board_context, board_date);
        //Toast.makeText(getApplicationContext(), "게시글이 등록되었습니다.", Toast.LENGTH_LONG).show();
        //데이터베이스
       // boolean isOpen=openDatabase();
        //if(isOpen){
         //   selectAllDB();
       // }
       // String now=new Date().toString();
       // IteminsertRecord(username, title, board_context, now);

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "BoardRegister Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String username = jObj.getString("username");

                        JSONObject boardtable = jObj.getJSONObject("boardtable");
                        String title = boardtable.getString("title");
                        String board_context = boardtable.getString("board_context");
                        String board_date = boardtable.getString("board_date");

                        // Inserting row in users table
                     //   BoardDB.addBoard(username, title, board_context, board_date);

                        Toast.makeText(getApplicationContext(), "게시글이 등록되었습니다.", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        //Intent intent = new Intent(Boardwrite.this, BoardFragment.class);
                        // startActivity(intent);
                       // finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Board Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("title", title);
                params.put("board_context", board_context);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

/*
    //데이터베이스
    private boolean openDatabase(){
        println("opening database ["+tablename+"].");
        dbHelper=new DBHelper(this);
        db1=dbHelper.getWritableDatabase();
        return true;
    }


    //데이터베이스 안에 있는 값들을 가져와서 화면 갱신
    public Cursor selectAllDB() {
        c2 = db1.rawQuery("select username, title, board_context, board_date from " + tablename, null);
        for (int i = 0; i < c2.getCount(); i++) {
            c2.moveToNext();
            username1 = c2.getString(1);
            title1 = c2.getString(2);
            board_context = c2.getString(3);
            board_date = c2.getString(4);

        }

        /////
        c2.close();
        return c2;
    }


    //데이터베이스에 값들을 삽입하는 메소드
    public void IteminsertRecord(String username, String title, String board_context, String board_date) {
        println("inserting records using parameters.");
        int count=1;


        db1.execSQL("insert or ignore into " + tablename + " (username, title, board_context, board_date) " +
                "values " + "(" + username + ", " + title + ", " + board_context + ", "
                + board_date+");");

        Toast.makeText(getApplicationContext(), "insert", Toast.LENGTH_LONG).show();

    }*/
}

