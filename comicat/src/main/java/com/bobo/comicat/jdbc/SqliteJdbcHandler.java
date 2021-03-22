package com.bobo.comicat.jdbc;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.base.MyCompositeFuture;
import com.bobo.comicat.common.entity.ComicsQuery;
import com.bobo.comicat.handler.JdbcHandler;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

import java.util.ArrayList;

import static com.bobo.comicat.common.JdbcConstant.QUERY_COMICS_PAGE;
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
    CREATE TABLE IF NOT EXISTS "chapter" (
      "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
      "comics_id" INTEGER NOT NULL,
      "chapter_name" TEXT,
      "status" TEXT,
      "chapter_index" INTEGER,
      "page_number" INTEGER
    );
    """, """
    CREATE TABLE IF NOT EXISTS "comics" (
      "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
      "comics_name" TEXT,
      "comics_author" TEXT,
      "comics_tags" TEXT,
      "status" TEXT,
      "create_time" DATE,
      "resource_type" TEXT,
      "resource_path" TEXT
    );
    """, """
    CREATE TABLE IF NOT EXISTS "reading_record" (
      "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
      "comics_id" INTEGER,
      "chapter_id" INTEGER,
      "position" TEXT,
      "recording_time" DATE
    );
    """, """
    CREATE TABLE IF NOT EXISTS "tag" (
      "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
      "name" TEXT NOT NULL
    );
    """
  };

  private JDBCClient jdbcClient;

  @Override
  public Future<JdbcHandler> init() {
    Promise<JdbcHandler> promise = Promise.promise();
    JsonObject sqliteConfig = new JsonObject()
      .put("url", "jdbc:sqlite:" + SQLITE_PATH)
      .put("driver_class", "org.sqlite.JDBC")
      .put("max_pool_size", 30);
    jdbcClient = JDBCClient.createShared(vertx, sqliteConfig);
    ArrayList<Future<Void>> futures = new ArrayList<>(createTable.length);
    for (String s : createTable) {
      Promise<Void> promise2 = Promise.promise();
      futures.add(promise2.future());
      jdbcClient.querySingle(s, res -> {
        if (res.succeeded()) {
          promise2.complete();
        } else {
          res.cause().printStackTrace();
          promise2.fail(res.cause());
        }
      });
    }
    MyCompositeFuture.join(futures).onSuccess(su -> promise.complete(this)).onFailure(promise::fail);
    return promise.future();
  }

  @Override
  public void registrationService() {
    //查询漫画列表 分页
    eventBus.consumer(QUERY_COMICS_PAGE, this::queryComicsPage);

  }


  private void queryComicsPage(Message<ComicsQuery> message) {
    ComicsQuery body = message.body();
    //先查询数据总数
    String selectAll = "select count(1) from comics";
    jdbcClient.querySingle(selectAll, selectAllRes -> {
      if (selectAllRes.succeeded()) {
        JsonArray result = selectAllRes.result();
        System.out.println(result);
      } else {

      }
    });
  }
}
