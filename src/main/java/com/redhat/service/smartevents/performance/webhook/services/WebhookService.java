package com.redhat.service.smartevents.performance.webhook.services;

import java.util.List;

import com.redhat.service.smartevents.performance.webhook.exceptions.EventNotFoundException;
import com.redhat.service.smartevents.performance.webhook.models.Event;

public interface WebhookService {
    List<Event> findAll();

    Event get(Long id) throws EventNotFoundException;

    Event create(Event event);

    Event update(Long id, Event event) throws EventNotFoundException;

    Event delete(Long id) throws EventNotFoundException;

    long deleteAll();
}
