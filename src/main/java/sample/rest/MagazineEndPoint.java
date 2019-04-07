package sample.rest;


import com.google.common.collect.ImmutableMap;
import io.opentracing.contrib.okhttp3.TracingCallFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sample.domain.MagManager;
import sample.domain.Magazine;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Random;

@Path("mag")
@RequestScoped
public class MagazineEndPoint {

    @Inject
    private io.opentracing.Tracer tracer;

    @Inject
    @ConfigProperty(name = "orderService")
    private String orderService;

    @Inject
    private MagManager magManager;


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIssue(@PathParam("id") String id) {
        var span = tracer.buildSpan("formatString").start();
        span.setTag("ext-to", "---http call---------hey-----");
        try {
            span.log(ImmutableMap.of("event", "string-format", "value", id));
            Thread.sleep(new Random().nextInt(100));

            var okHttpClient = new OkHttpClient();
            Call.Factory client = new TracingCallFactory(okHttpClient, tracer);
            var request = new Request.Builder()
                    .url(orderService)
                    .build();
            var resp=client.newCall(request).execute() ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            span.finish();
        }

        return Response.ok(magManager.get(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(magManager.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Magazine book) {
//        book.getId();
        String bookId = magManager.add(book);
        return Response.created(UriBuilder.fromResource(this.getClass()).path(bookId).build()).build();
    }
}
