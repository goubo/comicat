package com.bobo.comicat.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.base.MyCompositeFuture;
import com.bobo.comicat.common.entity.ChapterQuery;
import com.bobo.comicat.common.entity.ComicsQuery;
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
  private final String[] createTable = {
    "CREATE TABLE IF NOT EXISTS chapter (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,comics_id INTEGER NOT NULL,chapter_index INTEGER,chapter_name TEXT,status TEXT,page_number INTEGER,file_type TEXT,file_path TEXT,chapter_path TEXT,chapter_type TEXT)",
    "CREATE TABLE IF NOT EXISTS comics (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,comics_name TEXT,comics_author TEXT,comics_tags TEXT,status TEXT,create_time DATE,resource_type TEXT,resource_path TEXT,file_type TEXT,file_path TEXT);",
    "CREATE TABLE IF NOT EXISTS reading_record (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,comics_id INTEGER,chapter_id INTEGER,position TEXT,recording_time DATE);",
    "CREATE TABLE IF NOT EXISTS tag (id integer NOT NULL PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL);"
  };
  private JDBCClient jdbcClient;

  public SqliteJdbcHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

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
    eventBus.consumer(QUERY_COMICS_COUNT, this::queryComicsCount);
    eventBus.consumer(QUERY_COMICS_PAGE, this::queryComicsPage);
    eventBus.consumer(QUERY_COMICS_TAGS, this::queryComicsTags);
    eventBus.consumer(INSERT_COMICS, this::insertComics);
    eventBus.consumer(UPDATE_COMICS, this::updateComics);

    eventBus.consumer(INSERT_CHAPTER, this::insertChapter);
    eventBus.consumer(QUERY_CHAPTER, this::queryChapter);
    eventBus.consumer(DELETE_CHAPTER, this::deleteChapter);


  }

  private void deleteChapter(Message<String> message) {
    ChapterQuery chapter = JSONUtil.toBean(message.body(), ChapterQuery.class);
    String querySql = "DELETE FROM chapter WHERE id = 4";


  }

  private void queryChapter(Message<String> message) {
    ChapterQuery chapter = JSONUtil.toBean(message.body(), ChapterQuery.class);
    String querySql = "SELECT  * FROM chapter where comics_id = ? ";
    jdbcClient.queryWithParams(querySql, new JsonArray().add(chapter.getComicsId()), res -> {
      if (res.succeeded()) {
        message.reply(new JsonArray(res.result().getRows()));
      } else {
        message.fail(500, res.cause().getMessage());
      }
    });
  }

  private void insertChapter(Message<String> message) {
    ChapterQuery chapter = JSONUtil.toBean(message.body(), ChapterQuery.class);
    String insertSql = "INSERT INTO chapter (comics_id,chapter_index,chapter_name,status,page_number,file_type,file_path,chapter_path,chapter_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    List<Object> params = new ArrayList<>();
    params.add(chapter.getComicsId());
    params.add(chapter.getChapterIndex());
    params.add(chapter.getChapterName());
    params.add(chapter.getStatus());
    params.add(chapter.getPageNumber());
    params.add(chapter.getFileType());
    params.add(chapter.getFilePath());
    params.add(chapter.getChapterPath());
    params.add(chapter.getChapterType());
    jdbcClient.querySingleWithParams(insertSql, new JsonArray(params), res -> {
      if (res.succeeded()) {
        message.reply("");
      } else {
        message.fail(500, res.cause().getMessage());
      }
    });

  }

  private void updateComics(Message<String> message) {
    ComicsQuery comicsQuery = JSONUtil.toBean(message.body(), ComicsQuery.class);
    if (comicsQuery.getId() == null) {
      message.fail(500, "id为空");
      return;
    }
    JsonArray jsonArray = new JsonArray();
    List<String> list = new ArrayList<>();
    if (StrUtil.isNotEmpty(comicsQuery.getComicsName())) {
      list.add("comics_name = ?");
      jsonArray.add(comicsQuery.getComicsName());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getComicsAuthor())) {
      list.add("comics_author = ?");
      jsonArray.add(comicsQuery.getComicsAuthor());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getComicsTags())) {
      list.add("comics_tags = ?");
      jsonArray.add(comicsQuery.getComicsTags());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getCoverImage())) {
      list.add("cover_image = ?");
      jsonArray.add(comicsQuery.getCoverImage());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getDescription())) {
      list.add("description = ?");
      jsonArray.add(comicsQuery.getDescription());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getStatus())) {
      list.add("status = ?");
      jsonArray.add(comicsQuery.getStatus());
    }
    if (StrUtil.isNotEmpty(comicsQuery.getGradeType())) {
      list.add("grade_type = ?");
      jsonArray.add(comicsQuery.getGradeType());
    }
    String updateSql = "UPDATE comics SET " + list.stream().collect(Collectors.joining(",")) + " where id = ?";
    jsonArray.add(comicsQuery.getId());
    jdbcClient.querySingleWithParams(updateSql, jsonArray, res -> {
      if (res.succeeded()) {
        message.reply("");
      } else {
        message.fail(500, res.cause().getMessage());
      }
    });

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
    jdbcClient.querySingleWithParams(insertSql, new JsonArray(params), res -> {
      if (res.succeeded()) {
        message.reply("");
      } else {
        message.fail(500, res.cause().getMessage());
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

  private void queryComicsPage(Message<JsonObject> message) {
    ComicsQuery comicsQuery = JSONUtil.toBean(message.body().toString(), ComicsQuery.class);
    //先查询数据总数
    String selectPage = "select * from comics";
    List<Object> list = new ArrayList<>();
    String whereSql = getComicsWhereSql(comicsQuery, list);
    if (StrUtil.isNotEmpty(whereSql)) {
      selectPage += WHERE + whereSql;
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
      selectCount += WHERE + whereSql;
    }
    jdbcClient.querySingleWithParams(selectCount, new JsonArray(list), selectCountRes -> {
      if (selectCountRes.succeeded()) {
        message.reply(selectCountRes.result().getInteger(0));
      } else {
        message.fail(500, selectCountRes.cause().getMessage());
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
          whereSql += AND;
        }
        whereSql += "comics_tags like '%,' || ? || ',%' ";
        list.add(comicsQuery.getComicsTagList().get(0));
      } else {
        if (StrUtil.isEmpty(comicsQuery.getTagLogic())) {
          comicsQuery.setTagLogic(OR);
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
