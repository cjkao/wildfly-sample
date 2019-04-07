package sample;

// import static org.hamcrest.CoreMatchers.containsString;
// import static org.hamcrest.CoreMatchers.everyItem;
// import static org.hamcrest.CoreMatchers.hasItem;
// import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.SseEventSource;

import org.junit.ClassRule;
import org.junit.Test;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
// import static io.specto.hoverfly.junit.core.HoverflyConfig.localConfigs;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
public class AsyncTest {
	@ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inSimulationMode(dsl(
        service("www.my-test.com")
            .get("/app/app/orders")
			.willReturn(success("{\"bookingId\":\"1\"}", "application/json"))
            .get("/app/app/events")
			.willReturn(success("{\"bookingId\":\"1\"}", "application/json"))

    ));
	@Test
	public void testAsyncs() {
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target("http://www.my-test.com/app/app/orders");
		long start = System.currentTimeMillis();
		Response response = target.request().get(Response.class);
		String responseString = response.readEntity(String.class);
		System.out.println("response: " + responseString);
		System.out.println("time taken: " + (System.currentTimeMillis() - start));
		System.out.println("main method exits");

	}

	@Test
	public void testWithHamcrest() {
		WebTarget target = ClientBuilder.newClient().target("http://www.my-test.com/app/app/events");

		try (SseEventSource eventSource = SseEventSource.target(target).build()) {

			// EventSource#register(Consumer<InboundSseEvent>)
			// Registered event handler will print the received message.
			eventSource.register(System.out::println);

			// Subscribe to the event stream.
			eventSource.open();

			// Consume events for just 500 ms
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}