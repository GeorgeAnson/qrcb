package com.qrcb.common.extension.datahub.listener;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public interface ConsumerSeekAware {
    /**
     * Register the callback to use when seeking at some arbitrary time. When used with a
     * {@code ConcurrentMessageListenerContainer} or the same listener instance in multiple
     * containers listeners should store the callback in a {@code ThreadLocal}.
     *
     * @param callback the callback.
     */
    default void registerSeekCallback(ConsumerSeekCallback callback) {
        // do nothing
    }

    /**
     * Called when the listener consumer terminates allowing implementations to clean up
     * state, such as thread locals.
     *
     * @since 2.4
     */
    default void unregisterSeekCallback() {
        // do nothing
    }

    /**
     * A callback that a listener can invoke to seek to a specific offset.
     */
    interface ConsumerSeekCallback {

        /**
         * @param topic the topic.
         * @param shardId the shardId.
         * @param offset the offset (absolute).
         */
        void seek(String topic, int shardId, long offset);

        /**
         * @param topic the topic.
         * @param shardId the shardId.
         */
        void seekToBeginning(String topic, int shardId);

        /**
         * @param topic the topic.
         * @param shardId the shardId.
         */
        void seekToEnd(String topic, int shardId);

        /**
         * @param topic the topic.
         * @param shardId the shardId.
         * @param offset the offset; positive values are relative to the start, negative
         * values are relative to the end, unless toCurrent is true.
         * @param toCurrent true for the offset to be relative to the current position
         * rather than the beginning or end.
         */
        void seekRelative(String topic, int shardId, long offset, boolean toCurrent);

        /**
         * because the offset lookup is blocking.
         *
         * @param topic the topic.
         * @param shardId the shardId.
         * @param timestamp the time stamp.
         *
         * @since 2.3
         */
        void seekToTimestamp(String topic, int shardId, long timestamp);

    }

}
