package com.openvalue.example2;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class GreetingHandler implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext context) {
    String address = context.request().connection().remoteAddress().toString();

    MultiMap queryParams = context.queryParams();
    String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";

    context.json(new JsonObject()
      .put("name", name)
      .put("address", address)
      .put("message", "Hello " + name + " connected from " + address));
  }
}
