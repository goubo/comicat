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

  public static final String TAGS_GET_LIST = API_BASE_URL + "/tags";

  public static final String CHAPTER = API_BASE_URL + "/chapter";
  public static final String CHAPTER_ID = CHAPTER + "/:id";
  public static final String CHAPTER_UPLOAD = CHAPTER + "/upload";
  public static final String CHAPTER_GET_LIST = CHAPTER + "/:comicsId";

  public static final String FILE = "/file";
  public static final String FILE_TEMP = FILE + "/temp/:path";


  public static final int RESULT_CODE_200 = 200;
  public static final int RESULT_CODE_500 = 500;
  public static final int RESULT_CODE_404 = 404;
  public static final int RESULT_CODE_403 = 403;
  public static final int RESULT_CODE_422 = 422;

  public static final String RESULT_MESSAGE_SUCCESS = "success";
  public static final String RESULT_MESSAGE_BODY_MUST_BE_JSON = "请求体必须是json";
  public static final String RESULT_MESSAGE_FILE_TYPE_IS_NOT_CURRENTLY_SUPPORTED = "文件类型暂不支持";
  public static final String RESULT_MESSAGE_UPLOAD_FILE_NOT_FOUND = "上传文件未找到";
  public static final String RESULT_MESSAGE_IMAGE_NOT_FOUND = "图片未找到未找到";
  public static final String RESULT_MESSAGE_PARAM_FORMAT_ERROR = "参数错误";

}
