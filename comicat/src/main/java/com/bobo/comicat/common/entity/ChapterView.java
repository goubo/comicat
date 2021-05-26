package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author BO
 * @date 2021-05-10 16:02
 * @since 2021/5/10
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ChapterView extends Chapter {
  private String uploadPath;
}
