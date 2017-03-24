package com.example.andyk.videostreamingapp;

import android.util.Xml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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

    public void createSocket(){
        endPoint=new InetSocketAddress(ipAddr,port);
        try {
            sockt=new Socket(ipAddr,port);
        } catch (IOException e) {
            System.err.println("Error creating socket: "+e);
        }
    }

    public boolean openConnection(){
        try{
            sockt.connect(endPoint,3000);
        } catch (Exception e){
            System.out.println("Error connecting with socket: "+e);
        }
        return sockt.isConnected();
    }

    public String sendRequest(String req){
        byte[] message = new byte[4096];
        byte[] serverResponse = new byte[4096];
        try {
            message = req.getBytes("UTF8");
        } catch(Exception e){
            System.err.println("Error encoding message: "+e);
        }

        //Sending the message
        try{
            DataOutputStream dOut = new DataOutputStream(sockt.getOutputStream());
            dOut.write(message);
        } catch (Exception e){
            System.err.println("Error sending message: "+e);
        }

        //Receiving a response
        try{
            DataInputStream dIn = new DataInputStream(sockt.getInputStream());
            dIn.read(serverResponse);
            return new String(serverResponse,"UTF-8");
        } catch (Exception e){
            System.err.println("Error receiving message: "+e);
            return "";
        }
    }
}
