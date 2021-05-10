package com.bobo.comicat.common.entity;

import lombok.Data;

/**
 * 文件结构定义
 *
 * @author BO
 * @date 2021-04-20 15:01
 * @since 2021/4/20
 **/
@Data
public class FilePackage {
  /**
   * 名称
   */
  private String comicsName;
  /**
   * 作者
   */
  private String comicsAuthor;

  /**
   * 封面图片长度
   */
  private int coverLength;
  /**
   * 章节名称
   */
  private String chapterName;
  /**
   * 章节页码
   */
  private int chapterPageSize;
  /**
   * 页下标索引 ,每张图片的开头,需要加上包头长度和封面长度
   */
  private long[] pageIndex;


}
