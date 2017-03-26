package com.example.andyk.videostreamingapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by andyk on 2017-03-24.
 */

public class Controller {
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

    public void connectButton(){
        Button playBtn = ((Button) ((Activity)context).findViewById(R.id.playBtn));
        Button pauseBtn = ((Button) ((Activity)context).findViewById(R.id.pauseBtn));
        Button teardownBtn = ((Button) ((Activity)context).findViewById(R.id.teardownBtn));
        Button setupBtn = ((Button) ((Activity)context).findViewById(R.id.setupBtn));
        System.out.println("Connect Button Pressed");

        EditText port = (EditText) ((Activity)context).findViewById(R.id.portTxt);
        EditText ip = (EditText) ((Activity)context).findViewById(R.id.ipTxt);
        rtspModel = new RTSPmodel(Integer.parseInt(port.getText().toString()),ip.getText().toString());

        rtspModel.createSocket();
        rtspModel.openConnection(args -> {
            System.out.println("connection opened");
            if (rtspModel.isConnected()){
                setupBtn.setEnabled(true);
                playBtn.setEnabled(false);
                pauseBtn.setEnabled(false);
                teardownBtn.setEnabled(true);
            }
        });

    }

    public void setupStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();
        Button playBtn = ((Button) ((Activity)context).findViewById(R.id.playBtn));
        Button pauseBtn = ((Button) ((Activity)context).findViewById(R.id.pauseBtn));
        Button teardownBtn = ((Button) ((Activity)context).findViewById(R.id.teardownBtn));
        Button setupBtn = ((Button) ((Activity)context).findViewById(R.id.setupBtn));

        seqNumber = 2;

        //build the request
        String req = "SETUP rtsp://"+ipAddr+":"+portNum+"/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nTransport: RTP/UDP; client_port= 25000\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                String[] segments = res.split("[\\s\\n\\r +,;:/(\0)]");
                session = segments[11];
                rtpModel = new RTPmodel(Integer.parseInt(portNum),ipAddr);
                rtpModel.createSocket();
                isPaused = false;
                teardown = false;

                setupBtn.setEnabled(false);
                playBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
                teardownBtn.setEnabled(true);
            }
        });
    }

    public void playStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();
        Button playBtn = ((Button) ((Activity)context).findViewById(R.id.playBtn));
        Button pauseBtn = ((Button) ((Activity)context).findViewById(R.id.pauseBtn));
        Button teardownBtn = ((Button) ((Activity)context).findViewById(R.id.teardownBtn));
        Button setupBtn = ((Button) ((Activity)context).findViewById(R.id.setupBtn));
        seqNumber++;
        isPaused = false;
        teardown = false;

        //build the request
        String req = "PLAY rtsp://" + ipAddr + ":" + portNum + "/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nSession:  "+session+"\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                setupBtn.setEnabled(false);
                playBtn.setEnabled(false);
                pauseBtn.setEnabled(true);
                teardownBtn.setEnabled(true);
                timerTick();
            }
        });
    }

    public void pauseStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();
        Button playBtn = ((Button) ((Activity)context).findViewById(R.id.playBtn));
        Button pauseBtn = ((Button) ((Activity)context).findViewById(R.id.pauseBtn));
        Button teardownBtn = ((Button) ((Activity)context).findViewById(R.id.teardownBtn));
        Button setupBtn = ((Button) ((Activity)context).findViewById(R.id.setupBtn));
        seqNumber++;

        String req = "PAUSE rtsp://" + ipAddr + ":" + portNum + "/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nSession:  " + session + "\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                System.out.println("PausedPlayback");
                setupBtn.setEnabled(false);
                playBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
                teardownBtn.setEnabled(true);
            }
        });
        isPaused = true;
        teardown = false;
    }

    public void teardownStream(){
        String portNum = ((EditText) ((Activity)context).findViewById(R.id.portTxt)).getText().toString();
        String ipAddr =((EditText) ((Activity)context).findViewById(R.id.ipTxt)).getText().toString();
        Button playBtn = ((Button) ((Activity)context).findViewById(R.id.playBtn));
        Button pauseBtn = ((Button) ((Activity)context).findViewById(R.id.pauseBtn));
        Button teardownBtn = ((Button) ((Activity)context).findViewById(R.id.teardownBtn));
        Button setupBtn = ((Button) ((Activity)context).findViewById(R.id.setupBtn));
        seqNumber++;

        String req = "TEARDOWN rtsp://" + ipAddr + ":" + portNum + "/video3.mjpeg RTSP/1.0\r\nCSeq: " +
                seqNumber + "\r\nSession:  " + session + "\r\n";
        rtspModel.sendRequest(req, args -> {
            String res = args[0].toString();
            if (!res.equals("")){
                rtpModel.closeConnection();
                System.out.println("Teardown Complete");
                setupBtn.setEnabled(true);
                playBtn.setEnabled(false);
                pauseBtn.setEnabled(false);
                teardownBtn.setEnabled(false);
            }
        });
        isPaused = false;
        teardown = true;
    }

    private void timerTick() {
        ImageView im = ((ImageView) ((Activity) context).findViewById(R.id.imageView2));

        if (!isPaused && !teardown) {
            //get the image from the rtpmodel
            rtpModel.getFrame(args -> {
                im.setImageBitmap(args[0]);
                tmr.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timerTick();
                    }
                },30);
            });

        }
    }
}
