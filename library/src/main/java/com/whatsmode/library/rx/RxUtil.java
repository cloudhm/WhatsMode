/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.whatsmode.library.rx;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.AbstractResponse;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;

import static com.whatsmode.library.rx.Util.fold;


public final class RxUtil {

  public static Single<Storefront.QueryRoot> rxGraphQueryCall(final GraphCall<Storefront.QueryRoot> call) {
    return Single.<GraphResponse<Storefront.QueryRoot>>create(emitter -> {
      emitter.setCancellable(call::cancel);
      try {
        emitter.onSuccess(call.execute());
      } catch (Exception e) {
        Exceptions.throwIfFatal(e);
        if(!emitter.isDisposed())
        emitter.onError(e);
      }
    }).compose(queryResponseDataTransformer());
  }

  public static Observable<Storefront.QueryRoot> rxGraphQueryCallObservable(final GraphCall<Storefront.QueryRoot> call){
    return Observable.<GraphResponse<Storefront.QueryRoot>>create(e -> {
      e.setCancellable(call::cancel);
      try {
        e.onNext(call.execute());
      } catch (Exception t) {
        Exceptions.throwIfFatal(t);
        if(!e.isDisposed())
          e.onError(t);
      }
    }).compose(queryResponseDataTransformerObservable());
  }

  public static Single<Storefront.Mutation> rxGraphMutationCall(final GraphCall<Storefront.Mutation> call) {
    return Single.<GraphResponse<Storefront.Mutation>>create(emitter -> {
      emitter.setCancellable(call::cancel);
      try {
        emitter.onSuccess(call.execute());
      } catch (Exception e) {
        Exceptions.throwIfFatal(e);
        if(!emitter.isDisposed())
        emitter.onError(e);
      }
    }).compose(queryResponseDataTransformer());
  }

  private static <T extends AbstractResponse<T>> SingleTransformer<GraphResponse<T>, T> queryResponseDataTransformer() {
    return upstream -> upstream.flatMap(response -> {
      if (response.errors().isEmpty()) {
        return Single.just(response.data());
      } else {
        String errorMessage = fold(new StringBuilder(), response.errors(),
          (builder, error) -> builder.append(error.message()).append("\n")).toString();
        return Single.error(new RuntimeException(errorMessage));
      }
    });
  }

  private static <T extends AbstractResponse<T>> ObservableTransformer<GraphResponse<T>, T> queryResponseDataTransformerObservable() {
    return upstream -> upstream.flatMap(response -> {
      if (response.errors().isEmpty()) {
        return Observable.just(response.data());
      } else {
        String errorMessage = fold(new StringBuilder(), response.errors(),
                (builder, error) -> builder.append(error.message()).append("\n")).toString();
        return Observable.error(new RuntimeException(errorMessage));
      }
    });
  }

  public static <T> FlowableTransformer<T,T> defalutFlowableSchedule() {//compose简化线程
    return new FlowableTransformer<T, T>() {
      @Override
      public Flowable<T> apply(@NonNull Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
      }
    };
  }

  public static <T>ObservableTransformer<T,T> defalutObservableSchedule() {
    return new ObservableTransformer<T, T>() {
      @Override
      public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
      }
    };
  }

  private RxUtil() {
  }

}
