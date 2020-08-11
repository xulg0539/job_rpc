package com.lagou.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xulin
 * @date 2020/8/11 19:51
 * @description
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private Serializer serializer;

    public RpcDecoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer=serializer;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //之前编码的时候写入了一个int，4个字节来表示长度
        if (in.readableBytes() < 4) {
            return;
        }
        //标记一下当前readIndex的位置
        in.markReaderIndex();
        //读取传过来的消息的长度--readInt会让readIndex增加4
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        //将bytebuf中的数据读入data字节数组
        in.readBytes(data);
        out.add(serializer.deserialize(genericClass,data));
    }

}
