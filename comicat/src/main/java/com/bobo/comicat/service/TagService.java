package com.bobo.comicat.service;

import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.entity.Comics;
import com.bobo.comicat.common.entity.Tag;
import com.bobo.comicat.common.entity.TagQuery;
import com.bobo.comicat.common.entity.TagView;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.bobo.comicat.common.constant.JdbcConstant.QUERY_TAGS;

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
    HttpServerRequest request = routingContext.request();
    TagQuery tagQuery = new TagQuery();
    tagQuery.setName(request.getParam("name"));
    tagQuery.setGradeType(request.getParam("gradeType"));
    eventBus.request(QUERY_TAGS, JsonObject.mapFrom(tagQuery)).onSuccess(su->{
      List<Tag> list = ((JsonArray) su.body()).stream().map(o -> JSONUtil.toBean(o.toString(), Tag.class)).collect(Collectors.toList());
      responseSuccess(routingContext.response(),new TagView().setTagList(list));
    }).onFailure(f -> responseError(routingContext.response(), 500, f.getMessage()));
  }
}
