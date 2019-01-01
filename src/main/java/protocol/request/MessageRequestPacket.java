package protocol.request;


import lombok.Data;
import lombok.NoArgsConstructor;
import protocol.Packet;
import static protocol.command.Command.MESSAGE_REQUEST;

@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {
    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }
    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
