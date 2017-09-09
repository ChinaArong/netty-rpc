package com.rpc.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class RpcMapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean {

    private BeanNameGenerator beanNameGenerator;

    private Map<String,List<String>> basePackage;

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public void setBasePackage(Map<String, List<String>> basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for(Map.Entry<String,List<String>> serverInfo:basePackage.entrySet()){
            for(String packageName:serverInfo.getValue()){
                RpcInftPathMapperScanner scanner = new RpcInftPathMapperScanner(registry,serverInfo.getKey());
                scanner.scanGrenet(registry,beanNameGenerator,StringUtils
                        .tokenizeToStringArray(packageName, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
            }
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
