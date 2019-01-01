package protocol.response;

import lombok.Data;
import protocol.Packet;
import static protocol.command.Command.MESSAGE_REQUEST;


@Data
public class MessageResponsePacket extends Packet {
    private String fromUserId;

    private String fromUserName;

    private String message;


    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
