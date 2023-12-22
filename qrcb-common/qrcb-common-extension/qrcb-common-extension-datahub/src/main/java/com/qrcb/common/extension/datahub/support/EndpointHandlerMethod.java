package com.qrcb.common.extension.datahub.support;

import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class EndpointHandlerMethod {
    private final Object beanOrClass;
    private final String methodName;
    private Object bean;
    private Method method;

    public EndpointHandlerMethod(Object beanOrClass, String methodName) {
        Assert.notNull(beanOrClass, () -> {
            return "No destination bean or class provided!";
        });
        Assert.notNull(methodName, () -> {
            return "No method name for destination bean class provided!";
        });
        this.beanOrClass = beanOrClass;
        this.methodName = methodName;
    }

    public EndpointHandlerMethod(Object bean, Method method) {
        Assert.notNull(bean, () -> {
            return "No bean for destination provided!";
        });
        Assert.notNull(method, () -> {
            return "No method for destination bean class provided!";
        });
        this.method = method;
        this.bean = bean;
        this.beanOrClass = bean.getClass();
        this.methodName = method.getName();
    }

    public Method getMethod() {
        if (this.beanOrClass instanceof Class) {
            return this.forClass((Class)this.beanOrClass);
        } else {
            Assert.state(this.bean != null, "Bean must be resolved before accessing its method");
            if (this.bean instanceof EndpointHandlerMethod) {
                try {
                    return Object.class.getMethod("toString");
                } catch (SecurityException | NoSuchMethodException ignored) {
                }
            }

            return this.forClass(this.bean.getClass());
        }
    }

    public String getMethodName() {
        Assert.state(this.methodName != null, "Unexpected call to getMethodName()");
        return this.methodName;
    }

    public Object resolveBean(BeanFactory beanFactory) {
        if (this.bean instanceof EndpointHandlerMethod) {
            return ((EndpointHandlerMethod)this.bean).beanOrClass;
        } else {
            if (this.bean == null) {
                try {
                    if (this.beanOrClass instanceof Class) {
                        Class clazz = (Class)this.beanOrClass;

                        try {
                            this.bean = beanFactory.getBean(clazz);
                        } catch (NoSuchBeanDefinitionException var5) {
                            String beanName = clazz.getSimpleName() + "-handlerMethod";
                            ((BeanDefinitionRegistry)beanFactory).registerBeanDefinition(beanName, new RootBeanDefinition(clazz));
                            this.bean = beanFactory.getBean(beanName);
                        }
                    } else {
                        String beanName = (String)this.beanOrClass;
                        this.bean = beanFactory.getBean(beanName);
                    }
                } catch (BeanCurrentlyInCreationException var6) {
                    this.bean = this;
                }
            }

            return this.bean;
        }
    }

    private Method forClass(Class<?> clazz) {
        if (this.method == null) {
            this.method = Arrays.stream( ReflectionUtils.getDeclaredMethods(clazz))
                    .filter((mthd) -> mthd.getName().equals(this.methodName) )
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No method %s in class %s", this.methodName, clazz)) );
        }

        return this.method;
    }
}
