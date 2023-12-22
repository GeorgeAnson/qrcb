package com.qrcb.common.extension.datahub.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.Lifecycle;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

@Slf4j
public class ContainerGroup implements Lifecycle {

    private final String name;
    private final Collection<MessageListenerContainer> containers = new LinkedHashSet<>();
    private boolean running;

    public ContainerGroup(String name) {
        this.name = name;
    }

    public ContainerGroup(String name, List<MessageListenerContainer> containers) {
        this.name = name;
        this.containers.addAll(containers);
    }

    public ContainerGroup(String name, MessageListenerContainer... containers) {
        this.name = name;
        MessageListenerContainer[] var3 = containers;
        int var4 = containers.length;

        this.containers.addAll( Arrays.asList( var3 ).subList( 0, var4 ) );

    }

    public String getName() {
        return this.name;
    }

    public Collection<String> getListenerIds() {
        return this.containers.stream().map( MessageListenerContainer :: getListenerId )
                .peek((id) -> Assert.state(id != null, "Containers must have listener ids to be used here") )
                .collect( Collectors.toList());
    }

    public boolean contains(MessageListenerContainer container) {
        return this.containers.contains(container);
    }

    public boolean allStopped() {
        return this.containers.stream().allMatch((container) -> {
            return !container.isRunning();
        });
    }

    public void addContainers(MessageListenerContainer... theContainers) {
        MessageListenerContainer[] var2 = theContainers;
        int var3 = theContainers.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            MessageListenerContainer container = var2[var4];
            this.containers.add(container);
        }

    }

    public boolean removeContainer(MessageListenerContainer container) {
        return this.containers.remove(container);
    }

    public synchronized void start() {
        if (!this.running) {
            this.containers.forEach((container) -> {
                log.debug(String.format("Starting: %s" , container));
                container.start();
            });
            this.running = true;
        }

    }

    public synchronized void stop() {
        if (this.running) {
            this.containers.forEach((container) -> {
                container.stop();
            });
            this.running = false;
        }

    }

    public synchronized boolean isRunning() {
        return this.running;
    }

    public String toString() {
        return "ContainerGroup [name=" + this.name + ", containers=" + this.containers + "]";
    }
}
