package com.redhat.service.smartevents;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.service.smartevents.performance.webhook.exceptions.EventNotFoundException;
import com.redhat.service.smartevents.performance.webhook.models.Event;
import com.redhat.service.smartevents.performance.webhook.models.WebhookRequest;
import com.redhat.service.smartevents.performance.webhook.services.WebhookService;
import lombok.extern.slf4j.Slf4j;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class WebhookResource {

    @Inject
    WebhookService webhookService;

    @GET
    public Response getAll() {
        log.info("getting all events");
        return Response.ok(webhookService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Event event;
        try {
            log.info("getting id event {}", id);
            event = webhookService.get(id);
            log.info("returning event {}", event);
        } catch (EventNotFoundException e) {
            return Response.status(NOT_FOUND).build();
        }
        return Response.ok(event).build();
    }

    @POST
    public Response consumeEvent(WebhookRequest request) {
        log.info("request received {}", request);
        return Response.status(CREATED).entity(webhookService.create(request.toEntity())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            log.info("deleting event id {}", id);
            return Response.ok(webhookService.delete(id)).build();
        } catch (EventNotFoundException e) {
            return Response.status(NOT_FOUND).build();
        }
    }

    @DELETE
    public Response deleteAll() {
        log.info("deleting all events");
        return Response.ok(webhookService.deleteAll()).build();
    }
}