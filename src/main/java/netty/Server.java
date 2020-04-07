package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) {
        // bossGroup表示监听端口，accept 新连接的线程组，workerGroup表示处理每一条连接的数据读写的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //server端引导类，来引导绑定和启动服务器；
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //装配ServerBootstrap
        serverBootstrap.group(bossGroup, workerGroup)//多线程处理
                //制定通道类型为NioServerSocketChannel，一种异步模式的可以监听新进来的TCP连接的通道
                .channel(NioServerSocketChannel.class)
                //设置childHandler执行所有的连接请求
                //注册handler
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        serverBootstrap.bind(8000);
    }

}