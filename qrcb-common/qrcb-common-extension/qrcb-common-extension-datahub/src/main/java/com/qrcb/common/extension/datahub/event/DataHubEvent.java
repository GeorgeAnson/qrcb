package com.qrcb.common.extension.datahub.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description Datahub 异步事件 <br/>
 */

public class DataHubEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final Object container;

    public DataHubEvent(Object source, Object container) {
        super(source);
        this.container = container;
    }

    /**
     * Get the container for which the event was published, which will be the parent
     * container if the source that emitted the event is a child container, or the source
     * itself otherwise. The type is required here to avoid a dependency tangle between
     * the event and listener packages.
     * @param type the container type (e.g. {@code MessageListenerContainer.class}).
     * @param <T> the type.
     * @return the container.
     * @since 2.2.1
     * @see #getSource(Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getContainer(Class<T> type) {
        Assert.isInstanceOf(type, this.container);
        return (T) this.container;
    }

    /**
     * Get the container (source) that published the event. This is provided as an
     * alternative to {@link #getSource()} to avoid the need to cast in user code. The
     * type is required here to avoid a dependency tangle between the event and listener
     * packages.
     * @param type the container type (e.g. {@code MessageListenerContainer.class}).
     * @param <T> the type.
     * @return the container.
     * @since 2.2.1
     * @see #getContainer(Class)
     * @see #getSource()
     */
    @SuppressWarnings("unchecked")
    public <T> T getSource(Class<T> type) {
        Assert.isInstanceOf(type, getSource());
        return (T) getSource();
    }
}

