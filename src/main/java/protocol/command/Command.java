package protocol.command;


//定义命令接口
public interface Command {
    Byte LOGIN_REQUEST = 1;//登录命令
    Byte LOGIN_RESPONSE = 2;//登陆响应
    Byte MESSAGE_REQUEST = 3;//消息请求
    Byte MESSAGE_RESPONSE = 4;//消息回复
}
