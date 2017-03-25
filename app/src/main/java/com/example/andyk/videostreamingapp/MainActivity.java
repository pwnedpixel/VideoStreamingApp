package com.example.andyk.videostreamingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button connectBtn,exitBtn,setupBtn,playBtn,pausebtn,teardownBtn;

    private EditText ipTxt,portTxt;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
        connectBtn = (Button) findViewById(R.id.connectBtn);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        setupBtn = (Button) findViewById(R.id.setupBtn);
        playBtn = (Button) findViewById(R.id.playBtn);
        pausebtn = (Button) findViewById(R.id.pauseBtn);
        teardownBtn = (Button) findViewById(R.id.teardownBtn);
        ipTxt = (EditText) findViewById(R.id.ipTxt);
        portTxt = (EditText) findViewById(R.id.portTxt);

        Intent intent = getIntent();

        connectBtn.setOnClickListener( v-> controller.connectButton(v));
        exitBtn.setOnClickListener(v->System.exit(1));
        setupBtn.setOnClickListener(v->controller.setupStream());
        playBtn.setOnClickListener(v->controller.playStream());
    }

}
