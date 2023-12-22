package com.qrcb.common.extension.datahub.annotation;

import com.qrcb.common.extension.datahub.DataHubProperties;
import com.qrcb.common.extension.datahub.config.DataHubListenerContainerFactory;
import com.qrcb.common.extension.datahub.config.DataHubListenerEndpointRegistrar;
import com.qrcb.common.extension.datahub.config.DataHubListenerEndpointRegistry;
import com.qrcb.common.extension.datahub.config.MethodDataHubListenerEndpoint;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.log.LogAccessor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class DataHubListenerAnnotationProcessor implements BeanPostProcessor, Ordered, ApplicationContextAware, InitializingBean, SmartInitializingSingleton {
    protected final LogAccessor logger = new LogAccessor( LogFactory.getLog( this.getClass() ) );
    private static final String GENERATED_ID_PREFIX = "org.springblade.core.datahub.listener.DataHubListenerAnnotationProcessor#";
    public static final String DEFAULT_DATAHUB_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "dataHubListenerContainerFactory";
    private String defaultContainerFactoryBeanName = DEFAULT_DATAHUB_LISTENER_CONTAINER_FACTORY_BEAN_NAME;
    private final AtomicInteger counter = new AtomicInteger();
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();
    private BeanExpressionContext expressionContext;
    private final DataHubListenerEndpointRegistrar registrar = new DataHubListenerEndpointRegistrar();
    private final DataHubProperties dataHubProperties;
    private DataHubListenerEndpointRegistry endpointRegistry;
    private final ListenerScope listenerScope = new ListenerScope();
    private AnnotationEnhancer enhancer;
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap( new ConcurrentHashMap<>( 64 ) );

    public DataHubListenerAnnotationProcessor(DataHubProperties dataHubProperties) {
        this.dataHubProperties = dataHubProperties;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains( bean.getClass() )) {
            Class<?> targetClass = AopUtils.getTargetClass( bean );
            Map<Method, Set<DataHubListener>> annotatedMethods = MethodIntrospector.selectMethods( targetClass,
                    (MethodIntrospector.MetadataLookup<Set<DataHubListener>>) method -> {
                        Set<DataHubListener> listenerMethods = findListenerAnnotations( method );
                        return ( !listenerMethods.isEmpty() ? listenerMethods : null );
                    } );

            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add( bean.getClass() );
                this.logger.trace( () -> "No @DatahubListener annotations found on bean type: " + bean.getClass() );
            } else {
                // Non-empty set of methods
                for (Map.Entry<Method, Set<DataHubListener>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    for (DataHubListener listener : entry.getValue()) {
                        processDatahubListener( listener, method, bean, beanName );
                    }
                }
                this.logger.debug( () -> annotatedMethods.size() + " @DatahubListener methods processed on bean '"
                        + beanName + "': " + annotatedMethods );
            }
        }
        return bean;
    }

    private Set<DataHubListener> findListenerAnnotations(Method method) {
        Set<DataHubListener> listeners = new HashSet<>();
        DataHubListener ann = AnnotatedElementUtils.findMergedAnnotation( method, DataHubListener.class );
        if (ann != null) {
            ann = enhance( method, ann );
            listeners.add( ann );
        }
        return listeners;
    }

    private DataHubListener enhance(AnnotatedElement element, DataHubListener ann) {
        if (this.enhancer == null) {
            return ann;
        } else {
            return AnnotationUtils.synthesizeAnnotation(
                    this.enhancer.apply( AnnotationUtils.getAnnotationAttributes( ann ), element ), DataHubListener.class, null );
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildEnhancer();
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.registrar.setBeanFactory( this.beanFactory );
        if (this.defaultContainerFactoryBeanName != null) {
            this.registrar.setContainerFactoryBeanName( this.defaultContainerFactoryBeanName );
        }
        if (this.registrar.getEndpointRegistry() == null) {
            if (this.endpointRegistry == null) {
                Assert.state( this.beanFactory != null,
                        "BeanFactory must be set to find endpoint registry by bean name" );
                this.endpointRegistry = this.beanFactory.getBean( "dataHubListenerEndpointRegistry",
                        DataHubListenerEndpointRegistry.class );
            }
            this.registrar.setEndpointRegistry( this.endpointRegistry );
        }
        this.registrar.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.setBeanFactory( ( (ConfigurableApplicationContext) applicationContext ).getBeanFactory() );
        } else {
            this.setBeanFactory( applicationContext );
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ( (ConfigurableListableBeanFactory) beanFactory ).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext( (ConfigurableListableBeanFactory) beanFactory, this.listenerScope );
        }

    }

    private String resolveExpressionAsString(String value, String attribute) {
        Object resolved = resolveExpression( value );
        if (resolved instanceof String) {
            return (String) resolved;
        } else if (resolved != null) {
            throw new IllegalStateException( "The [" + attribute + "] must resolve to a String. "
                    + "Resolved to [" + resolved.getClass() + "] for [" + value + "]" );
        }
        return null;
    }

    private Integer resolveExpressionAsInteger(String value, String attribute) {
        Object resolved = resolveExpression( value );
        Integer result = null;
        if (resolved instanceof String) {
            result = Integer.parseInt( (String) resolved );
        } else if (resolved instanceof Number) {
            result = ( (Number) resolved ).intValue();
        } else if (resolved != null) {
            throw new IllegalStateException(
                    "The [" + attribute + "] must resolve to an Number or a String that can be parsed as an Integer. "
                            + "Resolved to [" + resolved.getClass() + "] for [" + value + "]" );
        }
        return result;
    }

    private Boolean resolveExpressionAsBoolean(String value, String attribute) {
        Object resolved = resolveExpression( value );
        Boolean result = null;
        if (resolved instanceof Boolean) {
            result = (Boolean) resolved;
        } else if (resolved instanceof String) {
            result = Boolean.parseBoolean( (String) resolved );
        } else if (resolved != null) {
            throw new IllegalStateException(
                    "The [" + attribute + "] must resolve to a Boolean or a String that can be parsed as a Boolean. "
                            + "Resolved to [" + resolved.getClass() + "] for [" + value + "]" );
        }
        return result;
    }

    private Object resolveExpression(String value) {
        return this.resolver.evaluate( resolve( value ), this.expressionContext );
    }

    /**
     * Resolve the specified value if possible.
     *
     * @param value the value to resolve
     *
     * @return the resolved value
     *
     * @see ConfigurableBeanFactory#resolveEmbeddedValue
     */
    private String resolve(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ( (ConfigurableBeanFactory) this.beanFactory ).resolveEmbeddedValue( value );
        }
        return value;
    }

    @Override
    public int getOrder() {
        return 2147483647;
    }

    private void buildEnhancer() {
        if (this.applicationContext != null) {
            Map<String, AnnotationEnhancer> enhancersMap =
                    this.applicationContext.getBeansOfType( AnnotationEnhancer.class, false, false );
            if (enhancersMap.size() > 0) {
                List<AnnotationEnhancer> enhancers = enhancersMap.values()
                        .stream()
                        .sorted( new OrderComparator() )
                        .collect( Collectors.toList() );
                this.enhancer = (attrs, element) -> {
                    Map<String, Object> newAttrs = attrs;
                    for (AnnotationEnhancer enh : enhancers) {
                        newAttrs = enh.apply( newAttrs, element );
                    }
                    return attrs;
                };
            }
        }
    }

    protected void processDatahubListener(DataHubListener dataHubListener, Method method, Object bean, String beanName) {
        Method methodToUse = checkProxy( method, bean );
        MethodDataHubListenerEndpoint endpoint = new MethodDataHubListenerEndpoint();
        endpoint.setMethod( methodToUse );
        String beanRef = dataHubListener.beanRef();
        this.listenerScope.addListener( beanRef, bean );
        processListener( endpoint, dataHubListener, bean, beanName );
        this.listenerScope.removeListener( beanRef );
    }

    protected void processListener(MethodDataHubListenerEndpoint endpoint, DataHubListener dataHubListener,
                                   Object bean, String beanName) {

        processDatahubListenerAnnotationBeforeRegistration( endpoint, dataHubListener, bean );

        String containerFactory = resolve( dataHubListener.containerFactory() );
        DataHubListenerContainerFactory<?> listenerContainerFactory = resolveContainerFactory( dataHubListener, containerFactory, beanName );

        this.registrar.registerEndpoint( endpoint, listenerContainerFactory );
    }

    private void processDatahubListenerAnnotationBeforeRegistration(MethodDataHubListenerEndpoint endpoint, DataHubListener dataHubListener,
                                                                    Object bean) {
        endpoint.setBean( bean );
        endpoint.setId( getEndpointId( dataHubListener ) );
        endpoint.setProject( dataHubListener.project() );
        endpoint.setSubId( dataHubListener.subId() );
        endpoint.setTopic( dataHubListener.topic() );
        endpoint.setAccessId( dataHubProperties.getAccessId() );
        endpoint.setAccessKey( dataHubProperties.getAccessKey() );
        endpoint.setEndpoint( dataHubProperties.getEndpoint() );
        String concurrency = dataHubListener.concurrency();
        if (StringUtils.hasText( concurrency )) {
            endpoint.setConcurrency( resolveExpressionAsInteger( concurrency, "concurrency" ) );
        }
        String autoStartup = dataHubListener.autoStartup();
        if (StringUtils.hasText( autoStartup )) {
            endpoint.setAutoStartup( resolveExpressionAsBoolean( autoStartup, "autoStartup" ) );
        }
        endpoint.setBeanFactory( this.beanFactory );
    }

    @Nullable
    private DataHubListenerContainerFactory<?> resolveContainerFactory(DataHubListener dataHubListener,
                                                                       Object factoryTarget, String beanName) {
        String containerFactory = dataHubListener.containerFactory();
        if (!StringUtils.hasText( containerFactory )) {
            return null;
        }

        DataHubListenerContainerFactory<?> factory = null;

        Object resolved = resolveExpression( containerFactory );
        if (resolved instanceof DataHubListenerContainerFactory) {
            return (DataHubListenerContainerFactory<?>) resolved;
        }
        String containerFactoryBeanName = resolveExpressionAsString( containerFactory,
                "containerFactory" );
        if (StringUtils.hasText( containerFactoryBeanName )) {
            assertBeanFactory();
            try {
                factory = this.beanFactory.getBean( containerFactoryBeanName, DataHubListenerContainerFactory.class );
            } catch (NoSuchBeanDefinitionException ex) {
                throw new BeanInitializationException(
                        noBeanFoundMessage( factoryTarget, beanName, containerFactoryBeanName,
                                DataHubListenerContainerFactory.class ), ex );
            }
        }
        return factory;
    }

    protected void assertBeanFactory() {
        Assert.state( this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name" );
    }

    protected String noBeanFoundMessage(Object target, String listenerBeanName, String requestedBeanName,
                                        Class<?> expectedClass) {
        return "Could not register Datahub listener endpoint on ["
                + target + "] for bean " + listenerBeanName + ", no '" + expectedClass.getSimpleName() + "' with id '"
                + requestedBeanName + "' was found in the application context";
    }

    private String getEndpointId(DataHubListener dataHubListener) {
        if (StringUtils.hasText( dataHubListener.id() )) {
            return resolveExpressionAsString( dataHubListener.id(), "id" );
        } else {
            return GENERATED_ID_PREFIX + this.counter.getAndIncrement();
        }
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy( bean )) {
            try {
                // Found a @DatahubListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod( method.getName(), method.getParameterTypes() );
                Class<?>[] proxiedInterfaces = ( (Advised) bean ).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod( method.getName(), method.getParameterTypes() );
                        break;
                    } catch (@SuppressWarnings("unused") NoSuchMethodException noMethod) {
                        // NOSONAR
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException( ex );
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException( String.format(
                        "@DatahubListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(),
                        method.getDeclaringClass().getSimpleName() ), ex );
            }
        }
        return method;
    }

    public interface AnnotationEnhancer extends BiFunction<Map<String, Object>, AnnotatedElement, Map<String, Object>> {

    }

    static class ListenerScope implements Scope {
        private final Map<String, Object> listeners = new HashMap();

        ListenerScope() {
        }

        public void addListener(String key, Object bean) {
            this.listeners.put( key, bean );
        }

        public void removeListener(String key) {
            this.listeners.remove( key );
        }

        public Object get(String name, ObjectFactory<?> objectFactory) {
            return this.listeners.get( name );
        }

        public Object remove(String name) {
            return null;
        }

        public void registerDestructionCallback(String name, Runnable callback) {
        }

        public Object resolveContextualObject(String key) {
            return this.listeners.get( key );
        }

        public String getConversationId() {
            return null;
        }
    }
}
