package io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServerV2 {

    public static void main(String[] args) throws Exception {
        //创建一个缓存线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建Socket服务，监听8000端口
        ServerSocket server = new ServerSocket(8001);
        System.out.println("服务端启动！");
        while (true) {
            //获取一个套接字(阻塞)
            final Socket socket = server.accept();
            System.out.println("出现一个新客户端！");
            //在线程池为新客户端开一个线程
            newCachedThreadPool.execute(() -> handle(socket));
        }
    }

    /**
     * 处理数据
     *
     * @param socket
     * @throws IOException
     */
    private static void handle(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream input = socket.getInputStream();

            int read = 0;
            while (read != -1) {
                //读取数据(阻塞)
                read = input.read(bytes);
                System.out.println(new String(bytes, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
