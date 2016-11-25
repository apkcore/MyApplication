package com.example.rxjavaapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends RxFragmentActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.img)
    ImageView img;
    private Subscription mTvShowSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv.setText("butterknife");
//        setObser1();
//        setObser2();
//        setObser3();
//        setObser4();
        setObser5();
    }

    private void setObser5() {
//        String[] names = {"a","b","c","d","e"};
//        Observable.from(names)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String name) {
//                        Log.d("bsb3", name);
//                    }
//                });

//        final ImageView imageView = (ImageView) findViewById(R.id.img);
//        Observable.create(new Observable.OnSubscribe<Drawable>() {
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher);
//                subscriber.onNext(drawable);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Drawable>() {
//            @Override
//            public void onNext(Drawable drawable) {
//                imageView.setImageDrawable(drawable);
//            }
//
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });

//        Observable.just(123).map(new Func1<Integer, String>() {
//            @Override
//            public String call(Integer integer) {
//                return String.valueOf(integer);
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                tv.setText(s);
//            }
//        });

        Observable.just("hello world!")
                .flatMap(new Func1<String, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(String s) {
                        return Observable.interval(1, TimeUnit.SECONDS);
                    }
                })
                //fuck....here
                .compose(this.<Long>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i(TAG, "....oh,oh,no!!..........." + aLong);
                    }
                });
    }

    private void setObser4() {
        PublishSubject<List<String>> mCounterEmitter = PublishSubject.create();
        mCounterEmitter.subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> colors) {
                Log.d(TAG, Thread.currentThread().getName());
                for (String color : colors) {
                    Log.d(TAG, color);
                }
            }
        });
        mCounterEmitter.onNext(getColorList());
    }

    private void setObser3() {
        Single<List<String>> listObservable = Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getColorList();
            }
        });
        mTvShowSubscription = listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> colors) {
                        for (String color : colors) {
                            Log.d(TAG, color);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    /**
     * 耗时请求
     */
    private void setObser2() {
        Observable<List<String>> listObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return getColorList();
            }
        });
        mTvShowSubscription = listObservable.compose(this.<List<String>>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<String> colors) {
                        for (String color : colors) {
                            Log.d(TAG, color);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //很好的解决了子线程的内存泄露烦恼
        if (mTvShowSubscription != null && !mTvShowSubscription.isUnsubscribed()) {
            mTvShowSubscription.unsubscribe();
        }
    }

    /**
     * 最简单的使用方法
     */
    private void setObser1() {
        //被观察发送消息
        Observable<List<String>> listObservable = Observable.just(getColorList());
        //订阅
        listObservable.subscribe(new Observer<List<String>>() {

            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<String> colors) {
                for (String color : colors) {
                    Log.d(TAG, color);
                }
            }
        });
    }

    private List<String> getColorList() {
        List<String> strs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strs.add("a" + i);
        }
        return strs;
    }
}
