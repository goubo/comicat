package com.bobo.comicat.handler;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.jdbc.MysqlJdbcHandler;
import com.bobo.comicat.jdbc.SqliteJdbcHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import static com.bobo.comicat.common.constant.Constant.DATABASE_TYPE_KEY;
import static com.bobo.comicat.common.constant.Constant.DATABASE_TYPE_MYSQL;

/**
 * @author BO
 * @date 2021-03-19 10:47
 * @since 2021/3/19
 **/
public class DataBaseHandler extends BaseBean {
  public DataBaseHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
    getHandler(config.getString(DATABASE_TYPE_KEY)).init().onFailure(f -> {
      f.printStackTrace();
    }).onSuccess(s -> {

    });

  }


  private JdbcHandler getHandler(String type) {
    if (DATABASE_TYPE_MYSQL.equals(type)) {
      return new MysqlJdbcHandler(vertx, config);
    } else {
      return new SqliteJdbcHandler(vertx, config);
    }
  }


}
