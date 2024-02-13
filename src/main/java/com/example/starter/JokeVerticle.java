package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

public class JokeVerticle extends AbstractVerticle {

  private HttpRequest<JsonObject> httpRequest;
  public void start(){
    httpRequest = WebClient.create(vertx)
      .get(443, "icanhazdadjoke.com", "/")
      .ssl(true)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject())
      .expect(ResponsePredicate.SC_OK);

    vertx.setPeriodic(3000, id -> fetchJoke());
  }

  private void fetchJoke() {
    httpRequest.send(httpResponseAsyncResult -> {
      if(httpResponseAsyncResult.succeeded()){
        System.out.println(httpResponseAsyncResult.result().body().getString("joke"));
        System.out.println();
      }
    });
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new JokeVerticle());
  }
}
