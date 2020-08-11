package com.lagou.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xulin
 * @date 2020/8/11 19:35
 * @description
 */
public class RpcEncoder extends MessageToByteEncoder {
    private Class<?> clazz;

    private Serializer serializer;



    public RpcEncoder(Class<?> clazz, Serializer serializer) {

        this.clazz = clazz;

        this.serializer = serializer;

    }



    @Override

    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {

        if (clazz != null && clazz.isInstance(msg)) {

            //json序列化的方式将对象转成字节数组
            byte[] bytes = serializer.serialize(msg);

            //将消息长度写到消息头中
            byteBuf.writeInt(bytes.length);

            //将发送的具体数据放到消息体中
            byteBuf.writeBytes(bytes);

        }

    }

}
