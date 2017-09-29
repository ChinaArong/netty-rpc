package com.rpc.core;

import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class RpcInftPathMapperScanner extends ClassPathBeanDefinitionScanner {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<Object>();

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory =
            new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private String serverName;

    public RpcInftPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public RpcInftPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public RpcInftPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public RpcInftPathMapperScanner(BeanDefinitionRegistry registry, String serverName) {
        super(registry);
        this.serverName = serverName;
    }

    public void scanGrenet(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator, String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if(beanDefinitions.isEmpty()){
            this.logger.warn("No mapper was found");
        }else {
            this.processBeanDefinitions(beanDefinitions,registry,beanNameGenerator);
        }

    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue #59
            definition.setBeanClass(this.mapperFactoryBean.getClass());
            definition.getPropertyValues().add("serverName",serverName);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    /**
     * Scan the class path for candidate components.
     * @param basePackage the package to check for annotated classes
     * @return a corresponding Set of autodetected bean definitions
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            boolean traceEnabled = logger.isTraceEnabled();
            for (Resource resource : resources) {
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                        if (isCandidateComponent(metadataReader)) {
                            ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                            sbd.setResource(resource);
                            sbd.setSource(resource);
                            candidates.add(sbd);
                        }
                        else {
                            if (traceEnabled) {
                                logger.trace("Ignored because not matching any filter: " + resource);
                            }
                        }
                    }
                    catch (Throwable ex) {
                        throw new BeanDefinitionStoreException(
                                "Failed to read candidate component class: " + resource, ex);
                    }
                }
                else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: " + resource);
                    }
                }
            }
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

}
