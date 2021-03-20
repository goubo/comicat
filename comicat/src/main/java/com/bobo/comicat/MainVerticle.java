package com.bobo.comicat;

import com.bobo.comicat.handler.ApiRouterHandler;
import com.bobo.comicat.handler.ConfigHandler;
import com.bobo.comicat.handler.DataBaseHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import static com.bobo.comicat.common.constant.Constant.EVENT_BUS_LOAD_CONFIG;

/**
 * @author qhong
 */
public class MainVerticle extends AbstractVerticle {


  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(MainVerticle.class.getName());
  }

  private EventBus eventBus;

  @Override
  public void start(Promise<Void> startPromise) {
    eventBus = vertx.eventBus();
    new ConfigHandler(vertx, config());
    eventBus.request(EVENT_BUS_LOAD_CONFIG, "").onSuccess(s -> {
      new DataBaseHandler(vertx, config());
      new ApiRouterHandler(vertx, config()).Router();
    });


  }

}
