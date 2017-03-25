package com.example.andyk.videostreamingapp;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by andyk on 2017-03-24.
 */

public class RTPpacket {
    byte[] header;

    public RTPpacket(){
        header = new byte[12];
    }

    public byte[] getFrame(byte[] data){
        byte[] frame = new byte[99988];

        for (int i =0;i<12;i++){
            header[i]=data[i];
        }

        for (int i = 12;i<data.length;i++){
            frame[i-12] = data[i];
        }
        return frame;
    }

    public String getHeader(){
        try {
            return new String(header,"UTF-8")+"\\r\\n";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
