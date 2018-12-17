package com.example.admin.clothes.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.clothes.MyHeart;
import com.example.admin.clothes.MyInfo;
import com.example.admin.clothes.MyModel;
import com.example.admin.clothes.R;
import com.example.admin.clothes.SQLiteHandler;
import com.example.admin.clothes.model.SessionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class SettingFragment extends Fragment{
    Button myinfo, heart, mymodel;
    ImageButton selfImage;
    Bitmap selfbitmap;
    final int REQ_CODE_SELECT_IMAGE=100;
    TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

    String name, email;
    //String nickname1;

    public SettingFragment() {
        // Required empty public constructor

    }

    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    //   super.onCreate(savedInstanceState);
    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Bundle bundle = this.getArguments();
        /*if(savedInstanceState != null)
        {
            nickname1 = getArguments().getString("Nickname1");
        }*/
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.setting, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        selfImage=(ImageButton)getView().findViewById(R.id.selfImage);
        myinfo=(Button)getView().findViewById(R.id.myinfo);
        heart=(Button)getView().findViewById(R.id.heart);
        mymodel=(Button)getView().findViewById(R.id.mymodel);
        selfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage();
            }
        });
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), MyInfo.class);
                startActivity(intent);
            }
        });


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), MyHeart.class);
                startActivity(intent);
            }
        });

        mymodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), MyModel.class);
                startActivity(intent);
            }
        });

        LogoutEvent();
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
                    selfImage.setBackground(selfdrawable);
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

    //프로필 이미지 변경
    private void changeImage(){
        final AlertDialog.Builder alt=new AlertDialog.Builder(getActivity());
        alt.setMessage("프로필 사진을 변경하시겠습니까?").setCancelable(false).setPositiveButton("예",
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
        alert.setTitle("프로필 사진 변경");
        alert.setIcon(R.drawable.icon);
        alert.show();
    }
    //bitmap--> drawable
    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        return d;
    }

    public void LogoutEvent() {
        txtName = (TextView) getView().findViewById(R.id.name);
        txtEmail = (TextView) getView().findViewById(R.id.email);
        btnLogout = (Button) getView().findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getContext().getApplicationContext());

        // session manager
        session = new SessionManager(getContext().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        email = user.get("email");

        /*if(nickname1 != ""){
            txtName.setText(nickname1);
        } else
            txtName.setText(name);*/

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        //txtName.setText(AppController.userName);
        //txtEmail.setText(AppController.userEmail);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
                Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        selfImage.invalidate();
        //btnLogout.setText("로그아웃");
        //txtName.setText(name);
        //txtEmail.setText(email);
    }
}