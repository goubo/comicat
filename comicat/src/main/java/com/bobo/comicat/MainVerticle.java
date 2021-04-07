package com.bobo.comicat;

import cn.hutool.core.util.StrUtil;
import com.bobo.comicat.handler.ApiRouterHandler;
import com.bobo.comicat.handler.ConfigHandler;
import com.bobo.comicat.handler.DataBaseHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import static com.bobo.comicat.common.constant.Constant.EVENT_BUS_LOAD_CONFIG;

/**
 * @author qhong
 */
public class MainVerticle extends AbstractVerticle {


  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) {
    new ConfigHandler(vertx, config());
    vertx.eventBus().request(EVENT_BUS_LOAD_CONFIG, StrUtil.EMPTY).onSuccess(s -> {
      new DataBaseHandler(vertx, config());
      new ApiRouterHandler(vertx, config()).router();
    });


  }

}
