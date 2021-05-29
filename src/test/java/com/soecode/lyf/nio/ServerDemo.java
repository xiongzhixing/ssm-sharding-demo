package com.soecode.lyf.nio;

import com.mysql.fabric.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketImpl;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ServerDemo {
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    private Selector selector;

    public ServerDemo() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8080));
        System.out.println("listening on port 8080");


        this.selector = Selector.open();
        //绑定channel的accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public static void main(String[] args) throws IOException {
        new ServerDemo().go();
    }

    public void go() throws IOException {
        while(selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()){
                    //有新的连接
                    System.out.println("服务端有新的连接建立");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();

                    //新注册channel监听读写事件
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if(socketChannel == null){
                        continue;
                    }
                    socketChannel.configureBlocking(false);

                    // 注意！这里和阻塞io的区别非常大，在编码层面之前的等待输入已经变成了注册事件，这样我们就可以在等待的时候做别的事情，
                    // 比如监听更多的socket连接，也就是之前说了一个线程监听多个socket连接。这也是在编码的时候最直观的感受
                    socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put("hi new channel".getBytes());
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                }

                // 服务端关心的可读，意味着有数据从client传来了，根据不同的需要进行读取，然后返回
                if(selectionKey.isReadable()){
                    System.out.println("isReadable");
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    readBuffer.clear();
                    socketChannel.read(readBuffer);
                    readBuffer.flip();

                    String messsage = Charset.forName("UTF-8").decode(readBuffer).toString();
                    System.out.println("message:" + messsage);

                    //把读到的数据绑定到key中
                    selectionKey.attach("server message echo:" +messsage);
                }

                // 实际上服务端不在意这个，这个写入应该是client端关心的，这只是个demo,顺便试一下selectionKey的attach方法
                if(selectionKey.isWritable()){
                    System.out.println("isWritable");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    String message= (String)selectionKey.attachment();
                    System.out.println("attachment:" + message);
                    message = "啥都没有";
                    /*if(message == null){
                        continue;
                    }*/
                    //selectionKey.attach(null);

                    writeBuffer.clear();
                    writeBuffer.put(message.getBytes());
                    writeBuffer.flip();
                    while(writeBuffer.hasRemaining()){
                        socketChannel.write(writeBuffer);
                    }
                }
            }
        }
    }
}
