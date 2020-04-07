package netty.reactor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 主从Reactor多线程模型
 */
public class MoreReactorMoreThreadDemo {
    public static void main(String[] args) {
        // bossGroup表示监听端口，accept 新连接的线程组，
        // workerGroup表示处理每一条连接的数据读写的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MoreThreadHandler());
        serverBootstrap.bind(8000);
    }
}
