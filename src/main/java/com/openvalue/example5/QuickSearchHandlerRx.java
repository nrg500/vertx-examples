package com.openvalue.example5;

import io.reactivex.Single;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class QuickSearchHandlerRx implements Handler<HttpServerRequest> {

  Logger log = Logger.getLogger(QuickSearchHandlerRx.class.getName());
  private final Vertx vertx;

  public QuickSearchHandlerRx(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void handle(HttpServerRequest event) {
    MultiMap queryParams = event.params();
    String search = queryParams.contains("q") ? queryParams.get("q") : "";
    WebClientOptions options = new WebClientOptions()
      .setKeepAlive(false);

    WebClient client = WebClient.create(vertx, options);

    Single<HttpResponse<Buffer>> request = client.get("www.google.com", "/search?q=" + search)
      .rxSend();

    Single<JsonObject> result = request.map(bufferHttpResponse ->
      new JsonObject().put("statusCode", bufferHttpResponse.statusCode())
        .put("contentPreview",  Arrays.stream(bufferHttpResponse.bodyAsString().split("/n")).limit(10).collect(Collectors.joining("\n"))));

    result.subscribe(jsonObject -> event.response()
      .putHeader("Content-Type", "application/json")
      .end(jsonObject.encode()), error -> log.severe(error.getMessage()));
  }
}
