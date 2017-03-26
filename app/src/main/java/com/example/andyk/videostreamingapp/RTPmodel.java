package com.example.andyk.videostreamingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by andyk on 2017-03-24.
 */

public class RTPmodel {

    DatagramSocket sockt;
    RTPpacket rtpPacket;
    InetSocketAddress endPoint;
    int port;
    InetAddress ipAddr;

    RTPmodel(int cport, String cIP){
        port = cport;
        try {
            ipAddr = InetAddress.getByName(cIP);
        } catch (Exception e){
            System.err.println("Error parsing IP Address: "+e);
        }
        rtpPacket = new RTPpacket();
    }

    void createSocket(){

        try {
            sockt=new DatagramSocket(25000);
            System.err.println("Created UDP socket");
        } catch (Exception e) {
            System.err.println("Error creating UDP socket: "+e);
        }
    }

    public void closeConnection(){
        sockt.close();
    }

    public void getFrame(RTPmodel.SuccessHandler handler){
        new GetFrameTask(){
            @Override
            protected void onPostExecute(Bitmap bm) {
                super.onPostExecute(bm);
                handler.onComplete(bm);
            }
        }.execute();
    }

    private class GetFrameTask extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            //System.err.println("Getting Frame...");
            byte[] message = new byte[100000];
            byte[] frame;
            //receive a response
            try {
                DatagramPacket receivePacket = new DatagramPacket(message, message.length);
                sockt.receive(receivePacket);
                //System.err.println("received frame packet");
                frame = rtpPacket.getFrame(message);
               // System.err.println("extracted frame");

                if (frame!=null){
                    return BitmapFactory.decodeByteArray(frame,0,frame.length);
                }
                return null;
            } catch (IOException e) {
                //System.err.println("Error reading frame.");
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface SuccessHandler{
        void onComplete(final Bitmap... args);
    }
}
