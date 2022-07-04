package com.redhat.service.smartevents.performance.webhook.models;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class WebhookRequest {

    @NotEmpty(message = "bridgeId name cannot be null or empty")
    @JsonProperty("bridgeId")
    private String bridgeId;

    @JsonProperty("message")
    private String message;

    public WebhookRequest() {
    }

    public WebhookRequest(String bridgeId) {
        this.bridgeId = bridgeId;
    }

    public String getBridgeId() {
        return bridgeId;
    }


    public Event toEntity() {
        Event event = new Event();
        event.setBridgeId(bridgeId);
        event.setMessage(message);

        return event;
    }

}
