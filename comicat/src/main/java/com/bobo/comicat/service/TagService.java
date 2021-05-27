package com.bobo.comicat.service;

import cn.hutool.core.util.StrUtil;
import com.bobo.comicat.common.base.BaseBean;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.bobo.comicat.common.util.CacheUtil.CACHE_TAGS;
import static com.bobo.comicat.common.constant.Constant.REFRESH;
import static com.bobo.comicat.common.constant.JdbcConstant.QUERY_COMICS_TAGS;

/**
 * tag
 *
 * @author BO
 * @date 2021-03-21 09:11
 * @since 2021/3/21
 **/
public class TagService extends BaseBean {
  public TagService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }


  public void getTags(RoutingContext routingContext) {
    if (routingContext.request().params().contains(REFRESH)) {
      CACHE_TAGS.clear();
    }

    if (CACHE_TAGS.size() < 1) {
      eventBus.request(QUERY_COMICS_TAGS, "").onSuccess(su -> {
        JsonArray list = (JsonArray) su.body();
        list.stream().map(l -> (JsonArray) l).map(l -> l.getString(0)).forEach(s -> {
          for (String s1 : s.split(StrUtil.COMMA)) {
            if (StrUtil.isNotEmpty(s1)) {
              CACHE_TAGS.add(s1);
            }
          }
        });
        responseSuccess(routingContext.response(), CACHE_TAGS);
      }).onFailure(f -> responseError(routingContext.response(), 500, f.getMessage()));
    } else {
      responseSuccess(routingContext.response(), CACHE_TAGS);
    }
  }
}
