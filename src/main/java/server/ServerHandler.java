package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.request.LoginRequestPacket;
import protocol.Packet;
import protocol.PacketCodeC;
import protocol.request.MessageRequestPacket;
import protocol.response.LoginResponsePacket;
import protocol.response.MessageResponsePacket;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {//msg：为什么 Netty 不直接把这个参数类型定义为 ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        //解码
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        //判断是否是登陆请求包
        if (packet instanceof LoginRequestPacket) {
            System.out.println(new Date() + ": 收到客户端登录请求……");
            // 登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            //登陆校验
            if (valid(loginRequestPacket)) {
                //校验成功
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 登录成功!");
            } else {
                //校验失败
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败!");
            }
            //编码
            ByteBuf out = PacketCodeC.INSTANCE.encode(ctx.alloc(),loginResponsePacket);
            ctx.channel().writeAndFlush(out);
        }else if (packet instanceof MessageRequestPacket){
            // 处理消息
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf =PacketCodeC.INSTANCE.encode(ctx.alloc(),messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

//    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
//        byte[] bytes = "嘻嘻".getBytes(Charset.forName("utf-8"));
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes(bytes);
//        return buffer;
//    }

    //校验登陆参数
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
