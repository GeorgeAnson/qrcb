package com.qrcb.common.extension.datahub.listener;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.SmartLifecycle;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public interface MessageListenerContainer extends SmartLifecycle, DisposableBean {
    String getListenerId();

    void setupMessageListener(MessageListener messageListener);

    default void pause() {
        throw new UnsupportedOperationException( "This container doesn't support pause" );
    }

    default void resume() {
        throw new UnsupportedOperationException( "This container doesn't support resume" );
    }

    default void setAutoStartup(boolean autoStartup) {
        // empty
    }

    default boolean isPauseRequested() {
        throw new UnsupportedOperationException( "This container doesn't support pause/resume" );
    }

    default void stopAbnormally(Runnable callback) {
        stop( callback );
    }

    default boolean isContainerPaused() {
        throw new UnsupportedOperationException("This container doesn't support pause/resume");
    }

    default boolean isInExpectedState() {
        return true;
    }

    default boolean isChildRunning() {
        return isRunning();
    }
    @Override
    default void destroy() {
        stop();
    }

}
