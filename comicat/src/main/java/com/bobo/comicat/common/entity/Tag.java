package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * tag
 *
 * @author BO
 * @date 2021-03-23 13:49
 * @since 2021/3/23
 **/
@Data
@Accessors(chain = true)
public class Tag {
  private Integer id;
  private String name;
  private String gradeType;
  private String type;
}
