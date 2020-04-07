package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //客户端连接服务器后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接服务器，开始发送数据......");
        byte[] req = "QUERY TIME ORDER".getBytes();//请求消息
        ByteBuf firstMessage = Unpooled.buffer(req.length);//发送类
        firstMessage.writeBytes(req);//发送
        ctx.writeAndFlush(firstMessage);//flush
    }

    //从服务器收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        System.out.println("client 读取server数据..");
        //服务器返回消息后
        byte[] req = new byte[buf.readableBytes()];//创建一个存储信息的byte数组
        buf.readBytes(req);//将buffer中的数据读到byte数组中
        String body = new String(req, "UTF-8");//将byte数组转换为String(并转码)
        System.out.println("服务端数据为：" + body);//打印服务端反馈的信息
    }

    //发生异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client exceptionCaught...");
        //释放资源
        ctx.close();
    }


    public static void main(String[] args) {
        int a;int b;int c;int d;
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int num : arr) {
            a = num;
            for (int num2 : arr) {
                b = num2;
                for (int num3 : arr) {
                    c = num3;
                    for (int num4 : arr) {
                        d = num4;
                        boolean rule = a * 1000 + b * 100 + c * 10 + d - (a * 100 + b * 10 + c) == d * 1000 + c * 100 + d * 10 + c;
                        if (rule && a != 0 && b != 0 && c != 0 && d != 0) {
                            System.out.println("a=" + a);
                            System.out.println("b=" + b);
                            System.out.println("c=" + c);
                            System.out.println("d=" + d);
                            break;
                        }
                    }
                }
            }
        }
    }
}