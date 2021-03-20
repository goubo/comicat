package com.bobo.comicat.jdbc;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.handler.JdbcHandler;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

import static com.bobo.comicat.common.constant.Constant.SQLITE_PATH;

/**
 * @author BO
 * @date 2021-03-19 10:54
 * @since 2021/3/19
 **/
public class SqliteJdbcHandler extends BaseBean implements JdbcHandler {
  public SqliteJdbcHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  private final String[] createTable = {"""
      CREATE TABLE IF NOT EXISTS "comics"(
        "id"
      integer NOT
      NULL,
        "comics_name"TEXT,
        "comics_author"TEXT,
        "comics_tags"TEXT,
        "status"TEXT,
        "create_time"DATE,
        "file_type"TEXT,
        "file_path"TEXT,
      PRIMARY KEY("id")
    );
    """,
    """
"""
  };

  private JDBCClient shared;

  @Override
  public Future<Void> init() {
    Promise<Void> promise = Promise.promise();
    JsonObject sqliteConfig = new JsonObject()
      .put("url", "jdbc:sqlite:" + SQLITE_PATH)
      .put("driver_class", "org.sqlite.JDBC")
      .put("max_pool_size", 30);
    shared = JDBCClient.createShared(vertx, sqliteConfig);
    for (String s : createTable) {
      shared.query(s, res -> {
      });
    }
    return promise.future();
  }
}
