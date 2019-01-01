package client;

import client.handler.LoginResponseHandler;
import client.handler.MessageResponseHandler;
import codec.PacketDecoder;
import codec.PacketEncoder;
import codec.Spliter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.request.LoginRequestPacket;
import protocol.request.MessageRequestPacket;
import util.SessionUtil;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int PORT = 8000;
    private static final String HOST = "127.0.0.1";
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
//                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.out.println("重试次数已用完，放弃连接");
            } else {
                int order = (MAX_RETRY - retry) + 1;
                int delay = 1 << order;
                System.err.println(new Date() + "连接失败，第" + order + "次连接...");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }


    private static void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.println("请输入用户名：");
                    String username = sc.nextLine();
                    loginRequestPacket.setUserName(username);
                    //密码使用默认的
                    loginRequestPacket.setPassword("pwd");
                    //发送数据包
                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
                }else {
                    String toUserId = sc.next();
                    String message = sc.next();
                    channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                }
            }
        }).start();
    }

    private static void waitForLoginResponse () {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {

        }

    }
}
