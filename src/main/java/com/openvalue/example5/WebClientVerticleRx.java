package com.openvalue.example5;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import java.util.logging.Logger;

public class WebClientVerticleRx extends AbstractVerticle {
  private static final Logger log = Logger.getLogger(WebClientVerticleRx.class.getName());

  @Override
  public void start() {

    vertx.createHttpServer()
      .requestHandler(new QuickSearchHandlerRx(vertx))
      .rxListen(8085)
      .subscribe(server -> {
        log.info("HTTP server started on port " + server.actualPort());
      }, error -> {
        log.severe("Failed to start WebclientVerticleRx!");
      });
  }
}
