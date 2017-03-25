package com.example.andyk.videostreamingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

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
            System.out.println("hi");
        });

    }

    public void setupStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();

        seqNumber = 2;

        //build the request
        String req = "SETUP rtsp://"+ipAddr+":"+portNum+"/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nTransport: RTP/UDP; client_port= 25000\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                char[] splitBy = { '\r', '\n', ' ', ',', ';', ':', '/' };
//                String[] segments = res.split("(\\n)(\\r)( )(,)(;)(:)(/)");
                String[] segments = res.split("[\\s\\n\\r +,;:/(\0)]");
                System.out.println("PREPRINT");
                System.out.println("segments size: "+segments.length);
                System.out.println(segments[11]);
                System.out.println("POSTPRINT");
                session = segments[11];

                rtpModel = new RTPmodel(Integer.parseInt(portNum),ipAddr);
                rtpModel.createSocket();
                isPaused = false;
                teardown = false;
            }
        });
    }

    public void playStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();
        seqNumber++;

        //build the request
        String req = "PLAY rtsp://" + ipAddr + ":" + portNum + "/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nSession:  "+session+"\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                tmr .schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timerTick();
                    }
                },0,1000);
                isPaused = false;
                teardown = false;
            }
        });
    }

    public void timerTick(){
        ImageView im = ((ImageView) ((Activity)context).findViewById(R.id.imageView2));
        System.out.println("tick");
        //array to hold the frame to play
        byte[] nextFrame = new byte[100000];

        //get the frame from the rtpmodel
        rtpModel.getFrame(args -> {
            if (nextFrame != null && isPaused == false){
                ByteArrayInputStream iStream = new ByteArrayInputStream(nextFrame);
                Bitmap bm = BitmapFactory.decodeStream(iStream);
                im.setImageBitmap(bm);
                System.out.println("Image Set");
            } else {
                System.out.println("Image not set: "+nextFrame!=null+" "+isPaused);
            }
        });
    }
}
