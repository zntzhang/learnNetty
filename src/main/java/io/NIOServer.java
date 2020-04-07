package io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
    //通道通信
    private Selector selector;

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     *
     * @param port 绑定的端口号
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        //获得一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverChannel.configureBlocking(false);
        //将通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        //获得一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector是否有需要处理的事件，如果有，则进行处理
     *
     * @throws IOException
     */
    public void listen() throws IOException {
        System.out.println("服务端启动成功！");
        //轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则，该方法会一直阻塞
            selector.select();
            //获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                if (key.isAcceptable()) {//客户端请求连接事件
                    handlerAccept(key);
                    //删除已选的key，以防重复处理
                    ite.remove();
                } else if (key.isReadable()) {//获得了可读的事件
                    handlerRead(key);//如果第二个连接来了，遍历时，第一个连接的可读消息是空的也不会阻塞
                }
            }
        }
    }


    /**
     * 处理连接请求
     *
     * @param key
     * @throws IOException
     */
    private void handlerAccept(SelectionKey key) throws IOException {
        //获得ServerSocket
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        //获得和客户端连接的通道
        SocketChannel channel = server.accept();
        //设置成非阻塞
        channel.configureBlocking(false);

        System.out.println("新的客户端连接");
        //服务端发给客户端的确认信息
        channel.write(ByteBuffer.wrap("服务端成功创建连接".getBytes()));
        //在和客户端连接成功后，为了可以接收到客户端的信息，需要给通道设置读的权限
        channel.register(this.selector, SelectionKey.OP_READ);
    }


    /**
     * 处理可读的事件
     *
     * @param key
     * @throws IOException
     */
    private void handlerRead(SelectionKey key) throws IOException {
        //得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        //创建读取的缓冲区(每次读1000个字节)
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        byte[] data = buffer.array();
        String msg = new String(data).trim();
        System.out.println("服务端收到信息：" + msg);

        ByteBuffer outBuffer = ByteBuffer.wrap(("服务端收到信息：" + msg).getBytes());
        channel.write(outBuffer);//将消息送回给客户端
    }

    /**
     * 启动服务测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.initServer(8002);
        server.listen();
    }

}
