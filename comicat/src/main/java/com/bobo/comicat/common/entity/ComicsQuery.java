package com.bobo.comicat.common.entity;

/**
 * 查询漫画类
 *
 * @author BO
 * @date 2021-03-21 09:59
 * @since 2021/3/21
 **/

public record ComicsQuery(
  String comicsName,
  String queryString,
  String tags,

  String orderByField,
  String orderByValue,

  int pageSize,
  int pageNumber,
  int total) {
}

