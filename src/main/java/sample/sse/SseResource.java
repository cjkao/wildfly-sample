/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sample.sse;

import java.io.IOException;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

/**
 *
 * see: https://www.infoq.com/news/2017/08/JAX-RS-2.1-released
 *
 * @author hantsy
 */
@Path("events")
@RequestScoped
public class SseResource {

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void eventStream(@Context Sse sse, @Context SseEventSink eventSink) {
        // Resource method is invoked when a client subscribes to an event stream.
        // That implies that sending events will most likely happen from different
        // context - thread / event handler / etc, so common implementation of the
        // resource method will store the eventSink instance and the application
        // logic will retrieve it when an event should be emitted to the client.

        // sending events:
        eventSink.send(sse.newEvent("event1"));
    new Thread(new Runnable() {
                final SseEventSink sink2=eventSink;
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        // ... code that waits 1 second
                        var eventBuilder = sse.newEventBuilder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class, "Hello world " + i + "!").id("" +i).reconnectDelay(10000);
                        sink2.send(eventBuilder.build());
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    sink2.close();
                }
            }).start();

        SseEventSink sink = eventSink;
        // try (SseEventSink sink = eventSink) {
            sink.send(sse.newEvent("data"));
            sink.send(sse.newEvent("MyEventName", "more data"));

            OutboundSseEvent event = sse.newEventBuilder().id("EventId").name("EventName").data("Data")
                    .reconnectDelay(10000).comment("Anything i wanna comment here!").build();

            sink.send(event);
        
        // }

    }

}
