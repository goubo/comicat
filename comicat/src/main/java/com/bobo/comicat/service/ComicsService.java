package com.bobo.comicat.service;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.entity.Comics;
import com.bobo.comicat.common.entity.ComicsQuery;
import com.bobo.comicat.common.entity.ComicsView;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
    comicsQuery.setTagLogic(params.get("tagLogic"));
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
    response.putHeader("content-type", "image/png");
//    vertx.fileSystem().readFile("/Users/bobo/Downloads/Bilibili (Embed)/尚硅谷2021版TypeScript教程（李立超老师TS新课）/尚硅谷2021版TypeScript教程（李立超老师TS新课） - 009 - 08_TS编译选项（4）.jpg")
//      .onFailure(f -> responseError(response, f))
//    .onSuccess(response::end);
//      new ZipInputStream()


    try {
      ZipFile zf = new ZipFile(new File("/Users/bobo/my/work/image.zip"));
      InputStream in = new BufferedInputStream(new FileInputStream("/Users/bobo/my/work/image.zip"));
      ZipInputStream zin = new ZipInputStream(in);

      ZipEntry ze;
      while ((ze = zin.getNextEntry()) != null) {
        System.out.println(ze.getName());
        System.out.println(new File(ze.getName()).getName());
        System.out.println(ze.getSize());
        System.out.println(ze.getTime());
        System.out.println(ze.getTimeLocal());
        System.out.println(ze.getComment());
        System.out.println(ze.getCompressedSize());
        System.out.println(ze.getCrc());
        System.out.println(ze.isDirectory());
        if (!ze.isDirectory()) {
          response.send(Buffer.buffer(zf.getInputStream(ze).readAllBytes()));
        }
        System.out.println("------*-------");
      }
      response.end();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

}
