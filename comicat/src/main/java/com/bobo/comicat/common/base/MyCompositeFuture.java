package com.bobo.comicat.common.base;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.impl.future.CompositeFutureImpl;

import java.util.List;

/**
 * 解决 CompositeFuture 无法使用泛型的Future ，会产生 Raw use of parameterized class 'Future' 警告⚠️️
 *
 * @author bobo
 */
public interface MyCompositeFuture extends CompositeFuture {
  /**
   * 如果a，b，c全被设为succeed，那么rt被设为succed。如果出现一个参数被设为failed，
   * 那么rt立即被设为failed（立即的意思就是不管其他参数有没被设置，只有有一个被设为failed，就立即给rt设置failed状态
   *
   * @param futures future list
   * @param <T>     泛型
   * @return 无
   */
  static <T> CompositeFuture all(List<Future<T>> futures) {
    return CompositeFutureImpl.all(futures.toArray(new Future[0]));
  }

  /**
   * 需要全部a，b，c都被设置了状态，rt才会被设置状态。如果a，b，c都为succeed，rt为succeed。如果a，b，c中有一个为failed，rt就被设为failed。
   *
   * @param futures future list
   * @param <T>     泛型
   * @return 无
   */
  static <T> CompositeFuture join(List<Future<T>> futures) {
    return CompositeFutureImpl.join(futures.toArray(new Future[0]));
  }

  /**
   * 有一个参数被设置为succeed，rt就立即被设置为succeed。如果全部a，b，c都被设为failed，rt就设为failed
   *
   * @param futures future list
   * @param <T>     泛型
   * @return 无
   */
  static <T> CompositeFuture any(List<Future<T>> futures) {
    return CompositeFutureImpl.any(futures.toArray(new Future[0]));
  }
}
