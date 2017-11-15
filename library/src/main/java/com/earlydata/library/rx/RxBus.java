package com.earlydata.library.rx;

import com.whatsmode.library.rx.RxUtil;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private static RxBus mInstance;

    private final FlowableProcessor<Object> mBusFlowable;
    private final Subject<Object> mBus;

    private RxBus() {
        mBusFlowable = PublishProcessor.create().toSerialized();
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void post(@android.support.annotation.NonNull Object obj) {
        mBus.onNext(obj);
    }

    public void postFlowable(@android.support.annotation.NonNull Object obj) {
        mBusFlowable.onNext(obj);
    }

    public <T> Observable<T> register(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public <T> Flowable<T> registerFlowable(Class<T> clz) {
        return mBusFlowable.ofType(clz);
    }

    public Observable<Object> register() {
        return mBus;
    }

    public Flowable<Object> registerFlowable() {
        return mBusFlowable;
    }

    public void unregisterAll() {
        //会将所有由mBus生成的Observable都置completed状态,后续的所有消息都收不到了
        mBus.onComplete();
    }

    public void unregisterAllFlowable() {
        //会将所有由mBus生成的Flowable都置completed状态后续的所有消息都收不到了
        mBusFlowable.onComplete();
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public boolean hasSubscribersFlowable() {
        return mBusFlowable.hasSubscribers();
    }

    public <T> Disposable toDefalut(Class<T> eventType, Consumer<T> consumer) {
        return mBus.ofType(eventType).compose(RxUtil.defalutObservableSchedule()).subscribe(consumer);
    }

    // 封装默认订阅
    public <T> Disposable toDefalutFlowable(Class<T> eventType, Consumer<T> consumer) {
        return mBusFlowable.ofType(eventType).compose(RxUtil.defalutFlowableSchedule()).subscribe(consumer);
    }

}