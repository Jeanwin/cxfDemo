package com.zonekey.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MultiThreadClient {
	private static Socket s1;

	public static void main(String args[]) {
		try {
			s1 = new Socket("127.0.0.1", 12345);

			// =========客户端，在这里应该先打开输入流，在打开输出流，
			// =========因为客户端执行的操作是先说，再听，说，听，说，听.....

			// 打开输入流
			InputStream is = s1.getInputStream();
			// 封装输入流
			DataInputStream dis = new DataInputStream(is);
			// 打开输出流
			OutputStream os = s1.getOutputStream();
			// 封装输出流
			DataOutputStream dos = new DataOutputStream(os);

			// 创建并启用两个线程
			new MyClientReader(dis).start();
			new MyClientWriter(dos).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// 接受并打印服务器端传过来的信息
class MyClientReader extends Thread {
	private DataInputStream dis;

	public MyClientReader(DataInputStream dis) {
		this.dis = dis;
	}

	public void run() {
		String info;
		try {
			while (true) {
				info = dis.readUTF();
				System.out.println("对方说: " + info);
				if (info.equals("bye")) {
					System.out.println("对方下线，程序退出!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// 从键盘获得输入流并写入信息到服务器端
class MyClientWriter extends Thread {
	private DataOutputStream dos;

	public MyClientWriter(DataOutputStream dos) {
		this.dos = dos;
	}

	public void run() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String info;
		try {
			while (true) {
				info = br.readLine();
				dos.writeUTF(info);
				if (info.equals("bye")) {
					System.out.println("自己下线，程序退出!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
