package com.example.admin.clothes;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyFriend extends Activity {

    Button closefriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlayout);

        closefriend=(Button)findViewById(R.id.closefriend);

        closefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
