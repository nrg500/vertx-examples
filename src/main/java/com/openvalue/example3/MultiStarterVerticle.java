package com.openvalue.example3;

import com.openvalue.example1.GreetingVerticle;
import com.openvalue.example2.SeparateHandlerVerticle;
import com.openvalue.example4.ReceiverVerticle;
import com.openvalue.example4.SenderVerticle;
import com.openvalue.example5.WebClientVerticle;
import com.openvalue.example5.WebClientVerticleRx;
import io.vertx.core.AbstractVerticle;

public class MultiStarterVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.deployVerticle(new GreetingVerticle());
    vertx.deployVerticle(new SeparateHandlerVerticle());
    vertx.deployVerticle(new SenderVerticle());
    vertx.deployVerticle(new ReceiverVerticle());
    vertx.deployVerticle(new WebClientVerticle());
    vertx.deployVerticle(new WebClientVerticleRx());
  }
}
