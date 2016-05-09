package com.neucrack.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.neucrack.tool.Session;


public class ServerHost {
	
	private static ArrayList<ServerToDeviceThead> mSocketList = new ArrayList<ServerToDeviceThead>();
	
	/*
	 * 基于TCP协议的Socket通信，实现用户登录 服务器端
	 */
	public static void main(String[] args) {
		
			Session session = new Session();
			session.EnableAutoClear();
			
			//开启一个线程监视用户的请求
			 new Thread(){

				@Override
				public void run() {
					try{
						// 1.创建一个服务器端Socket,即ServerSocket,指定绑定的端口，并监听此端口
						ServerSocket serverSocket = new ServerSocket(8099);
						Socket socket = null;
	
						// 2.调用accept()方法开始监听，等待客户端的连接
						System.out.println("Server Started，waiting for User request at port 8099: ");
						while (true) {// 循环监听等待客户端的连接
							socket = serverSocket.accept();// 调用accept()方法开始监听，等待客户端的连接
							ServerToUserThread userThread = new ServerToUserThread(mSocketList,socket);// 创建一个新的线程响应客户端的连接
							userThread.start();// 启动线程
							System.out.println("用户发起请求，地址："+socket.getInetAddress());
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				 
			 }.start();
			
			//监视设备的连接
		try {
			// 1.创建一个服务器端Socket,即ServerSocket,指定绑定的端口，并监听此端口
			ServerSocket serverSocket = new ServerSocket(8090);
			Socket socket = null;

			// 2.调用accept()方法开始监听，等待客户端的连接
			System.out.println("Server Started，waiting for devices connect at port 8090:");
			while (true) {// 循环监听等待客户端的连接
				socket = serverSocket.accept();// 调用accept()方法开始监听，等待客户端的连接
				ServerToDeviceThead serverThread = new ServerToDeviceThead(socket);// 创建一个新的线程响应客户端的连接
				serverThread.start();// 启动线程
				mSocketList.add(serverThread);
				DeleteOfflineDevices();//删除掉已经掉线了的设备
				showInfo(socket);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showInfo(Socket socket) {
		System.out.println("已连接设备数量：" + mSocketList.size());
		InetAddress address = socket.getInetAddress();
		System.out.println("新增网关IP地址：" + address.getHostAddress());
	}
	public static void DeleteOfflineDevices(){
		for (int i = 0;i<mSocketList.size();++i) {
			if(!mSocketList.get(i).IsAlive())//已经关闭连接了，可以移除
				mSocketList.remove(i);
		}
	}
}
