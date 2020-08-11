package com.lagou.service;

import java.io.IOException;

/**
 * @author xulin
 * @date 2020/8/11 19:33
 * @description
 */
public interface Serializer {
    /**

     * java对象转换为二进制

     *

     * @param object

     * @return

     */

    byte[] serialize(Object object) throws IOException;



    /**

     * 二进制转换成java对象

     *

     * @param clazz

     * @param bytes

     * @param <T>

     * @return

     */

    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;

}
