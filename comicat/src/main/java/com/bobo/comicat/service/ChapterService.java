package com.bobo.comicat.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.constant.Constant;
import com.bobo.comicat.common.constant.JdbcConstant;
import com.bobo.comicat.common.entity.Chapter;
import com.bobo.comicat.common.entity.ChapterQuery;
import com.bobo.comicat.common.entity.ChapterView;
import com.bobo.comicat.common.entity.Comics;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bobo.comicat.common.constant.ApiConstant.*;
import static com.bobo.comicat.common.constant.JdbcConstant.QUERY_CHAPTER;

/**
 * 章节类
 *
 * @author BO
 * @date 2021-05-10 16:01
 * @since 2021/5/10
 **/
public class ChapterService extends BaseBean {

  FileService fileService;

  public ChapterService(Vertx vertx, JsonObject config) {
    super(vertx, config);
    fileService = FileService.getInstance(vertx, config);

  }

  public void upload(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    //获取上传文件信息
    if (routingContext.fileUploads().size() != 1) {
      responseError(response, RESULT_CODE_404, RESULT_MESSAGE_UPLOAD_FILE_NOT_FOUND);
      return;
    }
    FileUpload fileUpload = routingContext.fileUploads().iterator().next();
    JSONObject jsonObject = new JSONObject();
    routingContext.request().params().forEach(m -> jsonObject.set(m.getKey(), m.getValue()));
    ChapterQuery chapterQuery = jsonObject.toBean(ChapterQuery.class);
    chapterQuery.setFileUpload(fileUpload);
    Future<ChapterView> chapterViewFuture;
    switch (FileUtil.getSuffix(fileUpload.fileName()).toLowerCase()) {
      case Constant.ZIP:
        chapterViewFuture = fileService.readZip(chapterQuery);
        break;
      case Constant.PDF:
      case Constant.MOBI:
      case Constant.EPUB:
      default:
        responseError(response, RESULT_CODE_403, RESULT_MESSAGE_FILE_TYPE_IS_NOT_CURRENTLY_SUPPORTED);
        vertx.fileSystem().delete(fileUpload.uploadedFileName());
        return;
    }
    chapterViewFuture.onFailure(f -> responseError(routingContext.response(), f))
      .onSuccess(c -> responseSuccess(routingContext.response(), c));
  }

  public void addChapter(RoutingContext routingContext) {
    String bodyAsString = routingContext.getBodyAsString();
    if (!JSONUtil.isJson(bodyAsString)) {
      responseError(routingContext.response(), 422, RESULT_MESSAGE_BODY_MUST_BE_JSON);
      return;
    }
    ChapterQuery chapterQuery = JSONUtil.toBean(bodyAsString, ChapterQuery.class);
    fileService.zipToFilePackage(chapterQuery).onFailure(f -> responseError(routingContext.response(), f))
      .onSuccess(json -> {
        //插入数据库
        eventBus.request(JdbcConstant.INSERT_CHAPTER, JSONUtil.toJsonStr(chapterQuery));
        responseSuccess(routingContext.response(), RESULT_MESSAGE_SUCCESS);
      });

  }

  public void getList(RoutingContext routingContext) {
    String comicsId = routingContext.request().getParam("comicsId");
    if (!NumberUtil.isInteger(comicsId)) {
      responseError(routingContext.response(), RESULT_CODE_422, RESULT_MESSAGE_PARAM_FORMAT_ERROR);
      return;
    }
    ChapterQuery build = ChapterQuery.builder().comicsId(NumberUtil.parseInt(comicsId)).build();
    eventBus.request(QUERY_CHAPTER, JSONUtil.toJsonStr(build)).onSuccess(res -> {
      List<ChapterView> chapterViews = JSONUtil.parseArray(res.body().toString()).toList(ChapterView.class).stream()
        .sorted(Comparator.comparing(ChapterView::getChapterIndex)).collect(Collectors.toList());
      responseSuccess(routingContext.response(), chapterViews);

    }).onFailure(f -> responseError(routingContext.response(), f));

  }
}
