package com.bobo.comicat.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bobo.comicat.common.base.BaseBean;
import com.bobo.comicat.common.constant.Constant;
import com.bobo.comicat.common.entity.ChapterQuery;
import com.bobo.comicat.common.entity.ChapterView;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.List;

/**
 * 章节类
 *
 * @author BO
 * @date 2021-05-10 16:01
 * @since 2021/5/10
 **/
public class ChapterService extends BaseBean {

  FileService fileService;

  public ChapterService(Vertx vertx, JsonObject config) {
    super(vertx, config);
    fileService = FileService.getInstance(vertx, config);

  }

  public void upload(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    //获取上传文件信息
    if (routingContext.fileUploads().size() != 1) {
      responseError(response, 403, "上传文件未找到");
      return;
    }
    FileUpload fileUpload = routingContext.fileUploads().iterator().next();
    JSONObject jsonObject = new JSONObject();
    routingContext.request().params().forEach(m -> jsonObject.set(m.getKey(), m.getValue()));
    ChapterQuery chapterQuery = jsonObject.toBean(ChapterQuery.class);
    chapterQuery.setFileUpload(fileUpload);
    Future<ChapterView> chapterViewFuture;
    switch (FileUtil.getSuffix(fileUpload.fileName()).toLowerCase()) {
      case Constant.ZIP:
        chapterViewFuture = fileService.readZip(chapterQuery);
        break;
      case Constant.PDF:
      case Constant.MOBI:
      case Constant.EPUB:
      default:
        responseError(response, 403, "文件类型暂不支持");
        vertx.fileSystem().delete(fileUpload.uploadedFileName());
        return;
    }
    chapterViewFuture.onFailure(f -> responseError(routingContext.response(), f))
      .onSuccess(c -> responseSuccess(routingContext.response(), c));
  }


  public void addChapter(RoutingContext routingContext) {
    String bodyAsString = routingContext.getBodyAsString();
    if (!JSONUtil.isJson(bodyAsString)) {
      responseError(routingContext.response(), 422, "请求体必须是json");
      return;
    }
    ChapterQuery chapterQuery = JSONUtil.toBean(bodyAsString, ChapterQuery.class);
    fileService.zipToFilePackage(chapterQuery).onFailure(f -> responseError(routingContext.response(), f))
      .onSuccess(json -> {
        System.out.println(json);
        responseSuccess(routingContext.response(), "success");
      });

  }

  public static void main(String[] args) {
    List<Integer> integers = Arrays.asList(124517, 78722, 148542, 124869, 143441, 136410, 120718, 127418, 171820, 136770, 116551, 136417, 125595, 110439, 128080, 127085, 130390, 162614, 154333, 113516, 131277, 142600, 133608, 106153, 109782, 142004, 134617, 159034, 126926, 111488, 123926, 172905, 126150, 101490, 138692, 126080, 126188, 153707, 121975, 129802, 133078, 124646, 128394, 149174, 142541, 182754, 146406, 126432, 170491, 141057, 126600, 145106, 122533, 116961, 130150, 103496, 106940, 155081, 128473, 146303, 174203, 116680, 125750, 143807, 157553, 124641, 120108, 134944, 103821, 123021, 175203, 132174, 128693, 151377, 134481, 141446, 109277, 124990, 111337, 124299, 120582, 143615, 121011, 148916, 110470, 168450, 144652, 118834, 109395, 119294, 146519, 118151, 129169, 127994, 123018, 130177, 186746, 117283, 150377, 109478, 127160, 166142, 108390, 120040);
    int sum = integers.stream().mapToInt(Integer::intValue).sum();
    System.out.println(sum);
  }
}
