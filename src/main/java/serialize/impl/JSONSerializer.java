package serialize.impl;

import com.alibaba.fastjson.JSON;
import serialize.Serializer;
import serialize.SerializerAlgorithm;


//我们使用最简单的 json 序列化方式，使用阿里巴巴的 fastjson 作为序列化框架
public class JSONSerializer implements Serializer {

    //获取具体的序列化算法标识
    @Override
    public byte getSerializerAlogrithm() {
        return SerializerAlgorithm.JSON;
    }

    //将 Java 对象转换成字节数组
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    //将字节数组转换成某种类型的 Java 对象
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);
    }
}
