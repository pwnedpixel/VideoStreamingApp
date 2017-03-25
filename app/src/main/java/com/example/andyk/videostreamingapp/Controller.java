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
        rtspModel.openConnection(args -> {

        });

    }

    public void setupStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();

        seqNumber = 2;

        //build the request
        String req = "SETUP rtsp://"+ipAddr+":"+portNum+"/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nTransport: RTP/UDP; client_port= 2000\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                char[] splitBy = { '\r', '\n', ' ', ',', ';', ':', '/' };
                String[] segments = res.split("(\\n)(\\r)( )(,)(;)(:)(/)");
                System.out.println(res);
            }
        });
    }
}
