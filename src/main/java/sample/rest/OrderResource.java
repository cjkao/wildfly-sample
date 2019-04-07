package sample.rest;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
@Path("/orders")
@Slf4j
public class OrderResource {

  @GET
  public void getAllOrders(@Suspended final AsyncResponse ar) {
      System.out.println("-- getAllOrders() resource method --");
      System.out.println("AsyncResponse#suspended=" + ar.isSuspended());

      ExecutorService es = Executors.newSingleThreadExecutor();
      es.submit(() -> {
          try {
              //Simulating a long running process
              log.debug("long long process...");
              Thread.sleep( new Random().nextInt(3000));
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          ar.resume("all orders data....");
          es.shutdown();
      });
  }
}