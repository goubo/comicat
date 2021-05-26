package com.bobo.comicat.common.constant;

/**
 * @author BO
 * @date 2021-03-22 10:45
 * @since 2021/3/22
 **/
public class ApiConstant {
  public static final String API_BASE_URL = "/api/v1";

  public static final String CONFIG = API_BASE_URL + "/config";

  public static final String COMICS = API_BASE_URL + "/comics";
  public static final String COMICS_IMAGE = COMICS + "/image";
  public static final String COMICS_COVER_PATH = COMICS + "/cover/:path";

  public static final String GET_TAGS = API_BASE_URL + "/tags";

  public static final String CHAPTER = API_BASE_URL + "/chapter";
  public static final String CHAPTER_UPLOAD = CHAPTER + "/upload";

}
