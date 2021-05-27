package com.bobo.comicat.common.base;

import com.bobo.comicat.common.entity.Result;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import static com.bobo.comicat.common.constant.ApiConstant.*;

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
  protected String basePath;


  protected BaseBean(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    this.eventBus = vertx.eventBus();
    this.basePath = config.getString("basePath");
  }

  protected void responseSuccess(HttpServerResponse response, Object object) {
    Result build = Result.builder().data(object).code(RESULT_CODE_200).msg(RESULT_SUCCESS).build();
    response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(build.getCode()).end(build.toJsonString());
  }

  protected void responseError(HttpServerResponse response, int code, String msg) {
    Result build = Result.builder().code(code).msg(msg).build();
    response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(build.getCode()).end(build.toJsonString());
  }

  protected void responseError(HttpServerResponse response, Throwable throwable) {
    Result build = Result.builder().code(RESULT_CODE_500).msg(throwable.getMessage()).build();
    response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(build.getCode()).end(build.toJsonString());
  }


}
