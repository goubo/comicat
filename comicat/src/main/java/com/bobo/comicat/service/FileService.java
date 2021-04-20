package com.bobo.comicat.service;

import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.base.MyCompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;

import static com.bobo.comicat.common.constant.Constant.TEMP_LEN_18;

/**
 * 文件操作类
 *
 * @author BO
 * @date 2021-04-16 10:02
 * @since 2021/4/16
 **/
public class FileService extends BaseBean {
  private static volatile FileService fileService;

  public static FileService getInstance(Vertx vertx, JsonObject config) {
    if (fileService == null) {
      synchronized (FileService.class) {
        if (fileService == null) {
          fileService = new FileService(vertx, config);
        }
      }
    }
    return fileService;
  }

  private FileService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

  /**
   * 从文件指定位置插入数据,后面的数据一次后移
   * 实现原理,复制出一个临时文件,在覆盖源文件
   *
   * @param filePath 文件路径
   * @param seek     插入位置
   */
  public Future<Void> insertFile(String filePath, int seek, Buffer buffer) {
    Promise<Void> promise = Promise.promise();
    //判断文件是否存在
    vertx.fileSystem().props(filePath).onFailure(promise::fail)
      .onSuccess(props -> vertx.fileSystem().open(filePath, new OpenOptions().setRead(true)).onFailure(promise::fail)
        .onSuccess(open -> vertx.fileSystem().open(filePath + ".insert.temp", new OpenOptions().setWrite(true).setCreate(true)).onFailure(promise::fail)
          .onSuccess(openTemp -> {
            ArrayList<Future<Void>> futureList = new ArrayList<>();
            futureList.add(copy(new CopyOption().setAsyncFile(open).setBegin(0).setEnd(seek), new CopyOption().setAsyncFile(openTemp).setBegin(0).setEnd(seek)));
            futureList.add(openTemp.write(buffer, seek));
            futureList.add(copy(new CopyOption().setAsyncFile(open).setBegin(seek).setEnd(props.size()), new CopyOption().setAsyncFile(openTemp).setBegin(seek + buffer.length()).setEnd(props.size() + buffer.length())));
            MyCompositeFuture.all(futureList).onFailure(promise::fail).onSuccess(su -> promise.complete());
          })));

    return promise.future();
  }

  @Data
  @Accessors(chain = true)
  static class CopyOption {
    AsyncFile asyncFile;
    long begin;
    long end;
  }


  private Future<Void> copy(CopyOption src, CopyOption des) {
    Promise<Void> promise = Promise.promise();

    if (des.getEnd() < des.getBegin() || src.getEnd() < src.getBegin()) {
      return Future.failedFuture("结束位置在开始之前");
    }
    if (des.getEnd() - des.getBegin() == 0) {
      return Future.succeededFuture();
    }
    if ((des.getEnd() - des.getBegin()) < (src.getEnd() - src.getBegin())) {
      return Future.failedFuture("desc 空间不足");
    }
    //循环读取src的数据
    long dataLen = src.getEnd() - src.getBegin();
    long readLen = 0;
    ArrayList<Future<Void>> futureList = new ArrayList<>();
    do {
      int i = Math.toIntExact(Math.min(dataLen - readLen, TEMP_LEN_18));
      if (i < 1) {
        break;
      }
      Buffer buffer = Buffer.buffer(i);
      long finalReadLen = readLen;
      src.getAsyncFile().read(buffer, 0, src.getBegin() + readLen, i).onFailure(Throwable::printStackTrace)
        .onSuccess(rb -> futureList.add(des.getAsyncFile().write(rb, des.getBegin() + finalReadLen)));
      readLen += i;
      if (i < TEMP_LEN_18) {
        break;
      }
    } while (true);
    MyCompositeFuture.all(futureList).onFailure(promise::fail).onSuccess(su -> promise.complete());
    return promise.future();
  }

}
