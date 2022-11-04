package com.openvalue.example1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import java.util.logging.Logger;

public class GreetingVerticle extends AbstractVerticle {
  private static final Logger log = Logger.getLogger(GreetingVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    router.route(HttpMethod.GET, "/api")
        .handler(context -> {
          String address = context.request().connection().remoteAddress().toString();

          MultiMap queryParams = context.queryParams();
          String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";

          context.json(new JsonObject()
            .put("name", name)
            .put("address", address)
            .put("message", "Hello " + name + " connected from " + address));
        });

    vertx.createHttpServer()
        .requestHandler(router)
          .listen(8080)
            .onSuccess(server -> {
              log.info("HTTP server started on port " + server.actualPort());
              startPromise.complete();
            })
      .onFailure(startPromise::fail);
  }
}
