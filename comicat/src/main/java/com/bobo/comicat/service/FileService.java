package com.bobo.comicat.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.base.MyCompositeFuture;
import com.bobo.comicat.common.entity.ChapterQuery;
import com.bobo.comicat.common.entity.ChapterView;
import com.bobo.comicat.common.entity.FilePackage;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static cn.hutool.core.text.CharPool.SLASH;
import static com.bobo.comicat.common.constant.Constant.*;

/**
 * 文件操作类
 *
 * @author BO
 * @date 2021-04-16 10:02
 * @since 2021/4/16
 **/
public class FileService extends BaseBean {
  private static volatile FileService fileService;

  private FileService(Vertx vertx, JsonObject config) {
    super(vertx, config);
  }

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

  /**
   * 读取zip文件
   *
   * @param chapterQuery 查询对象
   * @return 返回操作结果
   */
  public Future<ChapterView> readZip(ChapterQuery chapterQuery) {
    String uploadedFileName = chapterQuery.getFileUpload().uploadedFileName();

    return vertx.executeBlocking(promise -> {
      ZipFile zipFile = ZipUtil.toZipFile(FileUtil.file(uploadedFileName), null);
      ChapterView chapterView = ChapterView.builder().build();
      chapterView.setUploadPath(FileUtil.getPrefix(uploadedFileName));
      chapterView.setComicsId(chapterQuery.getComicsId());
      chapterView.setPageNumber(zipFile.stream().filter(e -> !e.isDirectory()).count());
      promise.complete(chapterView);
    });
  }

  public Future<JsonObject> zipToFilePackage(ChapterQuery chapterQuery) {
    Promise<JsonObject> promise = Promise.promise();
    //确认zip路径
    String zipPath = CACHE_UPLOAD_PATH + SLASH + chapterQuery.getUploadPath();
    ZipFile zipFile = ZipUtil.toZipFile(FileUtil.file(zipPath), null);
    List<ZipEntry> name = CollUtil.sortByProperty(zipFile.stream().filter(e -> !e.isDirectory()).collect(Collectors.toList()), "name");
    String comicsPath = basePath + SLASH + chapterQuery.getComics().getComicsName();
    String chapterPath = comicsPath + SLASH + chapterQuery.getChapterName() + ".cct";
    chapterQuery.setChapterPath(chapterQuery.getComics().getComicsName() + SLASH + chapterQuery.getChapterName() + ".cct");
    String coverImage = basePath + COVER_PATH + chapterQuery.getComics().getCoverImage();
    FilePackage filePackage = FilePackage.builder()
      .comicsName(chapterQuery.getComics().getComicsName())
      .chapterName(chapterQuery.getChapterName())
      .chapterPageNum(chapterQuery.getPageNumber())
      .comicsAuthor((chapterQuery.getComics().getComicsAuthor()))
      .pageIndex(name.stream().mapToLong(ZipEntry::getSize).toArray())
      .build();

    vertx.fileSystem().readFile(coverImage).onFailure(promise::fail)
      .onSuccess(cover -> vertx.fileSystem().mkdirsBlocking(FileUtil.getParent(chapterPath, 1))
        .open(chapterPath, new OpenOptions().setWrite(true).setCreate(true)).onFailure(promise::fail)
        .onSuccess(f -> {
          Buffer info = Buffer.buffer(JSONUtil.toJsonStr(filePackage.setCoverSize(cover.length())));
          Buffer buffer = Buffer.buffer();
          buffer.appendInt(info.length());
          buffer.appendBuffer(info);
          buffer.appendBuffer(cover);
          f.write(buffer);
          AtomicInteger infoSize = new AtomicInteger(buffer.length());
          vertx.executeBlocking(blockPromise -> {
            try {
              for (ZipEntry ze : name) {
                InputStream inputStream = zipFile.getInputStream(ze);
                byte[] bytes = new byte[1 << 12];
                Buffer zeBuff = Buffer.buffer();
                int readLen;
                while ((readLen = inputStream.read(bytes)) > 0) {
                  zeBuff.appendBytes(bytes, 0, readLen);
                }
                f.write(zeBuff);
                infoSize.addAndGet(zeBuff.length());
                inputStream.close();
              }
              promise.complete(new JsonObject().put("chapterPath", chapterPath).put("package", filePackage));
              vertx.fileSystem().delete(zipPath);
            } catch (Exception e) {
              e.printStackTrace();
              blockPromise.fail(e);
            }
          }, promise);
        }));
    return promise.future();
  }

  @Data
  @Accessors(chain = true)
  static class CopyOption {
    AsyncFile asyncFile;
    long begin;
    long end;
  }
}
