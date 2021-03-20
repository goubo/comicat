package com.bobo.comicat.handler;

import io.vertx.core.Future;

/**
 * jdbcHandler
 *
 * @author BO
 * @date 2021-03-19 10:52
 * @since 2021/3/19
 **/
public interface JdbcHandler {
  /**
   * 初始化数据库
   *
   * @return
   */
  Future<Void> init();
}
