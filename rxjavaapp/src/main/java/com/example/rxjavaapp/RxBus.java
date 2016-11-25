package com.example.rxjavaapp;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2016/9/7.
 */
public class RxBus {

    public static RxBus getDefault() {
        return RxBusInstance.rxBus;
    }

    private static class RxBusInstance {
        private static final RxBus rxBus = new RxBus();
    }

    /**
     * 单例
     * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     * 序列化主题
     * 将 Subject转换为一个 SerializedSubject ，类中把线程非安全的PublishSubject包装成线程安全的Subject
     */
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 主题，Subject是非线程安全的
    public final Subject bus;

    // 提供了一个新的事件用于发送,这时候的Subject相当于一个观察者
    public void post(final Object obj) {
        bus.onNext(obj);
    }


    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public Observable register(Class eventType) {
        return bus.ofType(eventType);
    }
}