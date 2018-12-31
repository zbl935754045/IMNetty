package protocol.command;

import lombok.Data;

//定义数据包
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;


    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();

}
