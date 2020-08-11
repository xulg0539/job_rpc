package com.lagou.service;

import com.alibaba.fastjson.JSON;

/**
 * @author xulin
 * @date 2020/8/11 19:34
 * @description
 */
public class JSONSerializer  implements Serializer{
    @Override

    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);

    }



    @Override

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);

    }

}
