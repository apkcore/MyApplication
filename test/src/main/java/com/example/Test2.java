package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Test2 {
    public static void main(String[] args) {
        test2();
//        multicastClient();
    }

    private static void test2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress group = null;
                try {
                    group = InetAddress.getByName("239.245.245.245");
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
                final int port = 8888;
                MulticastSocket socket = null;
                System.out.println("connect...");
                try {
                    byte[] buf = new byte[1000];
                    DatagramPacket recv = new DatagramPacket(buf, buf.length);
                    socket = new MulticastSocket(port);
                    socket.joinGroup(group);
                    while (true) {
                        socket.receive(recv);
                        String str = new String(recv.getData(), 0, recv.getLength(), "utf-8");
                        System.out.println(recv.getAddress() + "<><>" + str);
                        if (str.equals("data")) {
                            String ip, addre;
                            InetAddress addr = InetAddress.getLocalHost();
                            ip = addr.getHostAddress().toString();//获得本机IP
                            addre = addr.getHostName().toString();//获得本机名称
                            System.out.println("ip:" + ip + "\n" + addre);

                            try {
                                byte[] message = "Hello,This is Client.".getBytes(); //发送信息
//                                InetAddress inetAddress = InetAddress.getByName(groupHost); //组播地址
                                DatagramPacket datagramPacket = new DatagramPacket(message, message.length, group, port); //发送数据报
                                DatagramSocket socket1 = new DatagramSocket(); //DatagramSocket实例
                                socket1.send(datagramPacket); //发送数据
//                                socket.close(); //关闭端口
                            } catch (Exception exception) {
                                exception.printStackTrace();  //输出错误信息
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.leaveGroup(group);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        socket.close();

                    }
                }
            }
        }).start();
    }
    public static void multicastClient(){
        try{
            byte[] message = "Hello,This is Client.".getBytes(); //发送信息
            InetAddress inetAddress = InetAddress.getByName("239.245.245.245"); //组播地址
            DatagramPacket datagramPacket= new DatagramPacket(message, message.length, inetAddress, 8888); //发送数据报
            DatagramSocket socket = new DatagramSocket(); //DatagramSocket实例
            socket.send(datagramPacket); //发送数据
//            socket.close(); //关闭端口
        }
        catch (Exception exception) {
            exception.printStackTrace();  //输出错误信息
        }
    }
}
