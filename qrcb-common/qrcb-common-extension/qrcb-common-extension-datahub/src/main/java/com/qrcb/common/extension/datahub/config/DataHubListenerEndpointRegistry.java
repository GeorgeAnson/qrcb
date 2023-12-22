package com.qrcb.common.extension.datahub.config;

import com.qrcb.common.extension.datahub.listener.ContainerGroup;
import com.qrcb.common.extension.datahub.listener.MessageListenerContainer;
import com.qrcb.common.extension.datahub.support.EndpointHandlerMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@Slf4j
public class DataHubListenerEndpointRegistry implements ListenerContainerRegistry, DisposableBean, SmartLifecycle, ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, MessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();
    private int phase = 2147483547;
    private ConfigurableApplicationContext applicationContext;
    private boolean contextRefreshed;
    private volatile boolean running;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        }

    }

    @Nullable
    public MessageListenerContainer getListenerContainer(String id) {
        Assert.hasText( id, "Container identifier must not be empty" );
        return (MessageListenerContainer) this.listenerContainers.get( id );
    }

    public Set<String> getListenerContainerIds() {
        return Collections.unmodifiableSet( this.listenerContainers.keySet() );
    }

    public Collection<MessageListenerContainer> getListenerContainers() {
        return Collections.unmodifiableCollection( this.listenerContainers.values() );
    }

    public Collection<MessageListenerContainer> getAllListenerContainers() {
        List<MessageListenerContainer> containers = new ArrayList();
        containers.addAll( this.getListenerContainers() );
        containers.addAll( this.applicationContext.getBeansOfType( MessageListenerContainer.class, true, false ).values() );
        return containers;
    }

    public void registerListenerContainer(DataHubListenerEndpoint endpoint, DataHubListenerContainerFactory<?> factory) {
        this.registerListenerContainer( endpoint, factory, false );
    }

    @SuppressWarnings("unchecked")
    public void registerListenerContainer(DataHubListenerEndpoint endpoint, DataHubListenerContainerFactory<?> factory, boolean startImmediately) {
        Assert.notNull( endpoint, "Endpoint must not be null" );
        Assert.notNull( factory, "Factory must not be null" );
        String id = endpoint.getId();
        Assert.hasText( id, "Endpoint id must not be empty" );
        synchronized (this.listenerContainers) {
            Assert.state( !this.listenerContainers.containsKey( id ), "Another endpoint is already registered with id '" + id + "'" );
            MessageListenerContainer container = this.createListenerContainer( endpoint, factory );
            this.listenerContainers.put( id, container );
            ConfigurableApplicationContext appContext = this.applicationContext;
            String groupName = endpoint.getGroup();
            if (StringUtils.hasText( groupName ) && appContext != null) {
                List containerGroup;
                ContainerGroup group;
                if (appContext.containsBean( groupName )) {
                    containerGroup = appContext.getBean( groupName, List.class );
                    group = appContext.getBean( groupName + ".group", ContainerGroup.class );
                } else {
                    containerGroup = new ArrayList<>();
                    appContext.getBeanFactory().registerSingleton( groupName, containerGroup );
                    group = new ContainerGroup( groupName );
                    appContext.getBeanFactory().registerSingleton( groupName + ".group", group );
                }
                containerGroup.add( container );
                group.addContainers( container );
            }

            if (startImmediately) {
                this.startIfNecessary( container );
            }

        }
    }

    protected MessageListenerContainer createListenerContainer(DataHubListenerEndpoint endpoint, DataHubListenerContainerFactory<?> factory) {
        if (endpoint instanceof MethodDataHubListenerEndpoint) {
            MethodDataHubListenerEndpoint mkle = (MethodDataHubListenerEndpoint) endpoint;
            Object bean = mkle.getBean();
            if (bean instanceof EndpointHandlerMethod) {
                EndpointHandlerMethod ehm = (EndpointHandlerMethod) bean;
                ehm = new EndpointHandlerMethod( ehm.resolveBean( this.applicationContext ), ehm.getMethodName() );
                mkle.setBean( ehm.resolveBean( this.applicationContext ) );
                mkle.setMethod( ehm.getMethod() );
            }
        }

        MessageListenerContainer listenerContainer = factory.createListenerContainer( endpoint );
        if (listenerContainer instanceof InitializingBean) {
            try {
                ( (InitializingBean) listenerContainer ).afterPropertiesSet();
            } catch (Exception var6) {
                throw new BeanInitializationException( "Failed to initialize message listener container", var6 );
            }
        }

        int containerPhase = listenerContainer.getPhase();
        if (listenerContainer.isAutoStartup() && containerPhase != 2147483547) {
            if (this.phase != 2147483547 && this.phase != containerPhase) {
                throw new IllegalStateException( "Encountered phase mismatch between container factory definitions: " + this.phase + " vs " + containerPhase );
            }

            this.phase = listenerContainer.getPhase();
        }

        return listenerContainer;
    }

    public void destroy() throws Exception {

        for (MessageListenerContainer listenerContainer : this.getListenerContainers()) {
            listenerContainer.destroy();
        }

    }

    public int getPhase() {
        return this.phase;
    }

    public boolean isAutoStartup() {
        return true;
    }

    public void start() {

        for (MessageListenerContainer listenerContainer : this.getListenerContainers()) {
            this.startIfNecessary( listenerContainer );
        }

        this.running = true;
    }

    public void stop() {
        this.running = false;

        for (MessageListenerContainer listenerContainer : this.getListenerContainers()) {
            listenerContainer.stop();
        }

    }

    public void stop(Runnable callback) {
        this.running = false;
        Collection<MessageListenerContainer> listenerContainersToStop = this.getListenerContainers();
        if (listenerContainersToStop.size() > 0) {
            AggregatingCallback aggregatingCallback = new AggregatingCallback( listenerContainersToStop.size(), callback );

            for (MessageListenerContainer listenerContainer : listenerContainersToStop) {
                if (listenerContainer.isRunning()) {
                    listenerContainer.stop( aggregatingCallback );
                } else {
                    aggregatingCallback.run();
                }
            }
        } else {
            callback.run();
        }

    }

    public boolean isRunning() {
        return this.running;
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals( this.applicationContext )) {
            this.contextRefreshed = true;
        }

    }

    private void startIfNecessary(MessageListenerContainer listenerContainer) {
        if (this.contextRefreshed || listenerContainer.isAutoStartup()) {
            listenerContainer.start();
        }

    }

    private static final class AggregatingCallback implements Runnable {
        private final AtomicInteger count;
        private final Runnable finishCallback;

        private AggregatingCallback(int count, Runnable finishCallback) {
            this.count = new AtomicInteger( count );
            this.finishCallback = finishCallback;
        }

        public void run() {
            if (this.count.decrementAndGet() <= 0) {
                this.finishCallback.run();
            }

        }
    }

}
