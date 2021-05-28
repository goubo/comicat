package com.bobo.comicat.common.constant;

/**
 * 常量池
 *
 * @author BO
 * @date 2021-03-19 10:31
 * @since 2021/3/19
 **/
public class Constant {
  /**
   * 读取缓冲长度 1 << 18  256KB
   */
  public static final int TEMP_LEN_18 = 2 << 18;
  /**
   * 读取缓冲长度 1<< 20   1MB
   */
  public static final int TEMP_LEN_20 = 1 << 20;

  public static final String PATH = "path";
  public static final String FILE = "file";
  public static final String ZIP = "zip";
  public static final String PDF = "pdf";
  public static final String MOBI = "mobi";
  public static final String EPUB = "epub";


  public static final String DEF_CONF_DOT_JSON = "def-conf.json";

  /**
   * 刷新tag的参数
   */
  public static final String REFRESH = "refresh";

  public static final String COVER_PATH = "/cover/";
  public static final String HOME_PATH = System.getProperty("user.home");
  public static final String CACHE_PATH = HOME_PATH + "/.cache/comicat";
  public static final String CACHE_UPLOAD_PATH = CACHE_PATH + "/upload";
  public static final String CACHE_CONFIG_PATH = CACHE_PATH + "/config.json";
  public static final String DATABASE_TYPE_KEY = "dataBaseType";
  public static final String DATABASE_TYPE_SQLITE = "sqlite";
  public static final String SQLITE_PATH = CACHE_PATH + "/sqlLite.db";
  public static final String DATABASE_TYPE_MYSQL = "mysql";


  public static final String EVENT_BUS_WRITER_CONFIG_ADDRESS = "eventbus.config.writerConfig";
  public static final String EVENT_BUS_LOAD_CONFIG = "eventbus.config.loadConfig";


}
