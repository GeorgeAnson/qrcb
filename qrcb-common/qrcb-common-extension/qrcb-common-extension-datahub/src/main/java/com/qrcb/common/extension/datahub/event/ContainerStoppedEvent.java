package com.qrcb.common.extension.datahub.event;

/**
 * @Author Anson
 * @Create 2023-12-22
 * @Description <br/>
 */

public class ContainerStoppedEvent extends DataHubEvent {
    public ContainerStoppedEvent(Object source, Object container) {
        super( source, container );
    }

    @Override
    public String toString() {
        return "ContainerStoppedEvent [source=" + getSource() + "]";
    }
}
