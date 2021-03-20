package com.bobo.comicat.common.base;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.io.File;

/**
 * 根类
 *
 * @author BO
 * @date 2021-03-19 10:19
 * @since 2021/3/19
 **/
public abstract class BaseBean {


  protected Vertx vertx;
  protected EventBus eventBus;
  protected JsonObject config;


  protected BaseBean(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    this.eventBus = vertx.eventBus();

  }
}
