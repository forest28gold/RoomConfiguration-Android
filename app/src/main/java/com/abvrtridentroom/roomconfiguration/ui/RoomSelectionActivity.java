package com.abvrtridentroom.roomconfiguration.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.abvrtridentroom.roomconfiguration.R;

public class RoomSelectionActivity extends AppCompatActivity {

    private final static String TAG = "RoomSelectionActivity";

    Boolean toggleIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room_selection);

        ImageButton btn_back = (ImageButton)findViewById(R.id.imageButton_logo);
        ImageButton btn_room1 = (ImageButton)findViewById(R.id.imageButton_room1);
        ImageButton btn_room2 = (ImageButton)findViewById(R.id.imageButton_room2);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toggleIsOn) {
                    toggleIsOn = true;
                    Intent intent = new Intent(RoomSelectionActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toggleIsOn) {
                    toggleIsOn = true;
                    Intent intent = new Intent(RoomSelectionActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        toggleIsOn = false;
    }
}
