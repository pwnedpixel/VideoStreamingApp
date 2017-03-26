package com.example.andyk.videostreamingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button connectBtn,exitBtn,setupBtn,playBtn,pauseBtn,teardownBtn;

    EditText ipTxt,portTxt;

    Spinner spinner;

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
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        teardownBtn = (Button) findViewById(R.id.teardownBtn);
        ipTxt = (EditText) findViewById(R.id.ipTxt);
        portTxt = (EditText) findViewById(R.id.portTxt);
        spinner = (Spinner) findViewById(R.id.spinner);

        Intent intent = getIntent();

        connectBtn.setOnClickListener(v-> controller.connectButton());
        exitBtn.setOnClickListener(v->System.exit(1));
        setupBtn.setOnClickListener(v->controller.setupStream());
        playBtn.setOnClickListener(v->controller.playStream());
        pauseBtn.setOnClickListener(v->controller.pauseStream());
        teardownBtn.setOnClickListener(v->controller.teardownStream());
    }

    public void test(){

    }

}
