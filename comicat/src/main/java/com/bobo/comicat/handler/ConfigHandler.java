package com.bobo.comicat.handler;

import com.bobo.comicat.common.base.BaseBean;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.io.File;

import static com.bobo.comicat.common.constant.Constant.*;

/**
 * 配置文件处理类
 *
 * @author BO
 * @date 2021-03-19 10:22
 * @since 2021/3/19
 **/
public class ConfigHandler extends BaseBean {
  public ConfigHandler(Vertx vertx, JsonObject config) {
    super(vertx, config);
    vertx.eventBus().consumer(EVENT_BUS_WRITER_CONFIG_ADDRESS, this::writeConfig);
    vertx.eventBus().consumer(EVENT_BUS_LOAD_CONFIG, this::readConfig);
  }


  private void writeConfig(Message<JsonObject> message) {
    vertx.fileSystem().mkdirs(new File(CACHE_CONFIG_PATH).getParent())
      .onSuccess(s -> vertx.fileSystem().writeFile(CACHE_CONFIG_PATH, message.body().toBuffer())
        .onSuccess(su -> message.reply(message.body()))
        .onFailure(f -> message.fail(500, f.getMessage())))
      .onFailure(f -> message.fail(500, f.getMessage()));
  }


  private void readConfig(Message<JsonObject> message) {
    vertx.fileSystem().exists(CACHE_CONFIG_PATH).onSuccess(s -> {
      if (s.booleanValue() && false) {//TODO 覆盖用户设置
        configRetriever(message, CACHE_CONFIG_PATH, false);
      } else {
        configRetriever(message, "def-conf.json", true);
      }
    }).onFailure(f -> f.printStackTrace());
  }


  private void configRetriever(Message<JsonObject> message, String cacheConfigPath, boolean writeConfig) {
    ConfigRetrieverOptions options = new ConfigRetrieverOptions();
    options.addStore(new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", cacheConfigPath)));
    ConfigRetriever.create(vertx, options).getConfig(conf -> {
      if (conf.succeeded()) {
        config.clear();
        conf.result().getMap().forEach(config::put);
        message.reply(conf.result());
        if (writeConfig) {
          vertx.eventBus().request(EVENT_BUS_WRITER_CONFIG_ADDRESS, conf.result()).onFailure(Throwable::printStackTrace);
        }
      } else {
        message.fail(500, conf.cause().getMessage());
      }
    });
  }

}
