package com.bobo.comicat.handler;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.service.ComicsService;
import com.bobo.comicat.service.TagService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

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

    router.get(GET_COMICS).handler(comicsService::getComics);
    router.get(GET_COMICS_IMAGE).handler(comicsService::getComicsImage);
    router.get(GET_TAGS).handler(tagService::getTags);

  }

  private Router getRouter() {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create().setUploadsDirectory(CACHE_UPLOAD_PATH));
    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setSessionCookieName("comicat_session"));
    return router;
  }

}
