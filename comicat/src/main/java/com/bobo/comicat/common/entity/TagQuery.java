package com.bobo.comicat.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author BO
 * @date 2021-03-23 13:49
 * @since 2021/3/23
 **/
@Data
@Builder
@Accessors(chain = true)
public class TagQuery {
  private Integer id;
  private String name;
  private String gradeType;
  private String type;
}
