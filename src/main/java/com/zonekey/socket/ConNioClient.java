package com.zonekey.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ConNioClient {

	public static void main(String[] args) throws IOException {
		// 线程总数 Threadsum * 111
		int threadSum = 5;
		for (int i = 0; i < threadSum; i++) {
			new Thread(new ClientThread(2)).start();
		}

	}

	public static class ClientThread implements Runnable {

		public static long timeoutCount = 0;
		private static int idleCounter = 0;
		private Selector selector;
		private SocketChannel socketChannel;
		private ByteBuffer temp = ByteBuffer.allocate(1024);
		private int deep;

		public ClientThread(int deep) {
			this.deep = deep;
			// 同样的,注册闹钟.
			try {
				this.selector = Selector.open();
				// 连接远程server
				socketChannel = SocketChannel.open();
				// 如果快速的建立了连接,返回true.如果没有建立,则返回false,并在连接后出发Connect事件.
				Boolean isConnected = socketChannel.connect(new InetSocketAddress("localhost", 7878));
				socketChannel.configureBlocking(false);

				SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);

				if (!isConnected) {
					// 如果连接还在尝试中,则注册connect事件的监听. connect成功以后会出发connect事件.
					key.interestOps(SelectionKey.OP_CONNECT);
				}
			} catch (IOException e) {
				System.out.println(Thread.currentThread().getName() + " 连接异常  : #" + (++timeoutCount));
			}

		}

		@Override
		public void run() {
			// 模拟并发
			if (deep > 0) {
				for (int i = 0; i < 10; i++) {
					new Thread(new ClientThread(deep - 1)).start();
				}
			}
			while (true) {
				try {
					// 阻塞,等待事件发生,或者1秒超时. num为发生事件的数量.
					int num = this.selector.select(5000);
					if (num == 0) {
						idleCounter++;
						if (idleCounter > 2) {
							// System.out.println("连接超时，线程 : "+Thread.currentThread().getName()+" 退出 #"+(++timeoutCount));
							return;
						}
						continue;
					}
					Set<SelectionKey> keys = this.selector.selectedKeys();
					Iterator<SelectionKey> it = keys.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						if (key.isConnectable()) {
							// socket connected
							SocketChannel sc = (SocketChannel) key.channel();
							if (sc.isConnectionPending()) {
								sc.finishConnect();
							}
						}
						// 接收到消息
						if (key.isReadable()) {
							// msg received.
							SocketChannel sc = (SocketChannel) key.channel();
							this.temp = ByteBuffer.allocate(1024);
							int count = sc.read(temp);
							if (count < 0) {
								sc.close();
								continue;
							}
							// 切换buffer到读状态,内部指针归位.
							temp.flip();
							String msg = Charset.forName("UTF-8").decode(temp).toString();
							System.out.println(Thread.currentThread().getName() + "   Client received [" + msg + "] ");
							return;
						}
					}
				} catch (IOException e) {
					System.out.println("远程主机已关闭，程序退出！");
					return;
				}

			}
		}

	}

}