package com.bobo.comicat.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.constant.JdbcConstant;
import com.bobo.comicat.common.entity.Comics;
import com.bobo.comicat.common.entity.ComicsQuery;
import com.bobo.comicat.common.entity.ComicsView;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.StrUtil.DASHED;
import static cn.hutool.core.util.StrUtil.DOT;
import static com.bobo.comicat.common.Cache.CACHE_TAGS;
import static com.bobo.comicat.common.constant.Constant.COVER_PATH;
import static com.bobo.comicat.common.constant.JdbcConstant.*;

/**
 * 漫画类
 *
 * @author BO
 * @date 2021-03-21 09:10
 * @since 2021/3/21
 **/
public class ComicsService extends BaseBean {
  public ComicsService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  public void getComics(RoutingContext routingContext) {
    ComicsQuery comicsQuery = new ComicsQuery();
    MultiMap params = routingContext.request().params();
    comicsQuery.setComicsName(params.get("comicsName"));
    comicsQuery.setComicsTagList(params.getAll("comicsTags"));
    comicsQuery.setTagLogic(StrUtil.isEmpty(params.get("tagLogic")) ? "or" : params.get("tagLogic"));
    comicsQuery.setPageNumber(NumberUtil.parseInt(params.get("pageNumber")));
    if (comicsQuery.getPageSize() == 0) {
      comicsQuery.setPageSize(config.getInteger("pageSize", 18));
    }
    if (comicsQuery.getPageNumber() < 1) {
      comicsQuery.setPageNumber(1);
    }
    JsonObject comicsQueryJson = JsonObject.mapFrom(comicsQuery);
    eventBus.request(QUERY_COMICS_COUNT, comicsQueryJson).onSuccess(s -> {
      Integer count = (Integer) s.body();
      if (count < 1) {
        ComicsView comicsView = new ComicsView().setComicsQuery(comicsQuery).setComicsList(new ArrayList<>());
        responseSuccess(routingContext.response(), comicsView);
      } else {
        comicsQuery.setTotal(count);
        eventBus.request(QUERY_COMICS_PAGE, comicsQueryJson).onSuccess(su -> {
          List<Comics> list = ((JsonArray) su.body()).stream().map(o -> JSONUtil.toBean(o.toString(), Comics.class)).collect(Collectors.toList());
          ComicsView comicsView = new ComicsView().setComicsQuery(comicsQuery).setComicsList(list);
          responseSuccess(routingContext.response(), comicsView);
        }).onFailure(f -> responseError(routingContext.response(), f));
      }
    }).onFailure(f -> responseError(routingContext.response(), f));
  }

  public void getComicsImage(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    response.end();
  }

  public void getComicsCover(RoutingContext routingContext) {
    String path = routingContext.request().getParam("path");
    routingContext.response().sendFile(config.getString("basePath") + COVER_PATH + path)
      .onFailure(f -> responseError(routingContext.response(), 404, "图片未找到"));
  }

  public void updateComics(RoutingContext routingContext) {
    JsonObject jsonObject = new JsonObject();
    routingContext.request().params().forEach(map -> jsonObject.put(map.getKey(), map.getValue()));
    ComicsQuery comicsQuery = JSONUtil.toBean(jsonObject.toString(), ComicsQuery.class);
    comicsQuery.setComicsTags(StrUtil.COMMA + comicsQuery.getComicsTagList().stream().map(String::valueOf)
      .collect(Collectors.joining(StrUtil.COMMA)) + StrUtil.COMMA);
    FileUpload[] fileUploads = routingContext.fileUploads().toArray(new FileUpload[1]);
    Comics comics = JSONUtil.toBean(routingContext.request().getParam("old"), Comics.class);
    if (fileUploads[0] == null) {
      if (!comicsQuery.getComicsName().equals(comics.getComicsName()) && StrUtil.isNotEmpty(comics.getCoverImage())) {
        comicsQuery.setCoverImage(comicsQuery.getComicsName() + DOT + FileUtil.getSuffix(comics.getCoverImage()));
      }
      eventBus.request(UPDATE_COMICS, JSONUtil.toJsonStr(comicsQuery)).onSuccess(su -> {
        if (StrUtil.isNotEmpty(comics.getCoverImage()) && StrUtil.isNotEmpty(comicsQuery.getCoverImage())
          && !comicsQuery.getCoverImage().equals(comics.getCoverImage())) {
          vertx.fileSystem().move(config.getString("basePath") + COVER_PATH + comics.getCoverImage(),
            config.getString("basePath") + COVER_PATH + comicsQuery.getCoverImage());
          responseSuccess(routingContext.response(), comicsQuery);
        }
      }).onFailure(f -> responseError(routingContext.response(), f));
    } else {

    }


  }

  public void addComics(RoutingContext routingContext) {
    JsonObject jsonObject = new JsonObject();
    routingContext.request().params().forEach(map -> jsonObject.put(map.getKey(), map.getValue()));
    ComicsQuery comicsQuery = JSONUtil.toBean(jsonObject.toString(), ComicsQuery.class);
    comicsQuery.setComicsTags(StrUtil.COMMA + comicsQuery.getComicsTagList().stream().map(String::valueOf)
      .collect(Collectors.joining(StrUtil.COMMA)) + StrUtil.COMMA).setCreateTime(LocalDateTime.now())
      .setStatus("1").setGradeType("1");
    FileUpload[] fileUploads = routingContext.fileUploads().toArray(new FileUpload[1]);
    if (fileUploads.length > 0) {
      //移动文件到封面文件夹下
      FileUpload fileUpload = fileUploads[0];
      comicsQuery.setCoverImage(comicsQuery.getComicsName() + DASHED + comicsQuery.getComicsAuthor() + DOT + FileUtil.getSuffix(fileUpload.fileName()));
      vertx.fileSystem().move(fileUpload.uploadedFileName(),
        config.getString("basePath") + COVER_PATH + comicsQuery.getCoverImage(),
        new CopyOptions().setReplaceExisting(true))
        .onSuccess(rs -> eventBus.request(JdbcConstant.INSERT_COMICS, JSONUtil.toJsonStr(comicsQuery))
          .onSuccess(su -> {
            Object body = su.body();
            responseSuccess(routingContext.response(), body);
            CACHE_TAGS.addAll(comicsQuery.getComicsTagList());
          }).onFailure(f -> {
            vertx.fileSystem().delete(fileUpload.uploadedFileName());
            responseError(routingContext.response(), f);
          })).onFailure(rf -> {
        vertx.fileSystem().delete(fileUpload.uploadedFileName());
        responseError(routingContext.response(), rf);
      });
    } else {
      eventBus.request(JdbcConstant.INSERT_COMICS, JSONUtil.toJsonStr(comicsQuery))
        .onSuccess(su -> {
          Object body = su.body();
          responseSuccess(routingContext.response(), body);
          CACHE_TAGS.addAll(comicsQuery.getComicsTagList());
        }).onFailure(f -> responseError(routingContext.response(), f));
    }

  }
}
