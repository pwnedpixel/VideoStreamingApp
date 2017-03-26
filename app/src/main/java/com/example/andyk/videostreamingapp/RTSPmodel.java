package com.example.andyk.videostreamingapp;

import android.os.AsyncTask;
import android.util.Xml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * Created by andyk on 2017-03-24.
 */

public class RTSPmodel {
    InetAddress ipAddr;
    int port;
    Socket sockt;
    InetSocketAddress endPoint;

    public RTSPmodel(int cport, String cIP){
        port = cport;
        try {
            ipAddr = InetAddress.getByName(cIP);
        } catch (Exception e){
            System.err.println("Error parsing IP Address: "+e);
        }
    }

    void createSocket(){
        endPoint=new InetSocketAddress(ipAddr,port);
        try {
            sockt=new Socket();
        } catch (Exception e) {
            System.err.println("Error creating TCP socket: "+e);
        }
    }

    public boolean isConnected(){
        return sockt.isConnected();
    }

    void sendRequest(String req, SuccessHandler handler){
        new SendRequestTask(){
            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                handler.onComplete(response);
            }
        }.execute(req);
    }

    void openConnection(SuccessHandler handler){
        new SocketConnectTask(){
            @Override
            protected void onPostExecute(Void avoid) {
                super.onPostExecute(avoid);
                handler.onComplete();
            }
        }.execute();
    }

    private class SocketConnectTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                sockt.connect(endPoint,port);
                System.out.println("connected to server");
            } catch (Exception e){
                System.out.println("Error connecting with socket: "+e);
            }

            return null;
        }
    }

    private class SendRequestTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            byte[] message = new byte[4096];
            byte[] serverResponse = new byte[4096];
            try {
                message = params[0].getBytes("UTF-8");
            } catch(Exception e){
                System.err.println("Error encoding message: "+e);
            }

            //Sending the message
            try{
                DataOutputStream dOut = new DataOutputStream(sockt.getOutputStream());
                dOut.write(message);
                System.out.println("message Sent");
            } catch (Exception e){
                System.err.println("Error sending message: "+e);
            }

            //Receiving a response
            try{
                DataInputStream dIn = new DataInputStream(sockt.getInputStream());
                dIn.read(serverResponse);
                System.out.println("response received");
                return new String(serverResponse,"UTF-8");
            } catch (Exception e){
                System.err.println("Error receiving message: "+e);
                return "";
            }
        }
    }

    public interface SuccessHandler{
        void onComplete(final String... args);
    }



}
