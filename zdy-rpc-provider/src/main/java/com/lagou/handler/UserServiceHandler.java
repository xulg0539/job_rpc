package com.lagou.handler;

import com.lagou.service.RpcRequest;
import com.lagou.service.UserServiceImpl;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * 自定义的业务处理器
 */
@Component
public class UserServiceHandler extends ChannelInboundHandlerAdapter  implements ApplicationContextAware {
   private static ApplicationContext applicationContext2;
    //当客户端读取数据时,该方法会被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //注意:  客户端将来发送请求的时候会传递一个参数:  UserService#sayHello#are you ok
         //1.判断当前的请求是否符合规则
//        if(msg.toString().startsWith("UserService")){
//            //2.如果符合规则,调用实现类货到一个result
//            UserServiceImpl service = new UserServiceImpl();
//            String result = service.sayHello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
//            //3.把调用实现类的方法获得的结果写到客户端
//            ctx.writeAndFlush(result);
//        }

        //解析RpcRequest形式的参数
        RpcRequest req=(RpcRequest)msg;
        Object result=parseAndServiceInvoke(req);
        ctx.writeAndFlush("success");


    }

    //按照协议解析请求处理并执行服务本地调用
    private Object parseAndServiceInvoke(RpcRequest req) throws ClassNotFoundException, InvocationTargetException {
        Class<?> clazz=Class.forName(req.getClassName());
        //从容器中获取本地服务对象
        Object service=applicationContext2.getBean(clazz);
        Class<?> serviceClass=service.getClass();
        String methodName=req.getMethodName();
        Class<?>[] parameterTypes=req.getParameterTypes();
        Object[] parameters=req.getParameters();
        FastClass fastClass=FastClass.create(serviceClass);
        FastMethod fastMethod= fastClass.getMethod(methodName,parameterTypes);
        return fastMethod.invoke(service,parameters);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UserServiceHandler.applicationContext2=applicationContext;
    }
}
