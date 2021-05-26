package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author BO
 * @date 2021-03-22 15:47
 * @since 2021/3/22
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ComicsView extends Comics {
  private ComicsQuery comicsQuery;
  private List<Comics> comicsList;
  private Comics comics;
}
