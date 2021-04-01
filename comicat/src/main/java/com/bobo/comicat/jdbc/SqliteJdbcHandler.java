package com.bobo.comicat.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.base.MyCompositeFuture;
import com.bobo.comicat.common.entity.ComicsQuery;
import com.bobo.comicat.common.entity.TagQuery;
import com.bobo.comicat.handler.JdbcHandler;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bobo.comicat.common.constant.Constant.SQLITE_PATH;
import static com.bobo.comicat.common.constant.JdbcConstant.*;

/**
 * @author BO
 * @date 2021-03-19 10:54
 * @since 2021/3/19
 **/
@Slf4j
public class SqliteJdbcHandler extends BaseBean implements JdbcHandler {
  public SqliteJdbcHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  private final String[] createTable = {
    "CREATE TABLE IF NOT EXISTS chapter (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,comics_id INTEGER NOT NULL,chapter_name TEXT,status TEXT,chapter_index INTEGER,page_number INTEGER);",
    "CREATE TABLE IF NOT EXISTS comics (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,comics_name TEXT,comics_author TEXT,comics_tags TEXT,status TEXT,create_time DATE,resource_type TEXT,resource_path TEXT,file_type TEXT,file_path TEXT);",
    "CREATE TABLE IF NOT EXISTS reading_record (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,comics_id INTEGER,chapter_id INTEGER,position TEXT,recording_time DATE);",
    "CREATE TABLE IF NOT EXISTS tag (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL);"
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
    log.info("registrationService");
    //查询漫画列表 分页
    eventBus.consumer(QUERY_COMICS_COUNT, this::queryComicsCount);
    eventBus.consumer(QUERY_COMICS_PAGE, this::queryComicsPage);
    eventBus.consumer(QUERY_COMICS_TAGS, this::queryComicsTags);
    eventBus.consumer(INSERT_COMICS, this::insertComics);

    eventBus.consumer(QUERY_TAGS, this::queryTags);

  }

  private void insertComics(Message<String> message) {
    ComicsQuery comicsQuery = JSONUtil.toBean(message.body(), ComicsQuery.class);
    String insertSql = "INSERT INTO comics (comics_name,comics_author,comics_tags,status,create_time,grade_type,cover_image,description) VALUES (?,?,?,?,?,?,?,?);";
    List<Object> params = new ArrayList<>();
    params.add(comicsQuery.getComicsName());
    params.add(comicsQuery.getComicsAuthor());
    params.add(comicsQuery.getComicsTags());
    params.add(comicsQuery.getStatus());
    params.add(comicsQuery.getCreateTime().toString());
    params.add(comicsQuery.getGradeType());
    params.add(comicsQuery.getCoverImage());
    params.add(comicsQuery.getDescription());
    jdbcClient.querySingleWithParams(insertSql,new JsonArray(params),res->{
      if (res.succeeded()) {
        message.reply("");
      } else {
        message.fail(500,res.cause().getMessage());
      }
    });

  }

  private void queryComicsTags(Message<String> message) {
    String sql = "select comics_tags from comics";
    jdbcClient.query(sql, res -> {
      if (res.succeeded()) {
        message.reply(new JsonArray(res.result().getResults()));
      } else {
        message.fail(500, res.cause().getMessage());
      }
    });
  }


  private void queryTags(Message<JsonObject> message) {
    TagQuery tagQuery = JSONUtil.toBean(message.body().toString(), TagQuery.class);
    String select = "select * from tag";
    String whereSql = "";
    List<Object> params = new ArrayList<>();
    if (StrUtil.isNotEmpty(tagQuery.getName())) {
      whereSql += "name = ?";
      params.add(tagQuery.getName());
    }
    if (StrUtil.isNotEmpty(tagQuery.getGradeType())) {
      if (StrUtil.isNotEmpty(whereSql)) {
        whereSql += " and ";
      }
      whereSql += "grade_type = ?";
      params.add(tagQuery.getGradeType());
    }
    if (StrUtil.isNotEmpty(whereSql)) {
      select += " where ";
      select += whereSql;
    }
    jdbcClient.queryWithParams(select, new JsonArray(params), res -> {
      if (res.succeeded()) {
        message.reply(new JsonArray(res.result().getRows()));
      } else {
        message.fail(500, res.cause().getMessage());
      }
    });

  }

  private void queryComicsPage(Message<JsonObject> message) {
    ComicsQuery comicsQuery = JSONUtil.toBean(message.body().toString(), ComicsQuery.class);
    //先查询数据总数
    String selectPage = "select * from comics";
    List<Object> list = new ArrayList<>();
    String whereSql = getComicsWhereSql(comicsQuery, list);
    if (StrUtil.isNotEmpty(whereSql)) {
      selectPage += " where " + whereSql;
    }
    selectPage += " limit ?,?";
    list.add((comicsQuery.getPageNumber() - 1) * comicsQuery.getPageSize());
    list.add(comicsQuery.getPageSize());
    jdbcClient.queryWithParams(selectPage, new JsonArray(list), selectPageRes -> {
      if (selectPageRes.succeeded()) {
        message.reply(new JsonArray(selectPageRes.result().getRows()));
      } else {
        message.fail(500, selectPageRes.cause().getMessage());
      }
    });
  }

  private void queryComicsCount(Message<JsonObject> message) {
    ComicsQuery comicsQuery = JSONUtil.toBean(message.body().toString(), ComicsQuery.class);
    //先查询数据总数
    String selectCount = "select count(1) from comics";
    List<Object> list = new ArrayList<>();
    String whereSql = getComicsWhereSql(comicsQuery, list);
    if (StrUtil.isNotEmpty(whereSql)) {
      selectCount += " where " + whereSql;
    }
    jdbcClient.querySingleWithParams(selectCount, new JsonArray(list), selectCountRes -> {
      if (selectCountRes.succeeded()) {
        message.reply(selectCountRes.result().getInteger(0));
      } else {
        message.fail(500,selectCountRes.cause().getMessage());
      }
    });
  }

  private String getComicsWhereSql(ComicsQuery comicsQuery, List<Object> list) {
    String whereSql = "";
    if (StrUtil.isNotEmpty(comicsQuery.getComicsName())) {
      whereSql = "comics_name like '%' || ? || '%' ";
      list.add(comicsQuery.getComicsName());
    }
    if (CollectionUtil.isNotEmpty(comicsQuery.getComicsTagList())) {
      if (comicsQuery.getComicsTagList().size() == 1) {
        if (StrUtil.isNotEmpty(whereSql)) {
          whereSql += " and ";
        }
        whereSql += "comics_tags like '%,' || ? || ',%' ";
        list.add(comicsQuery.getComicsTagList().get(0));
      } else {
        if (StrUtil.isNotEmpty(comicsQuery.getTagLogic())) {
          comicsQuery.setTagLogic("or");
        }
        whereSql += "(";
        String and = comicsQuery.getComicsTagList().stream().map(s -> {
          list.add(s);
          return " comics_tags like '%,' || ? || ',%' ";
        }).collect(Collectors.joining(comicsQuery.getTagLogic()));
        whereSql += and;
        whereSql += ")";
      }
    }
    return whereSql;
  }
}
