package com.qrcb.common.extension.datahub.exception;

import org.springframework.lang.Nullable;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description 监听器执行失败异常处理 <br/>
 */

public class ListenerExecutionFailedException extends DataHubException {

    private final String groupId;

    /**
     * Construct an instance with the provided properties.
     * @param message the exception message.
     */
    public ListenerExecutionFailedException(String message) {
        this(message, null, null);
    }

    /**
     * Construct an instance with the provided properties.
     * @param message the exception message.
     * @param cause the cause.
     */
    public ListenerExecutionFailedException(String message, @Nullable Throwable cause) {
        this(message, null, cause);
    }

    /**
     * Construct an instance with the provided properties.
     * @param message the exception message.
     * @param groupId the container's group.id property.
     * @param cause the cause.
     * @since 2.2.4
     */
    public ListenerExecutionFailedException(String message, @Nullable String groupId, @Nullable Throwable cause) {
        super(message, cause);
        this.groupId = groupId;
    }

    /**
     * Return the consumer group.id property of the container that threw this exception.
     * @return the group id; may be null, but not when the exception is passed to an error
     * handler by a listener container.
     * @since 2.2.4
     */
    @Nullable
    public String getGroupId() {
        return this.groupId;
    }
}
