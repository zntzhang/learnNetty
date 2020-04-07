package netty.reactor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.ServerHandler;

/**
 * 单Reactor单线程模型
 */
public class SingleReactorDemo {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)//单线程处理
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerHandler());
        serverBootstrap.bind(8000);
    }
}
