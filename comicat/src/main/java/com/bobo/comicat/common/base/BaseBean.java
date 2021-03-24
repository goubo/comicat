package com.bobo.comicat.common.base;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

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

  protected void responseSuccess(HttpServerResponse response, Object object) {
    response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200).end(Json.encode(object));
  }

  protected void responseError(HttpServerResponse response, int code, String msg) {
    response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(code).end(msg);
  }

  protected void responseError(HttpServerResponse response, Throwable throwable) {
    responseError(response, 500, throwable.getMessage());
  }


}
