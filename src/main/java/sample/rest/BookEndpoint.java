package sample.rest;

import com.google.common.collect.ImmutableMap;
import io.opentracing.contrib.okhttp3.TracingCallFactory;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sample.domain.BookManager;
import sample.bean.AccountService;
import sample.domain.Book;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Random;

@Path("books")
@RequestScoped
public class BookEndpoint {

    @Inject
    private io.opentracing.Tracer tracer;
    @Inject
    AccountService accountService;
    @Inject
    private BookManager bookManager;

    @Inject
    @ConfigProperty(name = "orderService")
    private String orderService;



    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") String id) {
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
            
            

            accountService.onNewOrder();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            span.finish();
        }

        return Response.ok(bookManager.get(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(bookManager.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Book book) {
        book.getId();
        String bookId = bookManager.add(book);
        return Response.created(UriBuilder.fromResource(this.getClass()).path(bookId).build()).build();
    }
}