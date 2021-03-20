package com.bobo.comicat.handler;

import com.bobo.comicat.common.base.BaseBean;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * 路由
 *
 * @author BO
 * @date 2021-03-19 15:42
 * @since 2021/3/19
 **/
public class ApiRouterHandler extends BaseBean {

  public ApiRouterHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
    vertx.createHttpServer().listen(47373);

  }

  public void Router() {

  }
}
