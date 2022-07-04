package com.redhat.service.smartevents.performance.webhook.services;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.redhat.service.smartevents.performance.webhook.exceptions.EventNotFoundException;
import com.redhat.service.smartevents.performance.webhook.models.Event;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class WebhookServiceImpl implements WebhookService {

    @Override
    public List<Event> findAll() {
        return Event.findAll().list();
    }

    @Override
    public Event get(Long id) throws EventNotFoundException {
        Optional<Event> event = Event.findByIdOptional(id);
        return event.orElseThrow(() -> new EventNotFoundException(id));
    }

    @Override
    @Transactional
    public Event create(Event event) {
        event.persist();
        log.info("event persisted {}", event.getId());
        return event;
    }

    @Override
    @Transactional
    public Event update(Long id, Event event) throws EventNotFoundException {
        Event current = get(id);
        current.copy(event).persist();
        log.info("event updated {}", event.getId());
        return current;
    }

    @Override
    @Transactional
    public Event delete(Long id) throws EventNotFoundException {
        Event event = get(id);
        event.delete();
        log.info("event deleted {}", event.getId());
        return event;
    }

    @Override
    @Transactional
    public long deleteAll() {
        long deletedRows = Event.deleteAll();
        log.info("rows deleted {}", Event.deleteAll());
        return deletedRows;
    }
}