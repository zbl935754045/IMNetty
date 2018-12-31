package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class NettyServer {
    private static final int BEGIN_PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //attr()方法可以给服务端的 channel，也就是NioServerSocketChannel指定一些自定义属性
                //我们可以通过channel.attr()取出这个属性
                //通常情况下，我们也用不上这个方法
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                //childAttr可以给每一条连接指定自定义属性
                //然后后续我们可以通过channel.attr()取出该属性。
                .childAttr(clientKey, "clientValue")
                //给服务端channel设置一些属性
                .option(ChannelOption.SO_BACKLOG, 1024)
                //childOption()可以给每条连接设置一些TCP底层相关的属性
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                //childHandler()用于指定处理新连接数据的读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        //System.out.println(ch.attr(clientKey).get());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });


        bind(serverBootstrap, BEGIN_PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
