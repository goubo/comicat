package com.bobo.comicat.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author BO
 * @date 2021-03-23 13:49
 * @since 2021/3/23
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=true)
public class TagQuery extends Tag {
}
