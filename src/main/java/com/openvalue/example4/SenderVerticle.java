package com.openvalue.example4;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.logging.Logger;

public class SenderVerticle extends AbstractVerticle {

  private static final Logger log = Logger.getLogger(SenderVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    router.route(HttpMethod.GET, "/send/:message")
        .handler(this::sendMessage);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8083)
      .onSuccess(httpServer -> {
        log.info("Http server started on port " + httpServer.actualPort());
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  private void sendMessage(RoutingContext routingContext) {
    final String message = routingContext.pathParam("message") != null ? routingContext.pathParam("message") : "No message found";
    final EventBus eventBus = vertx.eventBus();

    JsonObject jsonMessage = new JsonObject();
    jsonMessage.put("message", message);
    jsonMessage.put("sentAt", System.currentTimeMillis());

    eventBus.request("incoming.message.event", jsonMessage, reply -> {
      if(reply.succeeded()) {
       routingContext.json(jsonMessage);
      } else {
        log.severe("Did not receive a reply before timeout");
      }
    });
  }
}
