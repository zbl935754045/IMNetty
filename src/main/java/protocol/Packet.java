package protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

//定义数据包
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false,serialize = false)
    private Byte version = 1;


    /**
     * 指令
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();

}
