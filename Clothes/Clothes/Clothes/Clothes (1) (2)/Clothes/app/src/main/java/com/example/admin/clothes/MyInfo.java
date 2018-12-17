package com.example.admin.clothes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.clothes.model.SessionManager;

import java.util.HashMap;

public class MyInfo extends Activity {

    EditText age, height;
    Button modify, close, infomodify;
    TextView nickname, arm, leg, waist;
    Activity MyInfoactivity;
    public static TextView normalweight;
    public static EditText weight;
    public static Double normalweight1;
    private SQLiteHandler db;
    private SessionManager session;
    String name;
    String arm2, leg2, waist2, normalweight2;
    String Age, Height, Weight;
    private SharedPreferences appData;
    private boolean saveLoginData;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfolayout);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        MyInfoactivity=this;
        nickname = (TextView) findViewById(R.id.nickname);
        age = (EditText) findViewById(R.id.age);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);

        modify = (Button) findViewById(R.id.modify);
        close = (Button) findViewById(R.id.closeInfo);
        infomodify=(Button) findViewById(R.id.infomodify);

        checkBox = (CheckBox) findViewById(R.id.checkBox);


        arm = (EditText) findViewById(R.id.arm);
        leg = (EditText) findViewById(R.id.leg);
        waist = (EditText) findViewById(R.id.waist);
        arm.setEnabled(false);
        leg.setEnabled(false);
        waist.setEnabled(false);


        normalweight = (TextView) findViewById(R.id.normalweight);
/*
        Age = age.getText().toString();
        Height = height.getText().toString();
        Weight = weight.getText().toString();
        if(Age.equals("")){
            age.setText("20");
        }else{
            age.setText(Age);
        }
        if(Height.equals("")){
            height.setText("160");
        }else{
            height.setText(Height);
        }
        if(Weight.equals("")){
            weight.setText("50");
        }else{
            weight.setText(Weight);
        }*/

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            age.setText(Age);
            height.setText(Height);
            weight.setText(Weight);
            checkBox.setChecked(saveLoginData);
        }


        modify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String Nickname = nickname.getText().toString().trim();
                Age = age.getText().toString().trim();
                Height = height.getText().toString().trim();
                Weight = weight.getText().toString().trim();
                age.setText(Age);
                height.setText(Height);
                weight.setText(Weight);

                if (!Nickname.isEmpty() && !Age.isEmpty() && !Height.isEmpty() && !Weight.isEmpty()) {
                    modify.setText("정보 수정하기");
                    Float arm1, leg1;
                    Double waist1;


                    arm1 = Float.parseFloat(height.getText().toString()) / 3;
                    arm2 = String.format("%.1f", arm1);
                    arm.setText(arm2);

                    leg1 = Float.parseFloat(height.getText().toString()) * 100 * 46 / 10000;
                    leg2 = String.format("%.1f", leg1);
                    leg.setText(leg2);

                    waist1 = Double.parseDouble(height.getText().toString()) / 2.7;
                    waist2 = String.format("%.1f", waist1);
                    waist.setText(waist2);

                    normalweight1 = (Double.parseDouble(height.getText().toString()) - 100) * 0.9;
                    normalweight2 = String.format("%.1f", normalweight1);
                    normalweight.setText(normalweight2 );

                    //세션 저장
                    save();

                    // Intent i = new Intent(Myinfo.class, SettingFragment.class);
                    // i.putExtra("Nickname", nickname.getText().toString());
                    // startActivity(i);
/*
                    Bundle bundle = new Bundle();
                    bundle.putString("Nickname1", Nickname);
                    SettingFragment fragInfo = new SettingFragment();
                    fragInfo.setArguments(bundle);
*/
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "모든 정보를 입력해 주세요!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        infomodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arm.isEnabled()==false){
                    arm.setEnabled(true);
                    leg.setEnabled(true);
                    waist.setEnabled(true);
                    infomodify.setText("확인");
                }else{
                    arm.setEnabled(false);
                    leg.setEnabled(false);
                    waist.setEnabled(false);
                    infomodify.setText("치수 수정하기");
                }
            }
        });

        // SqLite database handler
        db = new SQLiteHandler(this);
        // session manager
        session = new SessionManager(this);
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        nickname.setText(name);

    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("age", age.getText().toString().trim());
        editor.putString("height", height.getText().toString().trim());
        editor.putString("weight", weight.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        Age = appData.getString("age", "");
        Height = appData.getString("height", "");
        Weight=appData.getString("weight", "");
    }


}