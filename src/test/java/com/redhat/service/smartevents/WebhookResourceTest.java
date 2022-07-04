package com.redhat.service.smartevents;

import java.util.ArrayList;
import java.util.List;

import com.redhat.service.smartevents.performance.webhook.exceptions.EventNotFoundException;
import com.redhat.service.smartevents.performance.webhook.models.Event;
import com.redhat.service.smartevents.performance.webhook.models.WebhookRequest;
import com.redhat.service.smartevents.performance.webhook.services.WebhookService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.vertx.core.json.Json;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class WebhookResourceTest {

    @InjectMock
    WebhookService webhookService;

    @BeforeEach
    void init() {
        RestAssured.basePath = "/rest";
    }

    @Test
    void testGetAll() {
        given()
            .when()
            .get("/webhook")
            .then().statusCode(HttpStatus.SC_OK)
            .body("", hasSize(0));

        List<Event> events = new ArrayList<>();
        events.add(new Event().setBridgeId("bridgeId-1"));
        events.add(new Event().setBridgeId("bridgeId-2"));
        Mockito.when(webhookService.findAll()).thenReturn(events);

        String results = given()
                .when()
                .get("/webhook")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        Event[] resultEvents = Json.decodeValue(results, Event[].class);
        assertThat(resultEvents, arrayContaining(events.toArray()));
    }

    @Test
    void testGet() throws EventNotFoundException {
        Event event = new Event().setBridgeId("bridgeId-1");
        Mockito.when(webhookService.get(1L)).thenThrow(new EventNotFoundException(1L)).thenReturn(event);
        given()
            .when()
            .get("/webhook/1")
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);

        String result = given()
                .when()
                .get("/webhook/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asString();
        Event resultEvent = Json.decodeValue(result, Event.class);
        assertThat(resultEvent, Matchers.is(event));
    }

    @Test
    void testConsumeEvent() {
        Event expectedEvent = new Event();
        expectedEvent.setBridgeId("bridgeId-1");

        Mockito.when(webhookService.create(expectedEvent)).thenReturn(expectedEvent);

        WebhookRequest request = new WebhookRequest("bridgeId-1");
        String result = given()
                .when()
                .body(Json.encode(request))
                .contentType(ContentType.JSON)
                .post("/webhook")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().asString();
        Event response = Json.decodeValue(result, Event.class);

        assertThat(response, Matchers.is(expectedEvent));
    }

    @Test
    void testDelete() throws EventNotFoundException {
        Event expectEvent = new Event();
        expectEvent.setId(1L);
        Mockito.when(webhookService.delete(1L)).thenReturn(expectEvent);

        String result = given()
                .when()
                .contentType(ContentType.JSON)
                .delete("/webhook/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
        Event response = Json.decodeValue(result, Event.class);
        assertThat(response, Matchers.is(expectEvent));
    }

    @Test
    void testDeleteAll() {
        Mockito.when(webhookService.deleteAll()).thenReturn(1L);

        String result = given()
                .when()
                .contentType(ContentType.JSON)
                .delete("/webhook")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().asString();
        Long response = Json.decodeValue(result, Long.class);

        assertThat(response, Matchers.is(1L));
    }

}