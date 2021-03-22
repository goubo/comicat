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
   * @return 返回初始化结果
   */
  Future<JdbcHandler> init();

  /**
   * 注册数据库服务
   */
  void registrationService();
}
