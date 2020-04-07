package netty.reactor;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程Handler
 */
public class MoreThreadHandler extends ChannelInboundHandlerAdapter {

    private static ExecutorService executors = Executors.newScheduledThreadPool(200);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取数据
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        executors.submit(()->{
            //向客户端写数据
            String currentTime = new Date(System.currentTimeMillis()).toString();
            ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
            ctx.write(resp);
            ctx.flush();//刷新后才将数据发出到SocketChannel
        });
    }
}
