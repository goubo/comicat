package com.bobo.comicat.handler;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.service.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import static com.bobo.comicat.common.constant.ApiConstant.*;
import static com.bobo.comicat.common.constant.Constant.CACHE_UPLOAD_PATH;

/**
 * 路由
 *
 * @author BO
 * @date 2021-03-19 15:42
 * @since 2021/3/19
 **/
public class ApiRouterHandler extends BaseBean {
  private final Router router;

  public ApiRouterHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
    router = getRouter();
    vertx.createHttpServer().requestHandler(router).listen(47373);

  }

  public void router() {
    ComicsService comicsService = new ComicsService(vertx, config);
    TagService tagService = new TagService(vertx, config);
    ConfigService configService = new ConfigService(vertx, config);
    ChapterService chapterService = new ChapterService(vertx, config);
    FileService fileService = FileService.getInstance(vertx, config);
    router.get(COMICS).handler(comicsService::getComics);
    router.post(COMICS).handler(comicsService::addComics);
    router.patch(COMICS).handler(comicsService::updateComics);

    router.get(COMICS_IMAGE).handler(comicsService::getComicsImage);
    router.get(COMICS_COVER_PATH).handler(comicsService::getComicsCover);
    router.get(TAGS_GET_LIST).handler(tagService::getTags);

    router.post(CHAPTER_UPLOAD).handler(chapterService::upload);
    router.post(CHAPTER).handler(chapterService::addChapter);
    router.get(CHAPTER_GET_LIST).handler(chapterService::getList);

    router.get(CONFIG).handler(configService::getConfig);
    router.post(CONFIG).handler(configService::setConfig);

    router.delete(FILE_TEMP).handler(fileService::deleteTemp);

    router.get("/testFile").handler(this::testFile);

  }

  private void testFile(RoutingContext routingContext) {
    FileService instance = FileService.getInstance(vertx, config);
    HttpServerRequest request = routingContext.request();
    String path = request.getParam("path");
    String seek = request.getParam("seek");
    String str = request.getParam("str");
    instance.insertFile(path, Integer.parseInt(seek), Buffer.buffer(str));
    routingContext.response().end("i'm here");
  }

  private Router getRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create().setUploadsDirectory(CACHE_UPLOAD_PATH));
    return router;
  }

}
