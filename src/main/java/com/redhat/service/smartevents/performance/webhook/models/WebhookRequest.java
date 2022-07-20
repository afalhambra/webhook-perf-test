package com.redhat.service.smartevents.performance.webhook.models;

import java.time.Instant;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class WebhookRequest {

    @NotEmpty(message = "bridgeId cannot be null or empty")
    @JsonProperty("bridgeId")
    private String bridgeId;

    @JsonProperty("message")
    private String message;

    @NotNull(message = "submitted_at cannot be null")
    @JsonProperty("submitted_at")
    private Long submittedAt;

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
        event.setReceivedAt(Instant.now());
        event.setSubmittedAt(Instant.ofEpochMilli(submittedAt));
        return event;
    }

}
