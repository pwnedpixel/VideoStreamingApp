package com.example.andyk.videostreamingapp;

import android.os.AsyncTask;

import java.io.DataInput;
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
    RTPpacket packet;
    InetSocketAddress endPoint;
    int port;
    InetAddress ipAddr;

    public RTPmodel(int cport, String cIP){
        port = cport;
        try {
            ipAddr = InetAddress.getByName(cIP);
        } catch (Exception e){
            System.err.println("Error parsing IP Address: "+e);
        }
        packet = new RTPpacket();
    }

    public void createSocket(){
        endPoint=new InetSocketAddress(ipAddr,25000);
        try {
            sockt=new DatagramSocket(25000);
           // sockt.bind(endPoint);
        } catch (Exception e) {
            System.err.println("Error creating socket: "+e);
        }
    }

    public void closeConnection(){
        sockt.close();
    }

    public void getFrame(RTPmodel.SuccessHandler handler){
        new GetFrameTask(){
            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                handler.onComplete(bytes);
            }
        }.execute();
    }

    public class GetFrameTask extends AsyncTask<Void, Integer, byte[]> {

        @Override
        protected byte[] doInBackground(Void... params) {
            System.err.println("Getting Frame...");
            byte[] message = new byte[100000];
            byte[] frame;
            //receive a response
            DatagramPacket receivePacket = new DatagramPacket(message,message.length, ipAddr,port);
            try {
                sockt.receive(receivePacket);
                System.err.println("received frame packet");
                frame = packet.getFrame(receivePacket.getData());
                System.err.println("extracted frame");
                return frame;
            } catch (IOException e) {
                System.err.println("Error reading frame.");
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface SuccessHandler{
        public void onComplete(final byte[]... args);
    }
}
