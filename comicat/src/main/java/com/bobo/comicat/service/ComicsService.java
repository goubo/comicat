package com.bobo.comicat.service;

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
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bobo.comicat.common.constant.JdbcConstant.QUERY_COMICS_COUNT;
import static com.bobo.comicat.common.constant.JdbcConstant.QUERY_COMICS_PAGE;

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
      comicsQuery.setPageSize(config.getInteger("page_size", 12));
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
        //查询数据
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
    routingContext.response().sendFile(config.getString("basePath") + "/cover/" + path).onFailure(
      f -> responseError(routingContext.response(), 404, "图片未找到")
    );
  }

  public void addComics(RoutingContext routingContext) {
    ComicsQuery comicsQuery = JSONUtil.toBean(routingContext.getBodyAsString(), ComicsQuery.class);
    comicsQuery.setComicsTags(StrUtil.COMMA + comicsQuery.getComicsTagList().stream().map(String::valueOf)
      .collect(Collectors.joining(StrUtil.COMMA)) + StrUtil.COMMA).setCreateTime(LocalDateTime.now())
      .setStatus("1").setGradeType("1");
    eventBus.request(JdbcConstant.INSERT_COMICS, JSONUtil.toJsonStr(comicsQuery)).onSuccess(su -> {
      Object body = su.body();
      System.out.println(body);
      responseSuccess(routingContext.response(), body);
    }).onFailure(f -> responseError(routingContext.response(), f));
  }
}
