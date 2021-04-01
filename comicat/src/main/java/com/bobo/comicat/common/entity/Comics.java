package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author BO
 * @date 2021-03-22 15:47
 * @since 2021/3/22
 **/
@Data
@Accessors(chain = true)
public class Comics {
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
