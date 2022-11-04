package com.openvalue.example4;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.util.logging.Logger;

public class ReceiverVerticle extends AbstractVerticle {

  private static final Logger log = Logger.getLogger(ReceiverVerticle.class.getName());

  @Override
  public void start()  {
    vertx.eventBus().consumer("incoming.message.event", this::onMessage);
  }
  //  onMessage() will be called when a message is received.
  private <T> void onMessage(Message<T> tMessage) {
    JsonObject message = (JsonObject) tMessage.body();
    log.info(() -> "Message Received " + message);
    tMessage.reply(message);
  }
}
