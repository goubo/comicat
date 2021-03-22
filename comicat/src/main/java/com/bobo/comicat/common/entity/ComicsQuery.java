package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 查询漫画类
 *
 * @author BO
 * @date 2021-03-21 09:59
 * @since 2021/3/21
 **/

@Data
@Accessors(chain = true)
public class ComicsQuery {
  /**
   * 漫画名称
   */
  private String comicsName;
  /**
   * 其他查询条件
   */
  private String queryString;
  /**
   * 标签
   */
  private List<String> comicsTags;
  /**
   * 排序字段
   */
  private String orderByField;
  /**
   * 排序顺序
   */
  private String orderByValue;
  /**
   * 每页条数
   */
  private int pageSize;
  /**
   * 页数
   */
  private int pageNumber;
  /**
   * 总条数
   */
  private int total;
}

