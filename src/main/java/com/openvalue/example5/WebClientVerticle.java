package com.openvalue.example5;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import java.util.logging.Logger;

public class WebClientVerticle extends AbstractVerticle {
  private static final Logger log = Logger.getLogger(WebClientVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.route(HttpMethod.GET, "/quicksearch")
      .handler(new QuickSearchHandler());

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8084)
      .onSuccess(server -> {
        log.info("HTTP server started on port " + server.actualPort());
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }
}
