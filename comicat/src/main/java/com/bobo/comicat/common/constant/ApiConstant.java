package com.bobo.comicat.common.constant;

/**
 * @author BO
 * @date 2021-03-22 10:45
 * @since 2021/3/22
 **/
public class ApiConstant {
  public static final String API_BASE_URL = "/api/v1";

  public static final String CONFIG = API_BASE_URL + "/config";

  public static final String GET_COMICS = API_BASE_URL + "/comics";
  public static final String GET_COMICS_IMAGE = API_BASE_URL + "/comics/image";
  public static final String GET_COVER_IMAGE = API_BASE_URL + "/comics/cover/:path";
  public static final String GET_TAGS = API_BASE_URL + "/tags";


}
