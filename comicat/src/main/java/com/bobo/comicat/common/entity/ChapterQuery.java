package com.bobo.comicat.common.entity;

import io.vertx.ext.web.FileUpload;
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
public class ChapterQuery extends Chapter {
  private FileUpload fileUpload;
  private String uploadPath;
  private Comics comics;
}
