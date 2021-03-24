package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author BO
 * @date 2021-03-23 13:52
 * @since 2021/3/23
 **/
@Data
@Accessors(chain = true)
public class TagView {

  private List<Tag> tagList;
}
