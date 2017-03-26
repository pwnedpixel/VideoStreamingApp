package com.example.andyk.videostreamingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button connectBtn,exitBtn,setupBtn,playBtn,pauseBtn,teardownBtn;

    EditText ipTxt,portTxt;

    Spinner spinner;

    ImageButton imageSetup,imagePlay,imagePause,imageTeardown;

    ImageView imageView2;

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
        imageSetup = (ImageButton) findViewById(R.id.imageSetup);
        imagePlay = (ImageButton) findViewById(R.id.imagePlay);
        imagePause = (ImageButton) findViewById(R.id.imagePause);
        imageTeardown = (ImageButton) findViewById(R.id.imageTeardown);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        Intent intent = getIntent();

        connectBtn.setOnClickListener(v-> controller.connectButton());
        exitBtn.setOnClickListener(v->System.exit(1));
        setupBtn.setOnClickListener(v->controller.setupStream());
        playBtn.setOnClickListener(v->controller.playStream());
        pauseBtn.setOnClickListener(v->controller.pauseStream());
        teardownBtn.setOnClickListener(v->controller.teardownStream());
        imageSetup.setOnClickListener(v->controller.setupStream());
        imagePlay.setOnClickListener(v->controller.playStream());
        imagePause.setOnClickListener(v->controller.pauseStream());
        imageTeardown.setOnClickListener(v->controller.teardownStream());
        imageView2.setOnClickListener(v->controller.showButtons());


    }

}
