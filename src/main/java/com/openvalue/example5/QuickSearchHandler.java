package com.openvalue.example5;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QuickSearchHandler implements Handler<RoutingContext> {

  Logger log = Logger.getLogger(QuickSearchHandler.class.getName());

  @Override
  public void handle(RoutingContext context) {
    MultiMap queryParams = context.queryParams();
    String search = queryParams.contains("q") ? queryParams.get("q") : "";
    WebClientOptions options = new WebClientOptions()
      .setKeepAlive(false);

    WebClient client = WebClient.create(context.vertx(), options);

    client.get("www.google.com", "/search?q=" + search)
      .send()
      .onSuccess(response -> {
        log.info("Received response with status code: " + response.statusCode());
        String[] lines = response.bodyAsString().split("/n");
        context.json(new JsonObject().put("statusCode", response.statusCode()).put("contentPreview", Arrays.stream(lines).limit(10).collect(Collectors.joining("\n"))));
      })
      .onFailure(err -> log.severe("Something went wrong " + err.getMessage()));

//    Future<HttpResponse<Buffer>> request1 = client.get("www.google.com", "/search?q=" + search)
//        .send()
//      .compose((response) -> client.get("www.google.com", "/search?q=" + search)
//        .send());
  }
}
