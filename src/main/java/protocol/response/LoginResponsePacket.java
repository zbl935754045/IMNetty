package protocol.response;


import lombok.Data;
import protocol.Packet;

import static protocol.command.Command.LOGIN_RESPONSE;


@Data
public class LoginResponsePacket extends Packet {
    private String userId;
    private boolean success;
    private String reason;
    private String userName;
    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
