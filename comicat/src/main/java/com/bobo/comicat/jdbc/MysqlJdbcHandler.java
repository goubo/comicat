package com.bobo.comicat.jdbc;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.handler.JdbcHandler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author BO
 * @date 2021-03-19 10:58
 * @since 2021/3/19
 **/
public class MysqlJdbcHandler extends BaseBean implements JdbcHandler {
  public MysqlJdbcHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  @Override
  public Future<Void> init() {
    return null;
  }
}
