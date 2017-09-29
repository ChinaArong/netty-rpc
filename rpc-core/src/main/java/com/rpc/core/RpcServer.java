package com.rpc.core;

import com.rpc.domain.IPInfo;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import zr.rpc.annotation.RpcService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class RpcServer implements ApplicationContextAware,InitializingBean {

    private List<ServiceLogic> serviceLogics;

    private Map<String,List<Class>> interfaceClazzs;

    private ZookeeperServer zookeeperServer;

    private Map<String, Object> handlerMap = new HashMap<String, Object>();

    public void setZookeeperServer(ZookeeperServer zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public void setServiceLogics(List<ServiceLogic> serviceLogics) {
        this.serviceLogics = serviceLogics;
    }

    public void setInterfaceClazzs(Map<String, List<Class>> interfaceClazzs) {
        this.interfaceClazzs = interfaceClazzs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        zookeeperServer.setLogic(serviceLogics);
//        //get rpcservers
//        Map<String, Object> serviceBeans = applicationContext
//                .getBeansWithAnnotation(RpcService.class);
//        for(Object service:serviceBeans.values()){
//            Class<?> aClass = service.getClass();
//            Class<?>[] interfaces = aClass.getInterfaces();
//            for(Class clazz:interfaces){
//                handlerMap.put(clazz.getName(),service);
//            }
//        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
