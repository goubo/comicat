package com.bobo.comicat.common.entity;

import io.vertx.core.json.Json;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回类型
 *
 * @author BO
 * @date 2021-05-27 17:34
 * @since 2021/5/27
 **/
@Data
@Builder
@Accessors(chain = true)
public class Result {
  private int code;
  private String msg;
  private Object data;

  public String toJsonString() {
    return Json.encode(this);
  }
}
