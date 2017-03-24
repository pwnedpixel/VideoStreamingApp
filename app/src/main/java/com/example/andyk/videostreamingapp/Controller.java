package com.example.andyk.videostreamingapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Timer;

/**
 * Created by andyk on 2017-03-24.
 */

public class Controller {
    private static View view;
    Context context;
    String session = "";
    int seqNumber;
    Timer tmr;
    boolean isPaused;
    boolean teardown;
    RTPmodel rtpModel;
    RTSPmodel rtspModel;

    public Controller(Context context){
        tmr = new Timer();
        this.context=context;
    }

    public void connectButton(View v){
        System.out.println("Connect Button Pressed");
        view = v;

        EditText aaa = (EditText) ((Activity)context).findViewById(R.id.portTxt);
        EditText bbb = (EditText) ((Activity)context).findViewById(R.id.ipTxt);
        rtspModel = new RTSPmodel(Integer.parseInt(aaa.getText().toString()),bbb.getText().toString());

        rtspModel.createSocket();
        if (rtspModel.openConnection()){
            System.out.println("Connected");
        } else {
            System.out.println("Error connecting");
        }
    }

}
