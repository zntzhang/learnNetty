package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static void main(String[] args) {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();//客户端引导类
        //EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
        bootstrap.group(nioEventLoopGroup)//多线程处理
                .channel(NioSocketChannel.class)//制定通道类型为NioSocketChannel
                .handler(new ChannelInitializer<SocketChannel>() {//业务处理类
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());//注册handler
                    }
                });
        // 4.建立连接
        bootstrap.connect("127.0.0.1", 8000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败!");
            }
        });

    }
}