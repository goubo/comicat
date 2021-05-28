package com.bobo.comicat.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 章节
 *
 * @author BO
 * @date 2021-05-10 16:02
 * @since 2021/5/10
 **/
@Data
@Builder
@Accessors(chain = true)
public class Chapter {
  /**
   * 章节id
   */
  private Integer id;
  /**
   * 漫画id
   */
  private Integer comicsId;
  /**
   * 章节排序索引
   */
  private Integer chapterIndex;
  /**
   * 章节名称
   */
  private String chapterName;
  /**
   * 章节类型
   */
  private String chapterType;
  /**
   * 状态
   */
  private String status;
  /***
   * 章节页数
   */
  private Integer pageNumber;
  /**
   * 文件类型
   */
  private String fileType;
  /**
   * 文件地址
   */
  private String filePath;
  /**
   *
   */
  private String chapterPath;
}
