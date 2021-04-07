package com.bobo.comicat.common.constant;

/**
 * 常量池
 *
 * @author BO
 * @date 2021-03-19 10:31
 * @since 2021/3/19
 **/
public class Constant {
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
