package com.soecode.lyf.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ClientDemo {
    private final ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    private final ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

    private Selector selector;

    public ClientDemo() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(),8080));
        socketChannel.configureBlocking(false);
        System.out.println("与服务器建立连接成功");
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) throws Exception{
        final ClientDemo client=new ClientDemo();
        Thread receiver=new Thread(client::receiveFromUser);

        receiver.start();
        client.talk();
    }

    private void talk() throws IOException {
        while(selector.select() > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isReadable()){
                    receive(selectionKey);
                }

                // 实际上只要注册了关心写操作，这个操作就一直被激活
                if (selectionKey.isWritable()) {
                    send(selectionKey);
                }
            }
        }
    }

    private void send(SelectionKey key)throws IOException{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        synchronized(sendBuffer){
            sendBuffer.flip(); //设置写
            while(sendBuffer.hasRemaining()){
                socketChannel.write(sendBuffer);
            }
            sendBuffer.compact();
        }
    }
    private void receive(SelectionKey key)throws IOException{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        receiveBuffer.clear();
        socketChannel.read(receiveBuffer);
        String receiveData= Charset.forName("UTF-8").decode(receiveBuffer).toString();

        System.out.println("receive server message:"+receiveData);
        receiveBuffer.flip();
    }

    private void receiveFromUser() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            String msg;
            while ((msg = bufferedReader.readLine()) != null){
                synchronized(sendBuffer){
                    sendBuffer.put((msg+"\r\n").getBytes());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
