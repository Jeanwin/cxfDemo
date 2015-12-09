package com.zonekey.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	private static ServerSocket s;

	public static void main(String[] args) {
		try {
			s = new ServerSocket(8888);
			while (true) {
				Socket socket = s.accept();
				OutputStream os = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				dos.writeUTF("你好,客户端地址信息: " + socket.getInetAddress() + "\t客户端通信端口号: " + socket.getPort());
				dos.writeUTF("i'm a server ,my name is hongten！");
				dos.close();
				// 关闭打开的socket对象
				socket.close();
			}// 开始下一此循环
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}