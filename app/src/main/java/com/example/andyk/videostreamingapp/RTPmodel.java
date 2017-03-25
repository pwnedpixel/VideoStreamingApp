package com.example.andyk.videostreamingapp;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by andyk on 2017-03-24.
 */

public class RTPmodel {
    Socket sockt;
    RTPpacket packet;
    InetSocketAddress endPoint;
    int port;
    InetAddress ipAddr;
}
