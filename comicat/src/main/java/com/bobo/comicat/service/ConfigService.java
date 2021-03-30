package com.bobo.comicat.service;

import com.bobo.comicat.common.base.BaseBean;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.bobo.comicat.common.constant.Constant.EVENT_BUS_WRITER_CONFIG_ADDRESS;

/**
 * 配置文件类
 *
 * @author BO
 * @date 2021-03-25 10:13
 * @since 2021/3/25
 **/
public class ConfigService extends BaseBean {
  public ConfigService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }


  public void getConfig(RoutingContext routingContext) {
    responseSuccess(routingContext.response(), config);
  }

  public void setConfig(RoutingContext routingContext) {
    routingContext.getBodyAsJson();
    eventBus.request(EVENT_BUS_WRITER_CONFIG_ADDRESS, routingContext.getBodyAsJson())
      .onFailure(f -> responseError(routingContext.response(), f))
      .onSuccess(su -> {
        ((JsonObject) su.body()).getMap().forEach(config::put);
        responseSuccess(routingContext.response(), su.body());
      });
  }
}
