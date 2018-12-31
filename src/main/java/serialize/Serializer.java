package serialize;

import serialize.impl.JSONSerializer;


//定义序列化接口
public interface Serializer {
    /**
     * json 序列化
     */
    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * 获取具体的序列化算法标识
     * @return
     */
    byte getSerializerAlogrithm();

    /**
     * java 对象转换成二进制
     * 将 Java 对象转换成字节数组
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     *
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}