package com.bobo.comicat.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询漫画类
 *
 * @author BO
 * @date 2021-03-21 09:59
 * @since 2021/3/21
 **/

@Data
@Builder
@Accessors(chain = true)
public class ComicsQuery {
  /**
   * 其他查询条件
   */
  private String queryString;
  /**
   * tag 关系
   */
  private String tagLogic;

  /**
   * 标签
   */
  private List<String> comicsTagList;
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

  /**
   * id
   */
  private Integer id;
  /**
   * 名称
   */
  private String comicsName;
  /**
   * 作者
   */
  private String comicsAuthor;
  /**
   * 标签
   */
  private String comicsTags;
  /**
   * 漫画状态
   */
  private String status;
  /**
   * 录入时间
   */
  private LocalDateTime createTime;
  /**
   * 封面路径
   */
  private String coverImage;
  /**
   * 描述
   */
  private String description;
  /**
   * 分级
   */
  private String gradeType;
}

