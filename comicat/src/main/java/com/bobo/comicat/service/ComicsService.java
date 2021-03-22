package com.bobo.comicat.service;

import com.bobo.comicat.common.base.BaseBean;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * 漫画类
 *
 * @author BO
 * @date 2021-03-21 09:10
 * @since 2021/3/21
 **/
public class ComicsService extends BaseBean {
  protected ComicsService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }
}
