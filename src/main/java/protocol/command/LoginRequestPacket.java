package protocol.command;

import lombok.Data;
import static protocol.command.Command.LOGIN_REQUEST;//引入Command接口的变量作为静态变量

//定义登录请求数据包
@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
