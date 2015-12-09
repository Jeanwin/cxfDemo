package com.zonekey.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConNioServer {
    public static ArrayList<SelectorLoop> connectionBellList = new ArrayList<>();
    public boolean isReadBellRunning = false;
    public long requestCount = 0;
    public static ServerSocketChannel ssc;
    public static ExecutorService executorService = Executors
            .newFixedThreadPool(30);
 
    public static void main(String[] args) throws IOException {
        new ConNioServer().startServer();
 
    }
 
    // 启动服务器
    public void startServer() throws IOException {
 
        // 准备好闹钟.当有链接进来的时候响.
        for ( int i = 0; i < 5;i++ ){
            SelectorLoop selectorLoop = new SelectorLoop();
            connectionBellList.add(selectorLoop);
        }
        // 开启一个server channel来监听
        ssc = ServerSocketChannel.open();
 
        // 开启非阻塞模式
        ssc.configureBlocking(false);
        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress("localhost", 7878));
        // 给闹钟规定好要监听报告的事件,这个闹钟只监听新连接事件.
 
        for ( int i = 0; i < connectionBellList.size();i++ ){
            ssc.register(connectionBellList.get(i).getSelector(), SelectionKey.OP_ACCEPT);
            new Thread(connectionBellList.get(i)).start();
        }
    }
 
    public static class SendThread implements Runnable {
        private SocketChannel sc;
        private ByteBuffer temp;
        private String msg;
        //private long count;
 
        /**
         * @param sc
         * @param msg
         */
        public SendThread(SocketChannel sc, String msg) {
            this.sc = sc;
            this.temp = ByteBuffer.allocate(1024);
            this.msg = msg;
        }
 
        @Override
        public void run() {
            try {
                sc.write(ByteBuffer.wrap(msg.getBytes(Charset.forName("UTF-8"))));
                // 清空buffer
                temp.clear();
            } catch (IOException e) {
                System.out.println("channel is null");
            } catch (Exception e) {
                System.out.println("通道异常");
            }
 
        }
    }
 
    // Selector轮询线程类
 
    public static class SelectorLoop implements Runnable {
 
        private Selector selector;
        //private ByteBuffer temp = ByteBuffer.allocate(1024);
        private String msg = "this is a message...";
        public static long requsts = 0;
 
        public SelectorLoop() throws IOException {
 
            this.selector = Selector.open();
        }
 
        public Selector getSelector() {
            return this.selector;
        }
        public static synchronized long addCount() {
            return ++requsts;
        }
        @Override
        public void run() {
            System.out.println("Server start 。。。。 ");
            while (true) {
                Iterator<SelectionKey> it;
                try {
                    // 阻塞,只有当至少一个注册的事件发生的时候才会继续.
                    synchronized (ssc) {
                        this.selector.select();
                    }
                        // 获取事件
                        Set<SelectionKey> selectKeys = this.selector.selectedKeys();
                        it = selectKeys.iterator();
                        System.out.println("connection : " + addCount());
                     
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        // 处理事件. 可以用多线程来处理.
                        if (key.isAcceptable()) {
 
                            // 这是一个connection accept事件,
                            // 并且这个事件是注册在serversocketchannel上的.
                            ServerSocketChannel ssc = (ServerSocketChannel) key
                                    .channel();
                            // 接受一个连接.
                            SocketChannel sc = ssc.accept();
                            // 在线程池中执行数据传输
                            executorService.execute(new SendThread(sc, msg));
                            this.selector.wakeup();
                            // new Thread(new SendThread(sc,
                            // msg,++requestCount)).start();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("存在客户端强制断开连接断开连接！");
                }
            }
        }
 
    }
}